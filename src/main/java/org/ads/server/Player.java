package org.ads.server;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    public String name, skin, direction, isMoving;
    public int worldX, worldY, speed;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    GameThread gameThread;

    boolean collisionOn;

    public Player(String name, GameThread gameThread) {
        this.name = name;
        this.worldX = 0;
        this.worldY = 0;
        this.direction = "DOWN";
        this.speed = 4;
        this.isMoving = "NO";

        int rdmSkin = (int) (Math.random() * 1000);
        if (rdmSkin < 333){
            skin = "boy";
        } else if(rdmSkin < 666){
            skin = "oldman";
        } else {
            skin = "girl";
        }

        this.gameThread = gameThread;
    }

    public void update(){

        if (isMoving.equals("YES")) {
            collisionOn = false;
            gameThread.collisionHandler.checkTile(this);
            gameThread.collisionHandler.checkPlayers(this, gameThread.getPlayers());
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

    public void setSpawn() {
        collisionOn=true;
        String[] allDirections = new String[]{"LEFT","RIGHT","UP","DOWN"};
        while (collisionOn){
            collisionOn = false;
            for (String direction : allDirections){
                this.direction = direction;
                gameThread.collisionHandler.checkTile(this);
                gameThread.collisionHandler.checkPlayers(this, gameThread.getPlayers());
            }
            if(collisionOn){
                worldX+= gameThread.tileSize;
                if (worldX >= gameThread.tileSize * (gameThread.maxWorldCol - 1)){
                    worldX = 0;
                    worldY += gameThread.tileSize;
                }
            }
            if(worldY >= gameThread.tileSize * gameThread.maxWorldRow){
                break;
            }
        }
    }
}