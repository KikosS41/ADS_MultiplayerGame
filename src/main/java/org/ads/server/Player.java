package org.ads.server;

public class Player {
    public String name;
    public int worldX, worldY, speed;
    public Player(String name, int worldX, int worldY, int speed) {
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
    }

    public void update(int worldX, int worldY, int speed){
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
    }
}
