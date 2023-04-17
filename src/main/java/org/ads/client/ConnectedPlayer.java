package org.ads.client;

public class ConnectedPlayer extends Entity{
    public ConnectedPlayer(GamePanel gamePanel, String name, int worldX, int worldY){
        super(gamePanel);
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
    }

    public void update(int worldX, int worldY, int speed){
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
    }
}
