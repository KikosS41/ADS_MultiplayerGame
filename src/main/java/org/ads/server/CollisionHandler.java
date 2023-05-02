package org.ads.server;

import java.util.ArrayList;

public class CollisionHandler {
    GameThread gameThread;

    public CollisionHandler(GameThread gameThread){
        this.gameThread = gameThread;
    }

    public void checkTile(Player entity) {
        for (int i = 0; i < gameThread.currentMap.layers.size(); i++) {
            int entityLeftWorldX = entity.worldX + entity.solidArea.x;
            int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
            int entityTopWorldY = entity.worldY + entity.solidArea.y;
            int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

            int entityLeftCol = entityLeftWorldX / gameThread.tileSize;
            int entityRightCol = entityRightWorldX / gameThread.tileSize;
            int entityTopRow = entityTopWorldY / gameThread.tileSize;
            int entityBottomRow = entityBottomWorldY / gameThread.tileSize;

            int tileNum1, tileNum2;

            switch (entity.direction) {
                case "UP" -> {
                    entityTopRow = (entityTopWorldY - entity.speed) / gameThread.tileSize;
                    tileNum1 = gameThread.currentMap.layers.get(i)[entityLeftCol][entityTopRow];
                    tileNum2 = gameThread.currentMap.layers.get(i)[entityRightCol][entityTopRow];
                    if ((tileNum1 != 0 && gameThread.currentMap.tiles[tileNum1].collision) || (tileNum2 != 0 && gameThread.currentMap.tiles[tileNum2].collision)) {
                        entity.collisionOn = true;
                    }
                }
                case "DOWN" -> {
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gameThread.tileSize;
                    tileNum1 = gameThread.currentMap.layers.get(i)[entityLeftCol][entityBottomRow];
                    tileNum2 = gameThread.currentMap.layers.get(i)[entityRightCol][entityBottomRow];
                    if ((tileNum1 != 0 && gameThread.currentMap.tiles[tileNum1].collision) || (tileNum2 != 0 && gameThread.currentMap.tiles[tileNum2].collision)) {
                        entity.collisionOn = true;
                    }
                }
                case "LEFT" -> {
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gameThread.tileSize;
                    tileNum1 = gameThread.currentMap.layers.get(i)[entityLeftCol][entityTopRow];
                    tileNum2 = gameThread.currentMap.layers.get(i)[entityLeftCol][entityBottomRow];
                    if ((tileNum1 != 0 && gameThread.currentMap.tiles[tileNum1].collision) || (tileNum2 != 0 && gameThread.currentMap.tiles[tileNum2].collision)) {
                        entity.collisionOn = true;
                    }
                }
                case "RIGHT" -> {
                    entityRightCol = (entityRightWorldX + entity.speed) / gameThread.tileSize;
                    tileNum1 = gameThread.currentMap.layers.get(i)[entityRightCol][entityTopRow];
                    tileNum2 = gameThread.currentMap.layers.get(i)[entityRightCol][entityBottomRow];
                    if ((tileNum1 != 0 && gameThread.currentMap.tiles[tileNum1].collision) || (tileNum2 != 0 && gameThread.currentMap.tiles[tileNum2].collision)) {
                        entity.collisionOn = true;
                    }
                }
            }
        }
    }

    public void checkPlayers(Player entity, ArrayList<InputThread> players) {
        for (InputThread player: players) {
            if (player.player !=entity && player.player != null) {
                entity.solidArea.x += entity.worldX;
                entity.solidArea.y += entity.worldY;

                player.player.solidArea.x += player.player.worldX;
                player.player.solidArea.y += player.player.worldY;

                switch (entity.direction) {
                    case "UP" -> {
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(player.player.solidArea)) {
                            entity.collisionOn = true;
                        }
                    }
                    case "DOWN" -> {
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(player.player.solidArea)) {
                            entity.collisionOn = true;
                        }
                    }
                    case "LEFT" -> {
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(player.player.solidArea)) {
                            entity.collisionOn = true;
                        }
                    }
                    case "RIGHT" -> {
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(player.player.solidArea)) {
                            entity.collisionOn = true;
                        }
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;

                player.player.solidArea.x = player.player.solidAreaDefaultX;
                player.player.solidArea.y = player.player.solidAreaDefaultY;
            }
        }
    }
}
