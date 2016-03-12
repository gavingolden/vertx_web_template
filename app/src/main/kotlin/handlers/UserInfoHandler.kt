package handlers

import io.vertx.ext.web.RoutingContext
import objects.db.Db
import objects.table_types.User
import objects.table_types.UserService
import org.apache.logging.log4j.LogManager

class UserInfoHandler : AbstractFilterableHandler() {

    override fun handleFiltered(ctx: RoutingContext) {
        val service = UserService(ctx.get(Db.KEY));

        // Execute if not null
        service.get(1)?.let { user ->
            ctx.put(User.KEY, user)
        }

        ctx.next();
    }

    companion object {
        val logger = LogManager.getLogger(UserInfoHandler::class)
    }
}
