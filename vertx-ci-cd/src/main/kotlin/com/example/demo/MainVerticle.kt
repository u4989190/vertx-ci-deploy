package com.example.demo

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    //vertx.deployVerticle(HttpVerticle())

    val outer: Promise<JsonObject> = Promise.promise()
    val inner: Promise<JsonObject> = Promise.promise()
    inner.complete()
    inner.future().onComplete {
      outer.complete()
      print(it)
    }

    outer.future().onComplete{
      print(it)
    }




//    vertx.createHttpServer().requestHandler { req ->
//        req.response()
//          .putHeader("content-type", "text/plain")
//          .end("Hello from Vert.x!")
//      }
//      .listen(8888) { http ->
//        if (http.succeeded()) {
//          startPromise.complete()
//          println("HTTP server started on port 8888")
//        } else {
//          startPromise.fail(http.cause());
//        }
//      }
  }
}
