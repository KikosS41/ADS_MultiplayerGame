package org.ads.client.entities;

import org.ads.client.GamePanel;

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

    public void predict(){
        if(isMoving.equals("YES")){
            collisionOn = false;
            gamePanel.collisionHandler.checkTile(this);

            gamePanel.collisionHandler.checkConnectedPlayers(this, gamePanel.getConnectedPlayers());
            gamePanel.collisionHandler.checkPlayer(this, gamePanel.getPlayer());

            moveEntity();
        }
    }
}
