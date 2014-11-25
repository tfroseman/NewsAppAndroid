package com.newsappandroid;

import com.koushikdutta.ion.Ion;

/**
 * Created by Thomas on 11/24/2014.
 */
public class NetworkConnection {
    Ion.with(context).load("http://example.com/test.txt").setHeader("foo", "bar").asString().setCallback(toString());
}
