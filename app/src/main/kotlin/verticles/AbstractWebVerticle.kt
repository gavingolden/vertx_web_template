package verticles

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import objects.configuration.Config
import objects.configuration.Keys
import org.apache.logging.log4j.LogManager

abstract class AbstractWebVerticle : AbstractVerticle() {

    override fun start() {

        val router = Router.router(vertx)
        preRouterConfig(router)
        implRouterConfig(router)
        postRouterConfig(router)

        val port = Config.get<Int>(Keys.AppConfig.PORT)

        // Get the party started
        vertx.createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(port) {
                    logger.info("Server listening on port $port");
                }
    }

    /**
     * Configure router
     */
    abstract fun implRouterConfig(router: Router): Router;

    private fun preRouterConfig(router: Router): Router {
        return router;
    }

    private fun postRouterConfig(router: Router): Router {
        return router;
    }

    companion object {
        private val logger = LogManager.getLogger(WebAppVerticle::class.java)
    }
}
