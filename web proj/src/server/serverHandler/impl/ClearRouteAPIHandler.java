package server.serverHandler.impl;

import server.serverHandler.APIRouteHandler;
import spark.Request;
import spark.Response;

import static server.util.Constants.ROUTE_LIST;


/**
 * Handles the "Clear Route" button in Bearmaps.
 */
public class ClearRouteAPIHandler extends APIRouteHandler {


    @Override
    protected Object parseRequestParams(Request request) {
        return null;
    }

    @Override
    protected Object processRequest(Object requestParams, Response response) {
        ROUTE_LIST.clear();
        return true;
    }
}
