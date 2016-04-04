package util

import io.vertx.core.http.HttpServerResponse

object Web {
    fun doPostRedirect(response: HttpServerResponse, url: String) {
        doRedirect(response, url, 303)
    }

    fun doRedirect(response: HttpServerResponse, url: String, httpStatusCode: Int) {
        response.putHeader("location", url).setStatusCode(httpStatusCode).end()
    }
}
