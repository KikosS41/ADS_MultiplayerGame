package org.ads.client;

import org.ads.client.entities.ConnectedPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MessageParser {
    public ArrayList<ConnectedPlayer> parseMessage(String message, GamePanel gamePanel){

        ArrayList<ConnectedPlayer> ConnectedPlayersCopy = gamePanel.getConnectedPlayers();

        String[] parts = message.split(" ");

        switch (parts[0]) {
            case "JOIN" -> {
                ConnectedPlayer playerToAdd = new ConnectedPlayer(gamePanel, parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), parts[5], parts[6]);
                for (ConnectedPlayer connectedPlayer : ConnectedPlayersCopy) {
                    if (Objects.equals(connectedPlayer.name, playerToAdd.name)) {
                        return ConnectedPlayersCopy;
                    }
                }
                ConnectedPlayersCopy.add(playerToAdd);
                gamePanel.output.println("JOIN " + gamePanel.player.name + " " + gamePanel.player.worldX + " " + gamePanel.player.worldY + " " + gamePanel.player.speed + " " + gamePanel.player.direction + " " + gamePanel.player.skin);
                System.out.println(playerToAdd.name + " a rejoint la partie.");
            }
            case "UPDATE" -> {
                if (parts[1].equals("OK")) {
                    gamePanel.timestampReceived = System.currentTimeMillis();
                    return ConnectedPlayersCopy;
                }
                for (ConnectedPlayer connectedPlayer : ConnectedPlayersCopy) {
                    if (connectedPlayer.name.equals(parts[1])) {
                        connectedPlayer.update(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), parts[5], parts[6]);
                        break;
                    }
                }
            }
            case "QUIT" -> {
                int i = 0;
                for (ConnectedPlayer connectedPlayer : ConnectedPlayersCopy) {
                    if (connectedPlayer.name.equals(parts[1])) {
                        System.out.println(connectedPlayer.name + " a disparu.");
                        ConnectedPlayersCopy.remove(i);
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
        return ConnectedPlayersCopy;
    }
}
