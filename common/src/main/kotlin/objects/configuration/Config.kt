package objects.configuration

import io.vertx.core.json.JsonObject
import java.nio.file.Files
import java.nio.file.Paths

object Config {
    private val config: JsonObject = JsonObject(String(Files.readAllBytes(Paths.get(ConfigurationPaths.WEB_CONFIG))))

    fun <T> get(key: String): T = get(key, null)
    fun <T> get(key: String, default: Any?): T = config.getValue(key, default) as T
}