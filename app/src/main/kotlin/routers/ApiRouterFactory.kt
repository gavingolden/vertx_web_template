package routers

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.apache.logging.log4j.LogManager

object ApiRouterFactory {

    private val logger = LogManager.getLogger(ApiRouterFactory::class)

    fun getRouter(vertx: Vertx): Router {
        val router = Router.router(vertx)

        router.route().handler(BodyHandler.create())

        router.route().handler {
            logger.info("Received API request")
            it.next()
        }

        return router
    }
}