package extensions


fun <T: Enum<*>> Class<T>.join(skipFirst: Int = 0, skipLast: Int = 0): String {
    return this.enumConstants
            .drop(skipFirst)
            .dropLast(skipLast)
            .map { e -> e.name }
            .joinToString()
}

fun notNull(first: Any?): Boolean {
    return first != null
}

fun notNull(first: Any?, second: Any?): Boolean {
    return first != null && second != null
}


