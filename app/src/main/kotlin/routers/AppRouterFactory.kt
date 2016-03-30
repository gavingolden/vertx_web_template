package routers

import auth.AppAuthProvider
import filters.NormalizeSuffixPreFilter
import handlers.app.DummyFilterableHandler
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.*
import io.vertx.ext.web.sstore.LocalSessionStore
import org.apache.logging.log4j.LogManager

object AppRouterFactory {
    private val logger = LogManager.getLogger(ApiRouterFactory::class)

    fun getRouter(vertx: Vertx): Router {
        val authProvider = AppAuthProvider();
        val router = Router.router(vertx)

        router.route().handler(BodyHandler.create())

        /**
         * All of these must be in the same router as the other session related handlers
         */
        router.route().handler(CookieHandler.create())
        router.route().handler(SessionHandler
                .create(LocalSessionStore.create(vertx))
                .setCookieHttpOnlyFlag(true)
                .setCookieSecureFlag(false)
                .setNagHttps(true)
//                .setSessionTimeout(sessionTimeout)
        )
        router.route().handler(UserSessionHandler.create(authProvider))

        router.route("/").handler { it.reroute("/app/home.html") }

        router.route("/doLogin").handler(FormLoginHandler
                .create(authProvider)
                .setDirectLoggedInOKURL("/app/home.html"))

        router.route().handler(RedirectAuthHandler.create(authProvider, "/login.html"))

        // Normalize route suffix
        router.route("/*").handler(DummyFilterableHandler().addPreFilter(NormalizeSuffixPreFilter(".html")))

        return router
    }
}