package handlers.common

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

class DenyHandler: Handler<RoutingContext> {
    override fun handle(ctx: RoutingContext) {
        ctx.response().end("This resource is taken. Please see yourself out.")
    }
}