package routers

import auth.AppAuthProvider
import filters.NormalizeSuffixPreFilter
import handlers.app.DummyFilterableHandler
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.*
import io.vertx.ext.web.sstore.LocalSessionStore
import objects.configuration.Config
import objects.configuration.Keys
import org.apache.logging.log4j.LogManager
import util.Web

object AppRouter {
    private val logger = LogManager.getLogger(ApiRouterFactory::class)

    fun getRouter(vertx: Vertx): Router {
        val authProvider = AppAuthProvider();
        val router = Router.router(vertx)

        router.post().handler(BodyHandler.create())

        /**
         * Session related handler must be in the same context/router where they are used
         */
        router.route().handler(CookieHandler.create())

        router.route().handler(SessionHandler
                .create(LocalSessionStore.create(vertx))
                .setCookieHttpOnlyFlag(true)
                .setCookieSecureFlag(false)
                .setNagHttps(false)
                .setSessionTimeout(Config.get<Long>(Keys.AppConfig.SESSION_TIMEOUT))
        )
        router.route().handler(UserSessionHandler.create(authProvider))

        /**
         * This must be placed before our redirect handler or our login
         * actions will never be evaluated
         */
        router.post("/login").handler(FormLoginHandler
                .create(authProvider)
                .setDirectLoggedInOKURL("/app/home.html"))

        /**
         * Unauthenticated requests go back to jail
         */
        router.route().handler(RedirectAuthHandler.create(authProvider, "/login.html"))


        // Reroute to home
        router.get("/").handler { it.reroute("/app/home.html") }

        // Logout and redirect to root
        router.post("/logout").handler {
            it.session().destroy()
            Web.doRedirect(it.response(), "/")
        }

        // Normalize route suffix
        router.get("/*").handler(DummyFilterableHandler().addPreFilter(NormalizeSuffixPreFilter(".html")))

        return router
    }
}