package objects.table_types

import kotliquery.Row
import kotliquery.Session
import kotliquery.queryOf
import org.apache.logging.log4j.LogManager
import java.sql.Timestamp

data class User (var id: Long = -1, var fname: String? = null, var lname: String? = null, var dateAdded: Timestamp? = null): TableType {

    companion object {
        const val KEY = "user"
        val cols: Array<String> = arrayOf("id", "fname", "lname", "dateAdded");
    }
}

class UserService(override val session: Session) : TableTypeSerializer<User>, TableService<User> (){

    override val toObj: (Row) -> User = { row ->
        val user = User(row.long(User.cols[0])!!, row.string(User.cols[1]), row.string(User.cols[2]), row.sqlTimestamp(User.cols[3]))
        logger.debug("Got $user")
        user;
    }

    override fun get(id: Int): User? {
        return session.single(queryOf("select * from user where id = ?", id), toObj)
    }


    companion object {
        val logger = LogManager.getLogger(UserService::class)
    }
}