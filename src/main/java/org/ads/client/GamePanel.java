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
    KeyHandler keyHandler = new KeyHandler();

    // Map parser
    MapParser currentMap = new MapParser("src/main/resources/maps/map.json", this); // /maps/map.json

    // World settings (initialised at current.getMapInfo(this) in constructor)
    public int maxWorldCol;
    public int maxWorldRow;
    public int worldWidth;
    public int worldHeight;

    // Entities
    public Player player; // Initialised in constructor
    private ArrayList<ConnectedPlayer> connectedPlayers = new ArrayList<>();

    // Thread for game loop
    Thread gameThread;

    // Game loop
    int FPS = 60;
    BufferedReader input;
    public PrintWriter output;

    // Client-server part
    MessageParser messageParser = new MessageParser();
    long timestampSent, timestampReceived;

    public GamePanel(String name, String skin, int maxScreenCol, int maxScreenRow) throws IOException {
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

        player = new Player(this, keyHandler, name, skin);

        currentMap.loadMap();
        collisionHandler = new CollisionHandler(this);
    }
    public void setupGame() {
        try {
            // Communication
            String HOST = "localhost";
            int PORT = 8888;
            Socket socket = new Socket(HOST, PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("JOIN " + player.name + " " + player.worldX + " " + player.worldY + " " +player.speed + " " + player.direction + " " +player.skin);
            InputThread inputThread = new InputThread(input, this);
            Thread inputThreadThread = new Thread(inputThread);
            inputThreadThread.start();
        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur.");
            System.exit(1);
        }
    }
    public void startGame() throws IOException {
        // Start Stats Generator
        ResourceMonitor resourceMonitor = new ResourceMonitor("stats/" + player.name + ".csv");
        Thread resourceMonitorThread = new Thread(resourceMonitor);
        resourceMonitorThread.start();

        // Start Game Thread
        gameThread = new Thread(this);
        gameThread.start();

        // Start message Sender Thread
        OutputThread outputThread = new OutputThread(this);
        new Thread(outputThread).start();

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
        window.setTitle("Client : " + player.name);

        window.add(renderingThread);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        new Thread(renderingThread).start();

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
        player.update();
        player.updateAnimation();

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
    public void setConnectedPlayers(ArrayList<ConnectedPlayer> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    public Player getPlayer() {
        return new Player(this ,player);
    }
}