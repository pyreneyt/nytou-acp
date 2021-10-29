package io.nytou.web.acp.controller;

import io.javalin.http.Handler;
import io.nytou.web.acp.Main;
import io.nytou.web.acp.path.Path;
import io.nytou.web.acp.view.ViewUtil;

import java.util.Map;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;
import static io.nytou.web.acp.request.RequestUtil.*;

public class LoginController {


    public static Handler handleLoginPost = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        if (Main.instance.getUserHandler().authenticate(getQueryUsername(ctx), getQueryPassword(ctx))) {
            model.put("authenticationSucceeded", true);
            model.put("currentUser", getQueryUsername(ctx));
            model.put("welcomeMessage", "Welcome to my website");
            ctx.sessionAttribute("currentUser", getQueryUsername(ctx));
            ctx.render(Path.Template.DASHBOARD, model);
            System.out.println("logged in");
        }else {
            model.put("authenticationFailed", true);
            ctx.render(Path.Template.INDEX, model);
        }

    };


    public static Handler ensureLoginBeforeViewingBooks = ctx -> {
        if (!ctx.path().startsWith("/dashboard")) {
            return;
        }
        if (ctx.sessionAttribute("currentUser") == null) {
            ctx.sessionAttribute("loginRedirect", ctx.path());
            ctx.redirect(Path.Web.INDEX);
        }
    };


}
