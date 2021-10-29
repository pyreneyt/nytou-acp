package io.nytou.web.acp.view;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.nytou.web.acp.path.Path;

import java.util.HashMap;
import java.util.Map;

import static io.nytou.web.acp.request.RequestUtil.getSessionCurrentUser;
import static io.nytou.web.acp.request.RequestUtil.getSessionLocale;

public class ViewUtil {

    public static Map<String, Object> baseModel(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("currentUser", getSessionCurrentUser(ctx));
        return model;
    }

    public static Handler notFound = ctx -> {
        ctx.render(Path.Template.NOT_FOUND, baseModel(ctx));
    };


}
