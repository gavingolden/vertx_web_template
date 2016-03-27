package routers

import auth.AppAuthProvider
import filters.NormalizeSuffixPreFilter
import handlers.app.DummyFilterableHandler
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.FormLoginHandler
import io.vertx.ext.web.handler.RedirectAuthHandler
import org.apache.logging.log4j.LogManager

object AppRouterFactory {
    private val logger = LogManager.getLogger(ApiRouterFactory::class)

    fun getRouter(vertx: Vertx): Router {
        val router = Router.router(vertx)

        router.route().handler(RedirectAuthHandler
                .create(AppAuthProvider(), "login.html"))

        router.route().handler(FormLoginHandler
                .create(AppAuthProvider())
                .setDirectLoggedInOKURL("/app/app.html"))

        /**
         * Normalize route suffix
         */
//        router.get().handler(DummyFilterableHandler().addPreFilter(NormalizeSuffixPreFilter(".html")))

        return router
    }
}