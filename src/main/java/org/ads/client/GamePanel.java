package org.ads.client;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    // Screen Settings
    int originalTileSize = 16;
    int scale = 3;
    int tileSize = originalTileSize * scale;
    int maxScreenCol = 16;
    int maxScreenRow = 12;
    int screenWidth = tileSize * maxScreenCol;
    int screenHeight = tileSize * maxScreenRow;
    KeyHandler keyHandler = new KeyHandler();

    // Entities
    Player player;
    ArrayList<ConnectedPlayer> connectedPlayers;

    // Thread for game loop
    Thread gameThread;

    // Game loop
    int FPS = 60;
    // Communication
    private String HOST = "localhost";
    private int PORT = 8888;
    BufferedReader input;
    public PrintWriter output;
    MessageParser messageParser = new MessageParser();
    public GamePanel(String name){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        try {
            Socket socket = new Socket(HOST, PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("JOIN " + player.name + " " + player.worldX + " " + player.worldY + " " +player.speed);
        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur.");
            System.exit(1);
        }
    }

    public void setupGame(){

    }

    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        String message;
        try {
            while (gameThread != null && (message = input.readLine()) != null) {
                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;
                    connectedPlayers = messageParser.parseMessage(message, this);
                if (delta >= 1) {
                    update();
                    output.println("UPDATE " + player.name + " " + player.worldX + " " + player.worldY + " " + player.speed);
                    repaint();
                    delta--;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur.");
            System.exit(1);
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

        for (ConnectedPlayer connectedPlayer: connectedPlayers) {
            connectedPlayer.draw(graphics2D);
        }
        graphics2D.dispose();
    }
}
