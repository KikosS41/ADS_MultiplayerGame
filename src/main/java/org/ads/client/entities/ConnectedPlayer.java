package org.ads.client.entities;

import org.ads.client.GamePanel;

import java.util.Objects;

public class ConnectedPlayer extends Entity {
    public ConnectedPlayer(GamePanel gamePanel, String name, int worldX, int worldY, int speed, String direction,String skin){
        super(gamePanel, skin);
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.direction = direction;
        this.skin = skin;
        this.isMoving = "NO";
    }

    public void update(int worldX, int worldY, int speed, String direction, String isMoving){
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.direction = direction;
        this.isMoving = isMoving;
    }
}
