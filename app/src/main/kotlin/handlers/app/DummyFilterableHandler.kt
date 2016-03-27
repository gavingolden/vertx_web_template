package handlers.app

import handlers.AbstractFilterableHandler
import io.vertx.ext.web.RoutingContext

class DummyFilterableHandler: AbstractFilterableHandler() {

    override fun handleFiltered(ctx: RoutingContext) { ctx.next() }
}