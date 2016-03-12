package objects.table_types

import kotliquery.Row
import org.apache.logging.log4j.LogManager
import java.sql.ResultSet

/**
 * A table object
 */
interface TableType;

interface TableTypeSerializer <out T: TableType> {

    val toObj: (Row) -> T;

    fun <T> fail(e: Exception): T? {
        logger.error(e);
        return null;
    }

    companion object {
        val logger = LogManager.getLogger(TableTypeSerializer::class)
    }
}