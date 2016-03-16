package services.table_services

import objects.table_types.User
import objects.table_types.Users
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import services.utility_services.PasswordService


class UserService(override val tx: Transaction) : TableService<User>() {

    override fun ResultRow?.toInstance(): User? {
        this?.let {
            return User(this[Users.id], this[Users.firstname], this[Users.lastname], this[Users.username],
                    this[Users.password], this[Users.salt], this[Users.dateAdded])
        }
        return null
    }

    override fun get(id: Int): User? {
        with(tx) {
            return Users.select { Users.id eq id }.limit(1).firstOrNull()?.toInstance()
        }
    }

    override fun insert(user: User): Int {

        val (hexDigest, hexSalt) = PasswordService.saltAndDigest(user.password)
        user.password = hexDigest
        user.salt = hexSalt;

        val id = with(tx) {
            Users.insert {
                it[firstname] = user.firstname
                it[lastname] = user.lastname
                it[username] = user.username
                it[password] = user.password
                it[salt] = user.salt
            }[Users.id]
        }
        user.id = id;
        return id
    }


    fun authenticate(username: String, password: String): Boolean {
        val owner = with(tx) {
            Users.select { Users.username eq username }.limit(1).firstOrNull().toInstance()
        }
        return owner?.let {
            PasswordService.verified(password, owner.password, owner.salt);
        } ?: false
    }


    companion object {
        private val logger = LogManager.getLogger(UserService::class)
    }
}
