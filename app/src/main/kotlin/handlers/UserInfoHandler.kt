package handlers

import io.vertx.ext.web.RoutingContext
import objects.table_types.Users
import objects.table_types.UserService
import org.apache.logging.log4j.LogManager

class UserInfoHandler : AbstractFilterableHandler() {

    override fun handleFiltered(ctx: RoutingContext, dbSession: Session) {

        val service = UserService;

        // Execute if not null
        service.get(1)?.let { user ->
            ctx.put(Users.TABLE, user)
        }

        ctx.next();
    }

    companion object {
        private val logger = LogManager.getLogger(UserInfoHandler::class)
    }
}
