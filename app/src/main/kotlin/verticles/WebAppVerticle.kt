package verticles

import auth.AppAuthProvider
import filters.NormalizeSuffixPreFilter
import handlers.app.DummyFilterableHandler
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.*
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import objects.configuration.ConfigurationPaths
import org.apache.logging.log4j.LogManager
import routers.ApiRouterFactory
import routers.AppRouterFactory


class WebAppVerticle : AbstractWebVerticle() {

    override fun implRouterConfig(router: Router): Router {

        /** ENABLE SESSION **/
        // TODO -- should these be in app router only?
        router.route().handler(CookieHandler.create())
        router.route().handler(SessionHandler
                .create(LocalSessionStore.create(vertx))
                .setCookieHttpOnlyFlag(true)
                .setCookieSecureFlag(!devMode)
                .setNagHttps(true)
                .setSessionTimeout(sessionTimeout)
        )
        router.route().handler(UserSessionHandler.create(AppAuthProvider()))


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
        router.route().handler(TemplateHandler.create(ThymeleafTemplateEngine.create()));


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