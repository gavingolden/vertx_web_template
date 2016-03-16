import objects.db.Db
import objects.table_types.Users
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert


fun main(args: Array<String>) {

    val logger = LogManager.getLogger()

    Db.source().transaction {

        create(Users)

        val id = Users.insert {
            it[firstname] = "Bafilda"
            it[lastname] = "Thomlinson"
            it[username] = "baf123"
            it[password] = "Password"
            it[salt] = "pink"
        } get Users.id

        Users.deleteWhere {  Users.id eq id }

        logger.info("Created user with id $id");

        drop(Users)
    }
}


