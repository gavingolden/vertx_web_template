import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import verticles.AbstractWebVerticle

class BlockingHandlerTest: AbstractWebVerticle() {
    override fun implRouterConfig(router: Router): Router {
        router.route().blockingHandler { ctx ->
            vertx.setTimer(5000, {
                ctx.response().end("Finished")
            })
        }
        return router
    }
}

fun main(args: Array<String>) {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(BlockingHandlerTest())
}