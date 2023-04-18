package org.ads.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int PORT = 8888;
    private static final List<PlayerHandler> players = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Serveur lancé sur le port " + PORT);

        while (true) {
            Socket playerSocket = serverSocket.accept();
            System.out.println("Nouvelle connexion entrante : " + playerSocket);
            PlayerHandler playerHandler = new PlayerHandler(playerSocket);
            players.add(playerHandler);
            playerHandler.start();
        }
    }

    public static void broadcast(String message, PlayerHandler excludePlayer) {
        for (PlayerHandler player : players) {
            if (player != excludePlayer) {
                // Send positions of all connected clients
                for (PlayerHandler otherPlayer : players) {
                    if (otherPlayer != player) {
                        player.sendMessage(message);
                    }
                }
            }
        }
    }

    public static List<PlayerHandler> getPlayers() {
        return players;
    }

}
