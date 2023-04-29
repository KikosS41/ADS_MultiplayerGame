package org.ads.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class GameThread implements Runnable{
    int FPS = 60;
    ServerSocket serverSocket;
    List<InputThread> players = new ArrayList<>();
    public GameThread(int port) throws IOException {
        // Init server
        serverSocket = new ServerSocket(port);
        System.out.println("Serveur lancé sur le port " + port);

        //Start ConnectionManager thread
        ConnectionManager connectionManager = new ConnectionManager(this);
        Thread.ofVirtual().start(connectionManager);

        //Start output thread
        OutputThread outputThread = new OutputThread(this);
        Thread.ofVirtual().start(outputThread);
    }
    @Override
    public void run() {
        double drawInterval = 1000000000.0 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (true) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                delta--;
            }
        }
    }
    private void update() {
        for ( InputThread player: players) {
            if(player != null){
                player.update();
            }
        }
    }
}
