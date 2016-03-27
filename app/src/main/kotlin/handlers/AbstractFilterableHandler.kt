package handlers

import filters.PostFilter
import filters.PreFilter
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.apache.logging.log4j.LogManager

/**
 * Enables manipulation of the request object before and after handling
 */
abstract class AbstractFilterableHandler : Handler<RoutingContext> {

    private val preFilters = mutableListOf<PreFilter>();
    private val postFilters = mutableListOf<PostFilter>();

    abstract fun handleFiltered(ctx: RoutingContext);

    fun addPreFilter(filter: PreFilter): AbstractFilterableHandler {
        this.preFilters.add(filter)
        return this;
    };


    fun addPreFilters(filters: List<PreFilter>): AbstractFilterableHandler {
        this.preFilters.addAll(filters);
        return this;
    }

    fun addPostFilter(filter: PostFilter): AbstractFilterableHandler {
        this.postFilters.add(filter)
        return this;
    };


    fun addPostFilters(filters: List<PostFilter>): AbstractFilterableHandler {
        this.postFilters.addAll(filters);
        return this;
    }

    final override fun handle(ctx: RoutingContext) {
        preFilters.forEach { it.execFilter(ctx); }

        if (!ctx.response().ended())
            handleFiltered(ctx);

        postFilters.forEach { it.execFilter(ctx); }
    }


    companion object {
        private val logger = LogManager.getLogger(AbstractFilterableHandler::class)
    }
}
