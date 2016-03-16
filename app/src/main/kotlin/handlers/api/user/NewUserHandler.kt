package handlers.api.user

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import kotliquery.queryOf
import kotliquery.using
import objects.configuration.Keys
import objects.db.Db
import utility.ifNotNull

class NewUserHandler : Handler<RoutingContext> {
    override fun handle(ctx: RoutingContext) {
        val authBlock = ctx.bodyAsJson.getJsonObject(Keys.Auth.CREDS)
        val username = authBlock.getString(Keys.Auth.USER_NAME)
        val password = authBlock.getString(Keys.Auth.PASSWORD)

        ifNotNull(username, password) {
            using(Db.source()) { dbSession ->
                dbSession.transaction { tx ->
                    tx.update(queryOf("INSERT"))
                }
            }
        }
    }

}