package auth

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
import objects.configuration.Keys
import objects.table_types.UserService
import utility.ifNotNull

class StandardUserAuthProvider : AuthProvider {
    override fun authenticate(authBlock: JsonObject?, handler: Handler<AsyncResult<User>>) {
        val username = authBlock?.getString(Keys.Auth.USER_NAME);
        val password = authBlock?.getString(Keys.Auth.PASSWORD);

        ifNotNull(username, password) {

            val service = UserService();
            val user = service.get(1);
        }
        return;
    }
}