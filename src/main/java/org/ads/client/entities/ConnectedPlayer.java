package org.ads.client.entities;

import org.ads.client.GamePanel;

import java.util.ArrayList;

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
    public void updateByPrediction(ArrayList<ConnectedPlayer> connectedPlayersCopy){
        connectedPlayersCopy.remove(this);
        if(isMoving.equals("YES")){
            //CHECK TILE COLLISION
            collisionOn = false;
            gamePanel.collisionHandler.checkTile(this);

            //CHECK ENTITY COLLISION
            gamePanel.collisionHandler.checkConnectedPlayers(this, connectedPlayersCopy);

            //IF COLLISION IS FALSE PLAYER CAN MOVE
            if (!collisionOn) {
                switch (direction) {
                    case "UP" -> worldY -= speed;
                    case "DOWN" -> worldY += speed;
                    case "LEFT" -> worldX -= speed;
                    case "RIGHT" -> worldX += speed;
                }
            }
        }
    }
}
