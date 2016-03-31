package verticles

import handlers.common.DenyHandler
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.ErrorHandler
import io.vertx.ext.web.handler.FaviconHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.TemplateHandler
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import objects.configuration.Config
import objects.configuration.Keys
import org.apache.logging.log4j.LogManager
import routers.ApiRouterFactory
import routers.AppRouter


class WebAppVerticle : AbstractWebVerticle() {

    override fun implRouterConfig(router: Router): Router {


        router.get("/favicon.ico").handler(FaviconHandler.create())

        /**
         * Include the route path in the resolve path so that includes have accurate auto-completion
         *
         * (The matched part of the route path is removed from the request resource path once it is resolved
         * to a router)
         */
        router.get("/static/*").handler(StaticHandler
                .create("static")
                .setCachingEnabled(Config.get(Keys.AppConfig.TEMPLATE_CACHING)))

        /**
         * Explicit index handler because the supposed default 'index.html' doesn't seem to be used in the source code...
         */
        router.route("/").handler {
            it.reroute("/home.html");
        }


        router.mountSubRouter("/app", AppRouter.getRouter(vertx))

        router.mountSubRouter("/api", ApiRouterFactory.getRouter(vertx))

        // Deny access to our partials
        router.route("/fragments/*").handler(DenyHandler())

        /**
         * Render templates
         */
        val templateHandler = ThymeleafTemplateEngine.create();
        if (!Config.get<Boolean>(Keys.AppConfig.TEMPLATE_CACHING)) {
            templateHandler.thymeleafTemplateEngine.cacheManager = null
        }
        templateHandler.thymeleafTemplateEngine.cacheManager
        router.route().handler(TemplateHandler.create(templateHandler));


        /**
         * Reroute any failure to an error page.
         *
         * Need to fully qualify the template name because it will be cached immediately.
         */
        router.route().failureHandler(ErrorHandler.create("static/html/not-found.html", Config.get(Keys.AppConfig.SHOW_EXCEPTIONS_ON_ERROR)))

        return router;
    }

    companion object {

        private val logger = LogManager.getLogger(WebAppVerticle::class)

        @JvmStatic fun main(args: Array<String>) {
            Vertx.vertx().deployVerticle(WebAppVerticle())
        }
    }
}