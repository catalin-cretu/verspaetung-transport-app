package com.github.catalin.cretu.verspaetung.web;

@SuppressWarnings({ "squid:S1214", "squid:S00115" })
public interface Paths {

    String Api = "/api";

    interface api {
        String Vehicles = Api + "/vehicles";

    }

    interface Params {
        String lineName = "lineName";
        String nextAtStop = "nextAtStop";

        String time = "time";
        String stopX = "stopX";
        String stopY = "stopY";
    }
}
