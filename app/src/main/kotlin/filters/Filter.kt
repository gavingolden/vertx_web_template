package filters

import io.vertx.ext.web.RoutingContext

abstract class Filter {

    /**
     * Should the filter/handler chain end if the response has ended
     */
    abstract val stopOnResponseEnd: Boolean

    /**
     * Perform the filtering
     */
    abstract protected fun filter(ctx: RoutingContext);

    /**
     * Call method. Performs guards before filtering
     */
    fun execFilter(ctx: RoutingContext) {
        if (ctx.response().ended())
            return;
        filter(ctx);
    }
}