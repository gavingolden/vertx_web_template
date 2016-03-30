package filters

import io.vertx.ext.web.RoutingContext

class NormalizeSuffixPreFilter(val suffix: String, override val stopOnResponseEnd: Boolean = true) : PreFilter() {

    val delim = '/';

    /**
     * Reroute urls that are missing a suffix with the specified suffix
     *
     *  /app/index  --> /app/index.html
     */
    override fun filter(ctx: RoutingContext) {
        val uri = ctx.request().uri();
        if (uri.equals(delim)) return; // TODO -- this would only handle root "/"
        val pre = uri.substringBeforeLast(delim)
        val last = uri.substringAfterLast(delim);

        if (last.length > 0 && !last.contains('.')) {
            ctx.reroute("$pre$delim$last$suffix");
        }
    }
}