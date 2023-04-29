package org.ads.client.entities;

import org.ads.client.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends Entity{
    public int screenX;
    public int screenY;

    public Player(GamePanel gamePanel, Player player){
        super(gamePanel, player.skin);

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        this.worldX = player.worldX;
        this.worldY = player.worldY;

        isMoving = "NO";
    }

    public Player(String name, int worldX, int worldY, int speed, String skin, GamePanel gamePanel) {
        super(gamePanel, skin);
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.skin = skin;
        this.direction = "DOWN";
        isMoving = "NO";

        screenX = gamePanel.screenWidth/2 - gamePanel.tileSize/2;
        screenY = gamePanel.screenHeight/2 - gamePanel.tileSize/2;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void draw(Graphics2D graphics2D) {
        BufferedImage image = null;
        switch (direction) {
            case "DOWN" -> {
                if (spriteNumber == 1) {
                    image = down1;
                } else if (spriteNumber == 2) {
                    image = down2;
                }
            }
            case "LEFT" -> {
                if (spriteNumber == 1) {
                    image = left1;
                } else if (spriteNumber == 2) {
                    image = left2;
                }
            }
            case "RIGHT" -> {
                if (spriteNumber == 1) {
                    image = right1;
                } else if (spriteNumber == 2) {
                    image = right2;
                }
            }
            case "UP" -> {
                if (spriteNumber == 1) {
                    image = up1;
                } else if (spriteNumber == 2) {
                    image = up2;
                }
            }
            default -> {
            }
        }

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
