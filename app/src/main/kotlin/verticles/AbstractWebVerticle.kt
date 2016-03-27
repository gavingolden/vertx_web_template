package verticles

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.impl.Utils
import objects.configuration.Keys
import org.apache.logging.log4j.LogManager

abstract class AbstractWebVerticle : AbstractVerticle() {

    protected var port: Int = 8080;
    protected var templCachingEnabled: Boolean = true;
    protected  var showExceptionOnError: Boolean = false;
    protected  var sessionTimeout: Long = 1000 * 60 * 10;
    var devMode: Boolean = false;

    override fun start() {

        port = config().getInteger(Keys.AppConfig.PORT);
        templCachingEnabled = config().getBoolean(Keys.AppConfig.TEMPLATE_CACHING, templCachingEnabled);
        showExceptionOnError = config().getBoolean(Keys.AppConfig.SHOW_EXCEPTIONS_ON_ERROR, showExceptionOnError);
        sessionTimeout = config().getLong(Keys.AppConfig.SESSION_TIMEOUT, sessionTimeout);
        devMode = config().getBoolean(Keys.AppConfig.DEV_MODE, devMode);

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
