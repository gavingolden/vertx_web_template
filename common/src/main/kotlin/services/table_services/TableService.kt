package services.table_services

import objects.table_types.TableInstance
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction


abstract class TableService <T: TableInstance> {

    abstract val tx: Transaction

    abstract fun ResultRow?.toInstance(): T?

    abstract fun get(id: Int): T?;

    abstract fun insert(user: T): Int;
}
