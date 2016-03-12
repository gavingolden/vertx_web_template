package verticles

import objects.exceptions.StopException
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.impl.Utils
import io.vertx.ext.web.sstore.LocalSessionStore
import objects.context.ConfigKeys
import org.apache.logging.log4j.LogManager

abstract class AbstractWebVerticle(val enableSession: Boolean) : AbstractVerticle() {

    private val port: Int;
    private val templCachingEnabled: Boolean;
    private val showExceptionOnError: Boolean;
    private val sessionTimeout: Long;
    val devMode: Boolean;

    init {
        port = config().getInteger(ConfigKeys.PORT);
        templCachingEnabled = config().getBoolean(ConfigKeys.TEMPLATE_CACHING, true);
        showExceptionOnError = config().getBoolean(ConfigKeys.SHOW_EXCEPTIONS_ON_ERROR, false);
        sessionTimeout = config().getLong(ConfigKeys.SESSION_TIMEOUT, 1000 * 60 * 10);
        devMode = config().getBoolean(ConfigKeys.DEV_MODE, false);
    }

    override fun start() {

        val router = setupRouter(getRouter());

        // Get the party started
        vertx.createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(port) {
                    WebAppVerticle.logger.info("Server listening on port $port");
                }
    }

    /**
     * Configure router
     */
    abstract fun setupRouter(router: Router): Router;

    fun getRouter(): Router {
        val router = Router.router(vertx)

        if (enableSession) {
            router.route().handler(
                    SessionHandler
                            .create(LocalSessionStore.create(vertx))
                            .setCookieHttpOnlyFlag(true)
                            .setCookieSecureFlag(!devMode)
                            .setNagHttps(true)
                            .setSessionTimeout(sessionTimeout)
            )
        }
        return router;
    }

    fun staticResourcePath(resource: String) = "$staticPath$resource";




    companion object {
        val logger = LogManager.getLogger(WebAppVerticle::class.java)

        private val staticPath = "static/";

        /**
         * Read deployment options from a json file
         */
        fun getDeployOpts(vertx: Vertx, configPath: String?): DeploymentOptions {
            val opts = DeploymentOptions()
            if (configPath == null) return opts;

            try {
                opts.fromJson(JsonObject(Utils.readFileToString(vertx, configPath)))
                return opts
            }
            catch (e: Exception) {
                val msg = "Error reading db config from $configPath"
                logger.fatal(msg)
                throw StopException(msg, e);
            }
        }
    }
}
