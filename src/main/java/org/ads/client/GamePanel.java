package org.ads.client;

import org.ads.client.map_parser.MapParser;
import org.ads.client.entities.ConnectedPlayer;
import org.ads.client.entities.Player;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
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
    ArrayList<ConnectedPlayer> connectedPlayers = new ArrayList<>();

    // Thread for game loop
    Thread gameThread;

    // Game loop
    int FPS = 60;
    BufferedReader input;
    public PrintWriter output;
    MessageParser messageParser = new MessageParser();
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

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        player = new Player(this, keyHandler, name, skin);

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

        currentMap.loadMap();
        collisionHandler = new CollisionHandler(this);

    }

    public void setupGame() {
    }

    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                output.println("UPDATE " + player.name + " " + player.worldX + " " + player.worldY + " " + player.speed + " " + player.direction + " " + player.isMoving);
                repaint();
                delta--;
            }
        }

    }

    private void update() {
        player.update();
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;

        // Draw map
        currentMap.draw(graphics2D);
        // Draw player
        player.draw(graphics2D);

        // Draw connected players
        if (connectedPlayers != null){
            for (ConnectedPlayer connectedPlayer: connectedPlayers) {
                connectedPlayer.draw(graphics2D);
            }
        }
        graphics2D.dispose();
    }
    public void close(){
        output.println("QUIT");
        output.close();
    }

    public void setConnectedPlayers(ArrayList<ConnectedPlayer> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    public ArrayList<ConnectedPlayer> getConnectedPlayers(){
        return connectedPlayers;
    }
}
