package org.ads.server;

public class Player {
    public String name, skin, direction, isMoving;
    public int worldX, worldY, speed;

    public Player(String name) {
        this.name = name;
        this.worldX = 640;
        this.worldY = 640;
        this.speed = 4;
        this.direction = "DOWN";
        this.skin = "boy";
        this.isMoving = "NO";
    }

    public void update(){
        // Need to add collision system
        if (isMoving.equals("YES")) {
            switch (direction) {
                case "UP" -> worldY -= speed;
                case "DOWN" -> worldY += speed;
                case "LEFT" -> worldX -= speed;
                case "RIGHT" -> worldX += speed;
            }
        }
    }

    public void changeStatus(String direction, String isMoving) {
        this.direction = direction;
        this.isMoving = isMoving;
    }
}