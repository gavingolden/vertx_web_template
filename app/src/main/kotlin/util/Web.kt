package util

import io.vertx.core.http.HttpServerResponse

object Web {
    fun doRedirect(response: HttpServerResponse, url: String) {
        response.putHeader("location", url).setStatusCode(302).end()
    }
}
