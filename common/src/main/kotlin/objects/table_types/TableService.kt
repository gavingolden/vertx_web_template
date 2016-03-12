package objects.table_types

import kotliquery.Session

abstract class TableService <T: TableType> {

    abstract val session: Session;

    abstract fun get(id: Int): T?;
}

//object