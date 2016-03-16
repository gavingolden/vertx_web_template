package objects.table_types

import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

object Users : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val firstname = varchar("fname", length = 32)
    val lastname = varchar("lname", length = 32)
    val username = varchar("uname", length = 32)
    val password = varchar("password", 128)
    val salt = varchar("salt", 128)
    val dateAdded = datetime("dateAdded")
}

data class User(var id: Int? = null, var firstname: String, var lastname: String, var username: String,
                var password: String, var salt: String = "", var dateAdded: DateTime? = null): TableInstance
