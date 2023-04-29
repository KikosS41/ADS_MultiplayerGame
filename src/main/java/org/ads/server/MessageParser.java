package org.ads.server;

import java.io.IOException;

public class MessageParser {
    String action,name,direction,isMoving;
    public String parseMessage(String message, InputThread inputThread){
        String[] parts =  message.split(" ");
        action = parts[0];
        switch (action){
            case "JOIN" ->{
                name = parts[1];
                inputThread.player = new Player(name); // Add new player
                System.out.println(name + " s'est connecté.");
                inputThread.output.println("JOIN OK " + inputThread.player.worldX + " " + inputThread.player.worldY + " " + inputThread.player.speed + " " + inputThread.player.skin);
                return "JOIN " + inputThread.player.name + " " + inputThread.player.worldX + " " + inputThread.player.worldY + " " + inputThread.player.speed + " " + inputThread.player.skin;
            }
            case "EVENT" -> {
                direction = parts[1];
                isMoving = parts[2];
                System.out.println(message);
                inputThread.player.changeStatus(direction, isMoving); // Change player status
            }
            case "QUIT" -> {
                // Disconnect the player how send the message
                System.out.println(name + " s'est déconnecté.");
                return "QUIT "+ inputThread.player.name;
            }
            default -> {
                System.err.println("Erreur lors de la communication avec le client " + parts[1]);
                IOException e = new IOException();
                e.printStackTrace();
            }
        }
        return "";
    }
}
