package org.ads.server;

import java.awt.*;

public class Player {
    public String name, skin, direction, isMoving;
    public int worldX, worldY, speed;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    GameThread gameThread;

    boolean collisionOn;

    public Player(String name, GameThread gameThread) {
        this.name = name;
        this.worldX = 1280;
        this.worldY = 1280;
        this.speed = 4;
        this.direction = "DOWN";
        this.skin = "boy";
        this.isMoving = "NO";
        this.gameThread = gameThread;
    }

    public void update(){

        if (isMoving.equals("YES")) {
            collisionOn = false;
            gameThread.collisionHandler.checkTile(this);
            gameThread.collisionHandler.checkPlayers(this, gameThread.players);
            if (!collisionOn){
                switch (direction) {
                    case "UP" -> worldY -= speed;
                    case "DOWN" -> worldY += speed;
                    case "LEFT" -> worldX -= speed;
                    case "RIGHT" -> worldX += speed;
                }
            }
        }
    }

    public void changeStatus(String direction, String isMoving) {
        this.direction = direction;
        this.isMoving = isMoving;
    }
}