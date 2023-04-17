package org.ads.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MessageParser {
    public ArrayList<ConnectedPlayer> parseMessage(String message, GamePanel gamePanel){
        String[] parts = message.split(" ");

        switch (parts[0]) {
            case "JOIN" -> {
                ConnectedPlayer playerToAdd = new ConnectedPlayer(gamePanel, parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                for (ConnectedPlayer connectedPlayer : gamePanel.connectedPlayers) {
                    if (Objects.equals(connectedPlayer.name, playerToAdd.name)) {
                        return gamePanel.connectedPlayers;
                    }
                }
                gamePanel.connectedPlayers.add(playerToAdd);
                gamePanel.output.println("JOIN " + gamePanel.player.name + " " + gamePanel.player.worldX + " " + gamePanel.player.worldY + " " + gamePanel.player.speed);
                System.out.println(playerToAdd.name + " a rejoint la partie.");
            }
            case "UPDATE" -> {
                for (ConnectedPlayer connectedPlayer : gamePanel.connectedPlayers) {
                    if (connectedPlayer.name.equals(parts[1])) {
                        connectedPlayer.update(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
                        System.out.println(connectedPlayer.name + "est désormais en [" + connectedPlayer.worldX + ":" + connectedPlayer.worldY + "].");
                        break;
                    }
                }
            }
            case "QUIT" -> {
                int i = 0;
                for (ConnectedPlayer connectedPlayer : gamePanel.connectedPlayers) {
                    if (connectedPlayer.name.equals(parts[1])) {
                        System.out.println(connectedPlayer.name + " a disparu.");
                        gamePanel.connectedPlayers.remove(i);
                        break;
                    }
                    i++;
                }
            }
            default -> {
                System.err.println("Erreur lors de la communication avec le client " + parts[1]);
                IOException e = new IOException();
                e.printStackTrace();
            }
        }
        return gamePanel.connectedPlayers;
    }
}
