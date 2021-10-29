package io.nytou.web.acp;


import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinVelocity;
import io.javalin.plugin.rendering.vue.VueComponent;
import io.nytou.web.acp.controller.LoginController;
import io.nytou.web.acp.database.MongoDBDatabase;
import io.nytou.web.acp.handler.UserHandler;
import io.nytou.web.acp.index.IndexController;
import io.nytou.web.acp.path.Path;
import io.nytou.web.acp.view.ViewUtil;

import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    private UserHandler userHandler = new UserHandler();

    public static Main instance;

    private final MongoDBDatabase mongoDBDatabase;

    public Main() {

        instance = this;

        this.mongoDBDatabase = new MongoDBDatabase(27017, "backend", "127.0.0.1");
        this.mongoDBDatabase.connect();

        if (this.mongoDBDatabase.getBackendUsers().size() >= 1) {
            System.out.println("found: " + this.mongoDBDatabase.getBackendUsers().size() + this.mongoDBDatabase.getBackendUsers().get(0).getUserName());
        }

        JavalinRenderer.register(JavalinVelocity.INSTANCE, ".vm");
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH);
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
        }).start(7001);

        app.routes(() -> {
            before(LoginController.ensureLoginBeforeViewingBooks);
            post(Path.Web.LOGIN, LoginController.handleLoginPost);

        });

        app.error(404, ViewUtil.notFound);
    }

    public MongoDBDatabase getMongoDBDatabase() {
        return mongoDBDatabase;
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        new Main();
    }

}
