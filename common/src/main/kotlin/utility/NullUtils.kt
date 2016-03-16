package utility

inline fun ifNotNull(vararg items: Any?, body: () -> Unit): Any? {

    for (item in items) {
        if (item == null) {
            return Unit;
        }
    }
    return body();
}

fun noneNull(vararg items: Any?): Boolean {

    for (item in items) {
        if (item == null) {
            return false;
        }
    }
    return true;
}