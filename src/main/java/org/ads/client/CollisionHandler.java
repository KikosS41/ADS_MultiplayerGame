package org.ads.client;

import org.ads.client.entities.ConnectedPlayer;
import org.ads.client.entities.Entity;

import java.util.ArrayList;

public class CollisionHandler {
    GamePanel gamePanel;

    public CollisionHandler(GamePanel _gamePanel){
        gamePanel = _gamePanel;
    }

    public void checkTile(Entity entity) {
        for (int i = 0; i < gamePanel.currentMap.layers.size(); i++) {
            int entityLeftWorldX = entity.worldX + entity.solidArea.x;
            int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
            int entityTopWorldY = entity.worldY + entity.solidArea.y;
            int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

            int entityLeftCol = entityLeftWorldX / gamePanel.tileSize;
            int entityRightCol = entityRightWorldX / gamePanel.tileSize;
            int entityTopRow = entityTopWorldY / gamePanel.tileSize;
            int entityBottomRow = entityBottomWorldY / gamePanel.tileSize;

            int tileNum1, tileNum2;

            switch (entity.direction) {
                case "UP" -> {
                    entityTopRow = (entityTopWorldY - entity.speed) / gamePanel.tileSize;
                    tileNum1 = gamePanel.currentMap.layers.get(i)[entityLeftCol][entityTopRow];
                    tileNum2 = gamePanel.currentMap.layers.get(i)[entityRightCol][entityTopRow];
                    if ((tileNum1 != 0 && gamePanel.currentMap.tiles[tileNum1].collision) || (tileNum2 != 0 && gamePanel.currentMap.tiles[tileNum2].collision)) {
                        entity.collisionOn = true;
                    }
                }
                case "DOWN" -> {
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gamePanel.tileSize;
                    tileNum1 = gamePanel.currentMap.layers.get(i)[entityLeftCol][entityBottomRow];
                    tileNum2 = gamePanel.currentMap.layers.get(i)[entityRightCol][entityBottomRow];
                    if ((tileNum1 != 0 && gamePanel.currentMap.tiles[tileNum1].collision) || (tileNum2 != 0 && gamePanel.currentMap.tiles[tileNum2].collision)) {
                        entity.collisionOn = true;
                    }
                }
                case "LEFT" -> {
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gamePanel.tileSize;
                    tileNum1 = gamePanel.currentMap.layers.get(i)[entityLeftCol][entityTopRow];
                    tileNum2 = gamePanel.currentMap.layers.get(i)[entityLeftCol][entityBottomRow];
                    if ((tileNum1 != 0 && gamePanel.currentMap.tiles[tileNum1].collision) || (tileNum2 != 0 && gamePanel.currentMap.tiles[tileNum2].collision)) {
                        entity.collisionOn = true;
                    }
                }
                case "RIGHT" -> {
                    entityRightCol = (entityRightWorldX + entity.speed) / gamePanel.tileSize;
                    tileNum1 = gamePanel.currentMap.layers.get(i)[entityRightCol][entityTopRow];
                    tileNum2 = gamePanel.currentMap.layers.get(i)[entityRightCol][entityBottomRow];
                    if ((tileNum1 != 0 && gamePanel.currentMap.tiles[tileNum1].collision) || (tileNum2 != 0 && gamePanel.currentMap.tiles[tileNum2].collision)) {
                        entity.collisionOn = true;
                    }
                }
            }
        }
    }

    public void checkConnectedPlayers(Entity entity, ArrayList<ConnectedPlayer> connectedPlayers) {
        for (ConnectedPlayer connectedPlayer: connectedPlayers) {
            entity.solidArea.x += entity.worldX;
            entity.solidArea.y += entity.worldY;

            connectedPlayer.solidArea.x += connectedPlayer.worldX;
            connectedPlayer.solidArea.y += connectedPlayer.worldY;

            switch (entity.direction) {
                case "UP" -> {
                    entity.solidArea.y -= entity.speed;
                    if (entity.solidArea.intersects(connectedPlayer.solidArea)) {
                        entity.collisionOn = true;
                    }
                }
                case "DOWN" -> {
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(connectedPlayer.solidArea)) {
                        entity.collisionOn = true;
                    }
                }
                case "LEFT" -> {
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(connectedPlayer.solidArea)) {
                        entity.collisionOn = true;
                    }
                }
                case "RIGHT" -> {
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(connectedPlayer.solidArea)) {
                        entity.collisionOn = true;
                    }
                }
            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;

            connectedPlayer.solidArea.x = connectedPlayer.solidAreaDefaultX;
            connectedPlayer.solidArea.y = connectedPlayer.solidAreaDefaultY;
        }
    }
}
