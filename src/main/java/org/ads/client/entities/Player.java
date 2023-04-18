package org.ads.client.entities;

import org.ads.client.GamePanel;
import org.ads.client.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    KeyHandler keyHandler;
    public int screenX;
    public int screenY;

    public Player(GamePanel gamePanel, KeyHandler keyHandler, String name) {
        super(gamePanel);
        this.name = name;
        this.keyHandler = keyHandler;
        screenX = gamePanel.screenWidth/2 - gamePanel.tileSize/2;
        screenY = gamePanel.screenHeight/2 - gamePanel.tileSize/2;

        worldX = gamePanel.tileSize * 8;
        worldY = gamePanel.tileSize * 20;
        speed = 4;


    }

    public void update(){
        if(keyHandler.upPressed) {
            worldY -= speed;
        }
        else if(keyHandler.downPressed) {
            worldY += speed;
        }
        else if(keyHandler.leftPressed) {
            worldX -= speed;
        }
        else if(keyHandler.rightPressed) {
            worldX += speed;
        }
    }

    public void draw(Graphics2D graphics2D) {
        BufferedImage image = setup("/player/boy_down_1.png");

        int x = screenX;
        int y = screenY;

        if (screenX > worldX) {
            x = worldX;
        }
        if (screenY > worldY) {
            y = worldY;
        }
        int rightOffset = gamePanel.screenWidth - screenX;
        if (rightOffset > gamePanel.worldWidth - worldX) {
            x = gamePanel.screenWidth - (gamePanel.worldWidth - worldX);
        }
        int bottomOffset = gamePanel.screenHeight - screenY;
        if (bottomOffset > gamePanel.worldHeight - worldY) {
            y = gamePanel.screenHeight - (gamePanel.worldWidth - worldY);
        }

        graphics2D.drawImage(image, x, y, null);
    }
}
