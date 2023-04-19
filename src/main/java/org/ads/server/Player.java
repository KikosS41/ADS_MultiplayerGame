package org.ads.server;

public class Player {
    public String name, skin, direction, isMoving;
    public int worldX, worldY, speed;

    public Player(String name, int worldX, int worldY, int speed, String direction, String skin) {
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.direction = direction;
        this.skin = skin;
    }

    public void update(int worldX, int worldY, int speed, String direction, String isMoving){
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.direction = direction;
        this.isMoving = isMoving;
    }
}