package objects.configuration

object ConfigurationPaths {
    private const val CONFIG_ROOT = "/etc/webconfig"
    const val DB_CONFIG = "$CONFIG_ROOT/dbconfig.json"
    const val WEB_CONFIG = "$CONFIG_ROOT/webconfig.json"
}