package server.serverHandler.impl;

import server.serverHandler.APIRouteHandler;
import spark.Request;
import spark.Response;

/**
 * Created by rahul
 */
public class RedirectAPIHandler extends APIRouteHandler {
    @Override
    protected Object parseRequestParams(Request request) {
        return null;
    }

    @Override
    protected Object processRequest(Object requestParams, Response response) {
        response.redirect("/map.html", 301);
        return true;
    }

    @Override
    protected Object buildJsonResponse(Object result) {
        return true;
    }
}
