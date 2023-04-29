package org.ads.server;

import org.ads.statistics.ResourceMonitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        // Start stats thread
        ResourceMonitor resourceMonitor = new ResourceMonitor("stats/server.csv");
        Thread resourceMonitorThread = new Thread(resourceMonitor);
        resourceMonitorThread.start();

        //Start game thread
        GameThread gameThread = new GameThread(8888);
        Thread.ofVirtual().start(gameThread);
    }
}