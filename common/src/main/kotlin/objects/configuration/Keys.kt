package objects.configuration

object Keys {

    object DbConfig {
        const val DB_NAME = "databaseName"
        const val SERVER_NAME = "serverName"
        const val PORT = "port"
        const val USER = "user"
        const val PASSWORD = "password"
        const val DATA_SOURCE_CLASS = "dataSourceClassName"
        const val DATA_SOURCE_CONFIG_BLOCK = "dataSourceConfig"
    }

    object AppConfig {
        const val DEV_MODE = "devMode"
        const val PORT = "port"
        const val TEMPLATE_CACHING = "templCachingEnabled"
        const val SHOW_EXCEPTIONS_ON_ERROR = "showExceptionOnError"
        const val SESSION_TIMEOUT = "sessionTimeout"
    }

    object Auth {
        const val CREDS = "auth"
        const val USER_NAME = "user"
        const val PASSWORD = "password"
    }
}