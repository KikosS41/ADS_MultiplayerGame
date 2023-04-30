package org.ads.server;

import org.ads.server.map_parser.MapParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class GameThread implements Runnable{

    // Map parser
    MapParser currentMap = new MapParser("/maps/map.json", this);
    // World settings (initialised at current.getMapInfo(this) in constructor)
    public int maxWorldCol;
    public int maxWorldRow;
    public int worldWidth;
    public int worldHeight;
    int originalTileSize;
    int scale = 3;
    public int tileSize;
    int FPS = 60;
    public CollisionHandler collisionHandler;
    ServerSocket serverSocket;
    ArrayList<InputThread> players = new ArrayList<>();

    boolean isRunning;
    public GameThread(int port) throws IOException {

        setupGame();
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

    public void setupGame() throws IOException {
        ArrayList<Integer> parameters = currentMap.getMapInfo();

        maxWorldCol = parameters.get(0);
        maxWorldRow = parameters.get(1);
        originalTileSize = parameters.get(2);
        tileSize = originalTileSize * scale;
        worldWidth = tileSize * maxWorldCol;
        worldHeight = tileSize * maxWorldRow;

        currentMap.loadMap();
        collisionHandler = new CollisionHandler(this);

        isRunning = true;
    }
    @Override
    public void run() {
        double drawInterval = 1000000000.0 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (isRunning) {
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
