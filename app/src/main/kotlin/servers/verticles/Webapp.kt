package servers.verticles

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.TemplateHandler
import io.vertx.ext.web.templ.ThymeleafTemplateEngine
import org.apache.logging.log4j.LogManager


class Webapp: AbstractVerticle() {

    override fun start() {
        val port = 8080
        val router = Router.router(vertx)
        val engine = ThymeleafTemplateEngine.create()
        val templateHandler = TemplateHandler.create(engine)


        /**
         * Common handler. Everything will pass through this.
         */
        router.route().handler { ctx ->
            ctx.put("msg", "hola");
            ctx.next();
        }

        /**
         * Include the route path in the resolve path so that includes have accurate auto-completion
         *
         * (The matched part of the route path is removed from the request resource path once it is resolved
         * to a router)
         */
        router.route("/static/*").handler(StaticHandler.create("src/main/resources/static").setCachingEnabled(false))

        /**
         * Explicit index handler because the supposed default 'index.html' doesn't seem to be used in the source code...
         */
        router.route("/").handler {
            it.reroute("/index.html");
        }

        /**
         * Everything that the static handler doesn't get should route through the template handler
         */
        router.route().handler(templateHandler);


        /**
         * Get the party started
         */
        vertx.createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(port) {
                    logger.info("Server listening on port $port");
                }
    }

    companion object {

        @JvmStatic val logger = LogManager.getLogger(Webapp::class.java)

        @JvmStatic fun main(args: Array<String>) {

            Vertx.vertx().deployVerticle(Webapp())
        }
    }
}