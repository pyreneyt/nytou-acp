package io.nytou.web.acp.path;

public class Path {

    public static class Web {
        public static final String INDEX = "/";
        public static final String LOGIN = "/login";
        public static final String LOGOUT = "/logout";
    }


    public static class Template {
        public static final String INDEX = "velocity/index.vm";
        public static final String DASHBOARD = "velocity/dashboard.vm";
        public static final String NOT_FOUND = "velocity/error/not-found.vm";
    }

}
