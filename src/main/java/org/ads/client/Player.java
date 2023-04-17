package org.ads.client;

public class Player extends Entity{
    KeyHandler keyHandler;
    public int screenX;
    public int screenY;

    public Player(GamePanel gamePanel, KeyHandler keyHandler, String name) {
        super(gamePanel);
        this.name = name;
        this.keyHandler = keyHandler;
        screenX = gamePanel.screenWidth/2 - gamePanel.tileSize/2;
        screenY = gamePanel.screenHeight/2 - gamePanel.tileSize/2;
        worldX = gamePanel.tileSize * 8;
        worldY = gamePanel.tileSize * 8;
        speed = 4;
    }

    public void update(){
        if(keyHandler.upPressed) {
            worldY -= speed;
        }
        else if(keyHandler.downPressed) {
            worldY += speed;
        }
        else if(keyHandler.leftPressed) {
            worldX -= speed;
        }
        else if(keyHandler.rightPressed) {
            worldX += speed;
        }
    }
}
