package org.ads.client.entities;

import org.ads.client.GamePanel;
import org.ads.client.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Player extends Entity{
    KeyHandler keyHandler;
    public int screenX;
    public int screenY;

    public Player(GamePanel gamePanel, KeyHandler keyHandler, String name, String skin) {
        super(gamePanel, skin);
        this.keyHandler = keyHandler;
        this.name = name;
        this.skin= skin;

        screenX = gamePanel.screenWidth/2 - gamePanel.tileSize/2;
        screenY = gamePanel.screenHeight/2 - gamePanel.tileSize/2;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        worldX = gamePanel.tileSize * 20;
        worldY = gamePanel.tileSize * 20;
        speed = 4;
        direction = "DOWN";

    }

    public void update() {
        if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            isMoving = "YES";

            if (keyHandler.upPressed) {
                direction = "UP";
            } else if (keyHandler.downPressed) {
                direction = "DOWN";
            } else if (keyHandler.leftPressed) {
                direction = "LEFT";
            } else if (keyHandler.rightPressed) {
                direction = "RIGHT";
            }

            //CHECK TILE COLLISION
            collisionOn = false;
            gamePanel.collisionHandler.checkTile(this);

            //CHECK ENTITY COLLISION
            gamePanel.collisionHandler.checkConnectedPlayers(this, gamePanel.getConnectedPlayers());

            //IF COLLISION IS FALSE PLAYER CAN MOVE
            if (!collisionOn) {
                switch (direction) {
                    case "UP":
                        worldY -= speed;
                        break;
                    case "DOWN":
                        worldY += speed;
                        break;
                    case "LEFT":
                        worldX -= speed;
                        break;
                    case "RIGHT":
                        worldX += speed;
                        break;
                }
            }
        } else {
            isMoving = "NO";
        }
    }

    public void draw(Graphics2D graphics2D) {
        if(Objects.equals(isMoving, "YES")){
            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNumber == 1) {
                    spriteNumber = 2;
                } else if (spriteNumber == 2) {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        }

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
