package verticles

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.impl.Utils
import io.vertx.ext.web.sstore.LocalSessionStore
import objects.configuration.Keys
import org.apache.logging.log4j.LogManager

abstract class AbstractWebVerticle : AbstractVerticle() {

    protected val port: Int;
    protected val templCachingEnabled: Boolean;
    protected  val showExceptionOnError: Boolean;
    protected  val sessionTimeout: Long;
    val devMode: Boolean;

    init {
        port = config().getInteger(Keys.AppConfig.PORT);
        templCachingEnabled = config().getBoolean(Keys.AppConfig.TEMPLATE_CACHING, true);
        showExceptionOnError = config().getBoolean(Keys.AppConfig.SHOW_EXCEPTIONS_ON_ERROR, false);
        sessionTimeout = config().getLong(Keys.AppConfig.SESSION_TIMEOUT, 1000 * 60 * 10);
        devMode = config().getBoolean(Keys.AppConfig.DEV_MODE, false);
    }

    override fun start() {

        val router = Router.router(vertx)
        preRouterConfig(router)
        implRouterConfig(router)
        postRouterConfig(router)


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

    fun staticResourcePath(resource: String) = "$staticPath$resource";




    companion object {
        private val logger = LogManager.getLogger(WebAppVerticle::class.java)

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
                throw Error(msg, e);
            }
        }
    }
}
