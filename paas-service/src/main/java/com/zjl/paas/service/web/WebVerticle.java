package com.zjl.paas.service.web;

import com.google.common.base.Throwables;
import com.zjl.paas.common.dao.MongoDao;
import com.zjl.paas.common.model.Response;
import com.zjl.paas.service.project.handler.ProjectHandler;
import com.zjl.paas.service.user.handler.UserHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019/2/28
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class WebVerticle extends AbstractVerticle {

  public static final String APPLICATION_JSON = "application/json";

  private JsonObject config;

  private MongoDao mongo;

  private UserHandler userHandler;

  private ProjectHandler projectHandler;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    config = config();
    mongo = new MongoDao(MongoClient.createShared(vertx, config.getJsonObject("mongodb")));
    userHandler = new UserHandler(mongo);
    projectHandler = new ProjectHandler(mongo);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router mainRouter = createRouter();
    int port = config.getJsonObject("http").getInteger("port");
    vertx.createHttpServer().requestHandler(mainRouter).listen(port, http -> {
      if(http.succeeded()){
        log.info("HTTP server started on {}", port);
        startPromise.complete();
      }else{
        startPromise.fail(http.cause());
      }
    });
  }

  private Router createRouter() {
    Router mainRouter = Router.router(vertx);

    commonRoute(mainRouter);
    apiRoute(mainRouter);

    return mainRouter;
  }

  private void commonRoute(Router router) {
    //静态文件优先判断，直接返回
    router.routeWithRegex(HttpMethod.GET, ".?(.htm|.ico|.css|.js|.text|.png|.jpg|.gif|.jpeg|.mp3|.avi)")
      .handler(StaticHandler.create().setCacheEntryTimeout(1000 * 60 * 60 * 24));

    router.route().handler(BodyHandler.create().setBodyLimit(10 * 1048576L)); //10M
//    mainRouter.route().handler(SessionHandler.create(LocalSessionStore.create(vertx, "myapp3.sessionmap", 10000)));
    router.route().handler(routingContext -> {
      routingContext.response()
        .setChunked(true)
        .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
        .putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");

      routingContext.next();
    });
    router.route().failureHandler(this::failure);
    router.errorHandler(404, this::failure404);
  }

  private void failure(RoutingContext frc){
    if(Objects.nonNull(frc.failure())){
      log.error("failureHandler, case by : {}", Throwables.getStackTraceAsString(frc.failure()));
    }
    frc.response().putHeader("content-type", "application/json; charset=utf-8")
      .end(Response.fail("发生了一些未知异常！").encodePrettily());
  }

  private void failure404(RoutingContext frc){
    frc.response().putHeader("content-type", "application/json; charset=utf-8")
      .end(Response.fail("界面找不到").encodePrettily());
  }

  private void apiRoute(Router router) {
    router.route("/api/*").handler(ctx -> {
      ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
      ctx.next();
    });

    Router apiRouter = Router.router(vertx);
    apiRouter.route().consumes(APPLICATION_JSON);
    apiRouter.route().produces(APPLICATION_JSON);

    /* noAuth */
    router.mountSubRouter("/api/noAuth", noAuthRouter(apiRouter));

    /* API */
    router.mountSubRouter("/api/user", userRouter(apiRouter));
    router.mountSubRouter("/api/project", projectRouter(apiRouter));

    /* SockJS / EventBus */
//    router.route("/eventbus/*").handler(eventBusHandler());
  }

  private Router noAuthRouter(Router router) {
    router.post("/register").handler(userHandler::register);
    router.get("/one").handler(userHandler::getOne);
    router.get("/all").handler(userHandler::getAll);
    router.get("/bath").handler(userHandler::getBath);

//    router.post("/login").handler(authApi::login);
//    router.post("/logout").handler(authApi::logout);

    return router;
  }

  private Router userRouter(Router router) {

//    /* login / user-related stuff : no token needed */
//    router.post("/register").handler(authApi::register);
//    router.post("/login").handler(authApi::login);
//    router.post("/logout").handler(authApi::logout);
//
//    /* API to deal with feeds : token required */
//    router.route("/feeds*").handler(userContextHandler::fromApiToken);
//    router.post("/feeds").handler(feedsApi::create);
//    router.get("/feeds").handler(feedsApi::list);
//    router.get("/feeds/:feedId").handler(feedsApi::retrieve);
//    router.put("/feeds/:feedId").handler(feedsApi::update);
//    router.get("/feeds/:feedId/entries").handler(feedsApi::entries);
//    router.delete("/feeds/:feedId").handler(feedsApi::delete);

    return router;
  }

  private Router projectRouter(Router router) {
    router.post().handler(projectHandler::save);
    router.delete().handler(projectHandler::remove);
    router.get("/find").handler(projectHandler::findWithSort);
    router.get("/update").handler(projectHandler::updates);

    return router;
  }

  private SockJSHandler eventBusHandler() {
    SockJSHandler handler = SockJSHandler.create(vertx);
    BridgeOptions options = new BridgeOptions();
    PermittedOptions permitted = new PermittedOptions(); /* allow everything, we don't care for the demo */
    options.addOutboundPermitted(permitted);
    handler.bridge(options);
    return handler;
  }
  //  router.route().handler(UserSessionHandler.create(authProvider));
