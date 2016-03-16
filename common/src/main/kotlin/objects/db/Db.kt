package objects.db

import com.google.common.base.Joiner
import com.zaxxer.hikari.HikariDataSource
import io.vertx.core.json.JsonObject
import objects.configuration.ConfigurationPaths
import objects.configuration.Keys.DbConfig
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.Database
import org.mariadb.jdbc.MariaDbDataSource
import java.nio.file.Files
import java.nio.file.Paths

object Db {
    private val logger = LogManager.getLogger(Db::class)
    private var hikari = HikariDataSource();
    lateinit private var db: Database;

    init {

        try {
            val json = JsonObject(String(Files.readAllBytes(Paths.get(ConfigurationPaths.DB_CONFIG))))

            hikari.username = json.getString(DbConfig.USER);
            hikari.dataSourceClassName = json.getString(DbConfig.DATA_SOURCE_CLASS);

            val mdb = MariaDbDataSource()

            mdb.databaseName = json.getString(DbConfig.DB_NAME);
            mdb.port = json.getInteger(DbConfig.PORT);
            mdb.serverName = json.getString(DbConfig.SERVER_NAME);
            mdb.user = json.getString(DbConfig.USER);
            mdb.setPassword(json.getString(DbConfig.PASSWORD));

            val driverConfig = json.getJsonObject(DbConfig.DATA_SOURCE_CONFIG_BLOCK)

            // The driver only accepts config properties in url form...
            val propsStr = Joiner.on('&').join(driverConfig.map.entries);
            mdb.setProperties(propsStr);

            hikari.dataSource = mdb;

            db = Database.connect(hikari.dataSource)
        }
        catch(e: Exception) {
            val msg = "Error setting up connection pool"
            logger.fatal(msg, e)
            throw Error(msg, e);
        }
    }

    fun source(): Database {
        return db;
    }
}