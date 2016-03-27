package auth

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
import objects.db.Db
import services.table_services.UserService

class AppAuthProvider: AuthProvider {

    final val USER_KEY = "username"
    final val PASSWORD_KEY = "password"

    override fun authenticate(authInfo: JsonObject?, resultHandler: Handler<AsyncResult<User>>) {
        val username = authInfo?.getString(USER_KEY)
        val password = authInfo?.getString(PASSWORD_KEY)

        username?.let {
            password?.let {
                Db.source().transaction {
                    val userService = UserService(this);
                    val authUser = userService.authenticate(username, password)

                    authUser?.let {
                        resultHandler.handle(Future.succeededFuture(authUser))
                    }
                }
            }
        }
        resultHandler.handle(Future.failedFuture("Unauthenticated"))
    }
}