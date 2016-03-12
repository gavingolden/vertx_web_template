package objects.responses

data class Response(val success: Boolean = true, val e: Throwable? = null);