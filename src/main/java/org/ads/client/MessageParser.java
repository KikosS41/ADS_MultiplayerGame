package org.ads.client;

import org.ads.client.entities.ConnectedPlayer;
import org.ads.client.entities.Player;

import java.io.IOException;
import java.util.ArrayList;

public class MessageParser {

    String name, skin, direction, isMoving, status;
    int worldX, worldY, speed;

    public void parseMessage(String message, GamePanel gamePanel){
        String[] parts = message.split(" ");

        switch (parts[0]) {
            case "JOIN" -> {
                status = parts[1];
                if(status.equals("OK")){
                    // init local Player
                    worldX = Integer.parseInt(parts[2]);
                    worldY = Integer.parseInt(parts[3]);
                    speed = Integer.parseInt(parts[4]);
                    skin = parts[5];

                    gamePanel.player = new Player(name, worldX, worldY, speed, skin, gamePanel); // new Player ...
                } else {
                    // init connected player
                    name = parts[1];
                    if(!name.equals(gamePanel.name)){
                        worldX = Integer.parseInt(parts[2]);
                        worldY = Integer.parseInt(parts[3]);
                        speed = Integer.parseInt(parts[4]);
                        skin = parts[5];
                        gamePanel.connectedPlayers.add(new ConnectedPlayer(gamePanel,name,worldX,worldY,speed,"DOWN",skin));// new connected player ...
                    }
                }
            }
            case "UPDATE" -> {
                name = parts[1];
                worldX = Integer.parseInt(parts[2]);
                worldY = Integer.parseInt(parts[3]);
                direction = parts[4];
                isMoving = parts[5];
                gamePanel.updatePlayers(name, worldX, worldY, direction, isMoving);

            }
            case "QUIT" -> {
                name = parts[1];
                for (int i = 0 ; i< gamePanel.connectedPlayers.size(); i++){
                    if(gamePanel.connectedPlayers.get(i).name.equals(name)){
                        gamePanel.connectedPlayers.remove(i);
                        return;
                    }
                }
            }
            default -> {
                System.err.println("Erreur lors de la communication avec le client " + parts[1]);
                IOException e = new IOException();
                e.printStackTrace();
            }
        }
    }
}
