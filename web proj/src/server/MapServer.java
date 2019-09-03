package server;


import server.serverHandler.APIRouteHandlerFactory;

/**
 * This code is using BearMaps skeleton code version 4.0.
 */
public class MapServer {


    /**
     * This is where the MapServer is started.
     * @param args
     */
    public static void main(String[] args) {

        MapServerInitializer.initializeServer(APIRouteHandlerFactory.handlerMap);

    }

}
