package objects.db

import com.google.common.base.Joiner
import com.zaxxer.hikari.HikariDataSource
import io.vertx.core.json.JsonObject
import kotliquery.Session
import kotliquery.sessionOf
import objects.exceptions.StopException
import org.apache.logging.log4j.LogManager
import org.mariadb.jdbc.MariaDbDataSource
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Connection

object Db {
    val logger = LogManager.getLogger(Db::class)
    const val KEY = "db";

    const val configPath = "/usr/local/web/dbconfig.json";
    private var hikari = HikariDataSource();

    init {

        try {
            val json = JsonObject(String(Files.readAllBytes(Paths.get(configPath))))

            hikari.username = json.getString("user");
            hikari.dataSourceClassName = json.getString("dataSourceClassName");

            val mdb = MariaDbDataSource()

            mdb.databaseName = json.getString("databaseName");
            mdb.port = json.getInteger("port");
            mdb.serverName = json.getString("serverName");
            mdb.user = json.getString("user");
            mdb.setPassword(json.getString("password"));

            val driverConfig = json.getJsonObject("dataSourceConfig")
            val propsStr = Joiner.on('&').join(driverConfig.map.entries);
            mdb.setProperties(propsStr);
            hikari.dataSource = mdb;
        }
        catch(e: Exception) {
            val msg = "Error setting up connection pool"
            logger.fatal(msg, e)
            throw StopException(msg, e);
        }
    }

    fun source(): Session {
        return sessionOf(hikari.dataSource)
    }
}