package com.example.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class HttpVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);


    SockJSBridgeOptions options = new SockJSBridgeOptions()
      .addOutboundPermitted(new PermittedOptions().setAddress("news-feed"));
    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);

    sockJSHandler.bridge(options, event -> {

      // You can also optionally provide a handler like this which will be passed any events that occur on the bridge
      // You can use this for monitoring or logging, or to change the raw messages in-flight.
      // It can also be used for fine grained access control.

      if (event.type() == BridgeEventType.SOCKET_CREATED) {
        System.out.println("A socket was created");
      }

      // This signals that it's ok to process the event
      event.complete(true);

    });
    Route handler = router.route("/eventbus/*").handler(sockJSHandler);


    // Serve the static resources
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router).listen(8080);

    // Publish a message to the address "news-feed" every second
    vertx.setPeriodic(1000, t -> vertx.eventBus().publish("news-feed", "news from the server!"));
  }


}