//  AuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);

  // All requests to paths starting with '/private/' will be protected
//    router.route("/private/*").handler(basicAuthHandler);
//
//    router.route("/someotherpath").handler(routingContext -> {
//
//      // This will be public access - no login required
//
//    });
//
//    router.route("/private/somepath").handler(routingContext -> {
//
//      // This will require a login
//
//      // This will have the value true
//      boolean isAuthenticated = routingContext.user() != null;
//
//      //如果要登出
//      routingContext.clearUser();
//
//    });

//    router.route("some/path/").handler(routingContext -> {
//
//      Cookie someCookie = routingContext.getCookie("mycookie");
//      String cookieValue = someCookie.getValue();
//
//      // Do something with cookie...
//
//      // Add a cookie - this will get written back in the response automatically
//      routingContext.addCookie(Cookie.cookie("othercookie", "somevalue"));
//    });
//
//    router.post("/some/path/uploads").handler(routingContext -> {
//
//      Set<FileUpload> uploads = routingContext.fileUploads();
//
//      // 执行上传处理
//
//    });

//    route.handler(routingContext ->{
//      HttpServerResponse response = routingContext.response();
//      response.putHeader("content-type", "text/plain");
//      response.end("第一次访问vert.x");
//    });
//
//    //使用blockingHandler处理阻塞式请求
//    router.post("/some/endpoint").handler(ctx -> {
//      //处理表单文件
//      ctx.request().setExpectMultipart(true);
//      ctx.next();
//    }).blockingHandler(ctx -> {
//      // ... Do some blocking operation
//      //处理表单文件
//
//
//      ctx.next();
//    });


//  private void startPackage(RoutingContext routingContext){
//    if(lock == 0){
//      lock++;
//      DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//      String tag = df.format(LocalDateTime.now());
//
//      JsonObject config = config().getJsonObject("pass");
//
//      JsonObject restJsonObject = routingContext.getBodyAsJson();
//      String env = restJsonObject.getString("env");
//      JsonArray hshArray = restJsonObject.getJsonArray("hsh");
//      JsonArray herdArray = restJsonObject.getJsonArray("herd");
//
//      List<CiMeta> ciMetaList = new ArrayList(3);
//
//      if(Objects.nonNull(hshArray)){
//        ciMetaList.add(new CiMeta(hshArray));
//      }
//      if(Objects.nonNull(herdArray)){
//        herdArray.stream().forEach(str -> {
//          cmdList.add(new String[]{"/bin/sh", "-c", path + "/sh/pass-herd-web.sh " + branch + " " + tag});
//        });
//      }
//      run(config, env, hshArray, herdArray, cmdList);
//
//      routingContext.response()
//        .putHeader("content-type", "application/json; charset=utf-8")
//        .end(jsonObject.clear().put("result", "蛇皮怪,哔哩哔哩-->启动发包流程！").encodePrettily());
//    }else{
//      routingContext.response()
//        .putHeader("content-type", "application/json; charset=utf-8")
//        .end(new JsonObject().put("result", "有个蛇皮怪提前点了发包，你歇着吧！").encodePrettily());
//    }
//  }

