package verticles

import filters.NormalizeSuffixPreFilter
import handlers.UserInfoHandler
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CookieHandler
import io.vertx.ext.web.handler.ErrorHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.TemplateHandler
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import org.apache.logging.log4j.LogManager


class WebAppVerticle : AbstractWebVerticle(enableSession = true) {

    override fun setupRouter(router: Router): Router {

        /**
         * Common handler. Set context data that everything should have.
         */
        router.route().handler(UserInfoHandler().addPreFilter(NormalizeSuffixPreFilter(".html")))

        router.route("/app/*").handler {
            logger.info("In app")
            it.next()
        }

        /**
         * Include the route path in the resolve path so that includes have accurate auto-completion
         *
         * (The matched part of the route path is removed from the request resource path once it is resolved
         * to a router)
         */
        router.route("/static/*").handler(StaticHandler.create("src/main/resources/static").setCachingEnabled(templCachingEnabled))

        /**
         * Explicit index handler because the supposed default 'index.html' doesn't seem to be used in the source code...
         */
        router.route("/").handler {
            it.reroute("/index");
        }

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

        @JvmStatic val logger = LogManager.getLogger(WebAppVerticle::class.java)

        @JvmStatic fun main(args: Array<String>) {
            val vertx = Vertx.vertx();
            val opts = getDeployOpts(vertx, "conf.json")
            vertx.deployVerticle(WebAppVerticle(), opts)
        }
    }
}