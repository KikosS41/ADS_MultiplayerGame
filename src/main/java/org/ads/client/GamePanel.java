package org.ads.client;

import org.ads.client.map_parser.MapParser;
import org.ads.client.entities.ConnectedPlayer;
import org.ads.client.entities.Player;
import org.ads.statistics.ResourceMonitor;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class GamePanel implements Runnable {
    public CollisionHandler collisionHandler ;
    // Screen Settings
    int originalTileSize;
    int scale = 3;
    public int tileSize;
    int maxScreenCol;
    int maxScreenRow;
    public int screenWidth;
    public int screenHeight;
    KeyHandler keyHandler;

    // Map parser
    MapParser currentMap = new MapParser("/maps/map.json", this); // /maps/map.json

    // World settings (initialised at current.getMapInfo(this) in constructor)
    public int maxWorldCol;
    public int maxWorldRow;
    public int worldWidth;
    public int worldHeight;

    // Entities
    public String name;

    public Player player; // Initialised in constructor
    ArrayList<ConnectedPlayer> connectedPlayers = new ArrayList<>();

    // Thread for game loop
    Thread gameThread;

    // Game loop
    int FPS = 60;
    BufferedReader input;
    public PrintWriter output;

    // Client-server part
    MessageParser messageParser = new MessageParser();
    long timestampSent, timestampReceived;

    public GamePanel(String name, int maxScreenCol, int maxScreenRow) throws IOException {
        this.name = name;
        this.maxScreenCol = maxScreenCol;
        this.maxScreenRow = maxScreenRow;

        ArrayList<Integer> parameters = currentMap.getMapInfo();

        maxWorldCol = parameters.get(0);
        maxWorldRow = parameters.get(1);
        originalTileSize = parameters.get(2);
        tileSize = originalTileSize * scale;
        worldWidth = tileSize * maxWorldCol;
        worldHeight = tileSize * maxWorldRow;
        screenWidth = tileSize * this.maxScreenCol;
        screenHeight = tileSize * this.maxScreenRow;

        currentMap.loadMap();
        collisionHandler = new CollisionHandler(this);

        player = new Player(name, 20,20,0,"boy",this);
    }
    public void setupGame() {
        try {
            // Communication
            String HOST = "localhost";
            int PORT = 8888;
            Socket socket = new Socket(HOST, PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("JOIN " + name);
            InputThread inputThread = new InputThread(input, this);
            Thread.ofVirtual().start(inputThread);
        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur.");
            System.exit(1);
        }
    }
    public void startGame() throws IOException {
        keyHandler = new KeyHandler(output);

        // Start Stats Generator
        ResourceMonitor resourceMonitor = new ResourceMonitor("stats/" + name + ".csv");
        Thread resourceMonitorThread = new Thread(resourceMonitor);
        resourceMonitorThread.start();

        // Start Game Thread
        gameThread = new Thread(this);
        gameThread.start();

        // Start Rendering Thread
        RenderingThread renderingThread = new RenderingThread(this);

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                close();
            }
        });

        window.setResizable(false);
        window.setTitle("Client : " + name);

        window.add(renderingThread);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        Thread.ofVirtual().start(renderingThread);

    }
    @Override
    public void run() {
        double drawInterval = 1000000000.0 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
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
        player.updateAnimation();
        player.predict();
        for (ConnectedPlayer connectedPlayer:connectedPlayers) {
            connectedPlayer.updateAnimation();
            connectedPlayer.predict();
        }
    }
    public void close(){
        output.println("QUIT");
        output.close();
    }
    public ArrayList<ConnectedPlayer> getConnectedPlayers() {
        return new ArrayList<>(connectedPlayers);
    }

    public Player getPlayer() {
        return new Player(this ,player);
    }

    public void updatePlayers(String name, int worldX, int worldY, String direction, String isMoving) {
        if (this.name.equals(name)){
            player.update(worldX, worldY, direction, isMoving);
        } else {
            for (ConnectedPlayer connectedPlayer: getConnectedPlayers()) {
                connectedPlayer.update(worldX,worldY,direction,isMoving);
            }
        }
    }
}