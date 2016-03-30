package verticles

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.ErrorHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.TemplateHandler
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import objects.configuration.ConfigurationPaths
import org.apache.logging.log4j.LogManager
import routers.ApiRouterFactory
import routers.AppRouterFactory


class WebAppVerticle : AbstractWebVerticle() {

    override fun implRouterConfig(router: Router): Router {

        /**
         * Include the route path in the resolve path so that includes have accurate auto-completion
         *
         * (The matched part of the route path is removed from the request resource path once it is resolved
         * to a router)
         */
        router.route("/static/*").handler(StaticHandler
                .create("src/main/resources/static")
                .setCachingEnabled(templCachingEnabled))

        /**
         * Explicit index handler because the supposed default 'index.html' doesn't seem to be used in the source code...
         */
        router.route("/").handler {
            it.reroute("/home.html");
        }


        router.mountSubRouter("/app", AppRouterFactory.getRouter(vertx))

        router.mountSubRouter("/api", ApiRouterFactory.getRouter(vertx))


        /**
         * Everything that the static handler doesn't get should route through the template handler
         */
        val templateHandler = ThymeleafTemplateEngine.create();
        if (!templCachingEnabled) {
            templateHandler.thymeleafTemplateEngine.cacheManager = null
        }
        templateHandler.thymeleafTemplateEngine.cacheManager
        router.route().handler(TemplateHandler.create(templateHandler));


        /**
         * Reroute any failure to an error page.
         *
         * Need to fully qualify the template name because it will be cached immediately.
         */
        router.route().failureHandler(ErrorHandler.create(staticResourcePath("html/not-found.html"), showExceptionOnError))

        return router;
    }

    companion object {

        private val logger = LogManager.getLogger(WebAppVerticle::class)

        @JvmStatic fun main(args: Array<String>) {
            val vertx = Vertx.vertx();
            val opts = getDeployOpts(vertx, ConfigurationPaths.WEB_CONFIG)
            vertx.deployVerticle(WebAppVerticle(), opts)
        }
    }
}