package org.ads.client.entities;

import org.ads.client.GamePanel;
import org.ads.client.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public abstract class Entity {
    public String skin;
    public int solidAreaDefaultX, solidAreaDefaultY;
    GamePanel gamePanel;


    public String name, direction, isMoving;
    public int worldX, worldY, speed;
    protected int spriteCounter;
    public int spriteNumber = 1;
    public boolean collisionOn;
    public Rectangle solidArea = new Rectangle(0,0,48,48);

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public Entity(GamePanel gamePanel, String skin){
        this.gamePanel = gamePanel;
        isMoving = "NO";
        getImages(skin);
    }

    public void draw(Graphics2D graphics2D){

        if(Objects.equals(isMoving, "YES")){
            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNumber == 1) {
                    spriteNumber = 2;
                } else if (spriteNumber == 2) {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        }

        BufferedImage image = null;

        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        if (worldX + gamePanel.tileSize> gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

            switch(direction) {
                case "DOWN":
                    if(spriteNumber == 1) {
                        image = down1;
                    }
                    else if(spriteNumber == 2) {
                        image = down2;
                    }
                    break;
                case "LEFT":
                    if(spriteNumber == 1) {
                        image = left1;
                    }
                    else if(spriteNumber == 2) {
                        image = left2;
                    }
                    break;
                case "RIGHT":
                    if(spriteNumber == 1) {
                        image = right1;
                    }
                    else if(spriteNumber == 2) {
                        image = right2;
                    }
                    break;
                case "UP":
                    if(spriteNumber == 1) {
                        image = up1;
                    }
                    else if(spriteNumber == 2) {
                        image = up2;
                    }
                    break;
                default:
                    break;
            }

            graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }
    }

    private void getImages(String skin){
        up1 = setup("/"+skin+"/up_1.png");
        up2 = setup("/"+skin+"/up_2.png");
        down1 = setup("/"+skin+"/down_1.png");
        down2 = setup("/"+skin+"/down_2.png");
        right1 = setup("/"+skin+"/right_1.png");
        right2 = setup("/"+skin+"/right_2.png");
        left1 = setup("/"+skin+"/left_1.png");
        left2 = setup("/"+skin+"/left_2.png");
    }

    private BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            image =  uTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
