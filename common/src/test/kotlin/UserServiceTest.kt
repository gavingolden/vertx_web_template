import objects.db.Db
import org.apache.logging.log4j.LogManager
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.shouldBeNull
import org.jetbrains.spek.api.shouldEqual
import org.jetbrains.spek.api.shouldNotBeNull
import services.table_services.UserService
import services.utility_services.RandDataService

class UserServiceTest: Spek() { init {

    val firstUser = RandDataService.randomTestUser()
    val logger = LogManager.getLogger(UserServiceTest::class)

    given("a user ${firstUser.firstname} ${firstUser.lastname}") {
        on("inserting into database") {
            it("should return the same user when retrieved using the returned id") {
                Db.source().transaction {
                    val service = UserService(this)
                    val userId = service.insert(firstUser);
                    val newUser = service.get(userId);

                    shouldNotBeNull(newUser)
                    shouldEqual(userId, newUser?.id)
                }
            }
        }
    }

    given("an existing user $firstUser") {
        on ("retrieving this user") {
            it("should have the same name") {
                Db.source().transaction {
                    val service = UserService(this)
                    val retUser = service.get(firstUser.id!!)
                    shouldEqual(retUser?.firstname, firstUser.firstname)
                    shouldEqual(retUser?.lastname, firstUser.lastname)
                }
            }
        }
    }

    given("a nested transaction") {
        val id = 1
        on("running the child transaction inside the parent transaction") {
            it("should work") {
                Db.source().transaction {
                    val service = UserService(this)

                    val user1 = service.get(id);

                    Db.source().transaction {
                        val user2 = UserService(this).get(id)

                        shouldEqual(user1?.id, id);
                        shouldEqual(user2?.id, id);
                    }
                }
            }
        }
    }

    given("two separate transactions") {
        on("executing them both consecutively") {
            it("should safely perform both transactions") {
                Db.source().transaction {
                    val service = UserService(this)
                    shouldEqual(service.get(1), service.get(1))
                }
            }
        }
    }

    given("an insert transaction followed by a failed nested transaction") {
        val randUser = RandDataService.randomTestUser()
        on("failing the nested transaction") {
            it("should still allow the parent insert to persist") {
                Db.source().transaction {
                    val service = UserService(this)
                    try {
                        service.insert(randUser)
                        Db.source().transaction {
                            prepareStatement("select * from u").execute() // Fail...
                        }
                    } catch(e: Throwable) { }

                    val persistedUser = service.get(randUser.id!!)
                    shouldNotBeNull(persistedUser)
                }
            }
        }
    }

    given("an insert transaction followed by a failed transaction") {
        val randUser = RandDataService.randomTestUser()
        on("failing the sibling transaction") {
            it("should caused the initial insert to rollback") {
                try {
                    Db.source().transaction {
                        UserService(this).insert(randUser)
                        prepareStatement("select * from u").execute() // Fail...
                    }
                }
                catch(e: Throwable) { }
                Db.source().transaction {
                    val service = UserService(this)
                    val persistedUser = service.get(randUser.id!!)
                    shouldBeNull(persistedUser)
                }
            }
        }
    }

}}