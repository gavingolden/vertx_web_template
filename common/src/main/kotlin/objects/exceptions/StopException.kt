package objects.exceptions

open class StopException: Error {

    constructor(str: String) {
        Error(str);
    }

    constructor(str: String, e: Throwable) {
        Error(str, e)
    }
}