//  public void run(JsonObject config, String env, JsonArray hshArray,JsonArray herdArray, List<String[]> cmdList){
//
//    List<java.util.concurrent.Future> list = new ArrayList();
//    WorkerExecutor executor = vertx.createSharedWorkerExecutor("my-worker-pool", 4, 10, TimeUnit.MINUTES);
//
//    executor.executeBlocking(exeFuture -> {
//      cmdList.forEach(str -> {
//        list.add(java.util.concurrent.Future.(future -> {
////          GitOperation gitOperation = new GitOperation();
//
//          try {
//            Process process = Runtime.getRuntime().exec(str);
//            java.util.concurrent.Future.future(infoFuture -> {
//              BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//              String line = null;
//              StringBuffer sb = new StringBuffer("");
//              try {
//                while ((line = br.readLine()) != null) {
//                  sb.append(line);
//                }
//                br.close();
//              } catch (Exception e) {
//                log.error("读取脚本输出信息出错", e);
//              }
//              log.info(sb.toString());
//              infoFuture.complete();
//            });
//
//            Future.future(errorFuture -> {
//              BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//              String line = null;
//              StringBuffer sb = new StringBuffer("");
//              try {
//                while ((line = br.readLine()) != null) {
//                  sb.append(line);
//                }
//                br.close();
//              } catch (Exception e) {
//                log.error("读取脚本错误信息出错", e);
//              }
//              log.error("Process error log : {}", sb.toString());
//              errorFuture.complete();
//            });
//            process.waitFor();
//          } catch (Exception e) {
//            log.error("run sh scripts failed! ", e);
//          }
//          future.complete();
//        }));
//      });
//      CompositeFuture.all(list).setHandler(r -> {
//        lock--;
//      });
//      exeFuture.complete("");
//    }, res -> {
//      System.out.println("发包完成~~~~~~~~~~~~~~~~~~~~~");
//    });
//    executor.close();
//  }


//  public void run(JsonObject config, String env, JsonArray hshArray,JsonArray herdArray, List<String[]> cmdList){
//
//    List<Future> list = new ArrayList();
//    WorkerExecutor executor = vertx.createSharedWorkerExecutor("my-worker-pool", 4, 10, TimeUnit.MINUTES);
//
//
//
//    executor.executeBlocking(exeFuture -> {
//      cmdList.forEach(str -> {
//        list.add(Future.future(future -> {
//          try {
//            Process process = Runtime.getRuntime().exec(str);
//            Future.future(infoFuture -> {
//              BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//              String line = null;
//              StringBuffer sb = new StringBuffer("");
//              try {
//                while ((line = br.readLine()) != null) {
//                  sb.append(line);
//                }
//                br.close();
//              } catch (Exception e) {
//                log.error("读取脚本输出信息出错", e);
//              }
//              log.info(sb.toString());
//              infoFuture.complete();
//            });
//
//            Future.future(errorFuture -> {
//              BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//              String line = null;
//              StringBuffer sb = new StringBuffer("");
//              try {
//                while ((line = br.readLine()) != null) {
//                  sb.append(line);
//                }
//                br.close();
//              } catch (Exception e) {
//                log.error("读取脚本错误信息出错", e);
//              }
//              log.error("Process error log : {}", sb.toString());
//              errorFuture.complete();
//            });
//            process.waitFor();
//          } catch (Exception e) {
//            log.error("run sh scripts failed! ", e);
//          }
//          future.complete();
//        }));
//      });
//      CompositeFuture.all(list).setHandler(r -> {
//          lock--;
//      });
//      exeFuture.complete("");
//    }, res -> {
//      System.out.println("发包完成~~~~~~~~~~~~~~~~~~~~~");
//    });
//    executor.close();
//  }
}
