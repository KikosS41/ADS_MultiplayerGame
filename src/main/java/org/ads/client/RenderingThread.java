package org.ads.client;

import org.ads.client.entities.ConnectedPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderingThread extends JPanel implements Runnable {
    private final GamePanel gamePanel;

    public RenderingThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        initWindow();
    }

    public void initWindow(){
        this.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(gamePanel.keyHandler);
        this.setFocusable(true);
    }
    @Override
    public void run() {
        double drawInterval = 1000000000.0 /gamePanel.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gamePanel.gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                repaint();
                delta--;
            }
            repaint();
        }
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;

        // Draw map
        gamePanel.currentMap.draw(graphics2D);
        // Draw player
        gamePanel.player.draw(graphics2D);

        // faire une copie de la liste connectedPlayers
        ArrayList<ConnectedPlayer> connectedPlayersCopy = new ArrayList<>(gamePanel.getConnectedPlayers());
        // itérer sur la copie de la liste
        if (connectedPlayersCopy.size() > 0){
            for (ConnectedPlayer connectedPlayer: connectedPlayersCopy) {
                connectedPlayer.draw(graphics2D);
            }
        }
        graphics2D.dispose();
    }
}
