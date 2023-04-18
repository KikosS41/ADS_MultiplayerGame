package org.ads.client.entities;

import org.ads.client.GamePanel;

public class ConnectedPlayer extends Entity {
    public ConnectedPlayer(GamePanel gamePanel, String name, int worldX, int worldY, int speed){
        super(gamePanel);
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
    }

    public void update(int worldX, int worldY, int speed){
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        System.out.println(name + " est en [" + worldX + "," + worldY + "].");
    }
}
