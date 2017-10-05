package com.example.test.omadresampleapp;

import android.app.Application;

/**
 * Base Application
 */
public class App extends Application {

    private static App instance;

    /**
     * set context to instance
     */
    public App() {
        instance = this;
    }

    /**
     * call from anywhere returns application context
     *
     * @return Application context
     */
    public static App get() {
        return instance;
    }

}
