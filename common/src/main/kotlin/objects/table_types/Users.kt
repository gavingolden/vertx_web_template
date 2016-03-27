package objects.table_types

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
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
                var password: String, var salt: String = "", var dateAdded: DateTime? = null): io.vertx.ext.auth.User, TableInstance {
    override fun isAuthorised(authority: String?, resultHandler: Handler<AsyncResult<Boolean>>?): User? {
        throw UnsupportedOperationException()
    }

    override fun clearCache(): User? {
        throw UnsupportedOperationException()
    }

    override fun setAuthProvider(authProvider: AuthProvider?) {
        throw UnsupportedOperationException()
    }

    override fun principal(): JsonObject? {
        throw UnsupportedOperationException()
    }
}
