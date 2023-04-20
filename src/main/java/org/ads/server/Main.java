package org.ads.server;

import org.ads.statistics.ResourceMonitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int PORT = 8888;
    private static final List<PlayerHandler> players = new ArrayList<>();
    static boolean isRunning = true;


    public static void main(String[] args) throws IOException {
        ResourceMonitor resourceMonitor = new ResourceMonitor("stats/server.csv");
        Thread resourceMonitorThread = new Thread(resourceMonitor);
        resourceMonitorThread.start();

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Serveur lancé sur le port " + PORT);

        while (isRunning) {
            Socket playerSocket = serverSocket.accept();
            System.out.println("Nouvelle connexion entrante : " + playerSocket);
            PlayerHandler playerHandler = new PlayerHandler(playerSocket);
            players.add(playerHandler);
            playerHandler.start();
        }
    }

    public static void broadcast(String message, PlayerHandler excludePlayer) {
        List<PlayerHandler> playersCopy = new ArrayList<>(players);
        for (PlayerHandler player : playersCopy) {
            if (player != excludePlayer) {
                player.sendMessage(message);
            }
        }
    }
    public static List<PlayerHandler> getPlayers() {
        return players;
    }
    public static void stop() {
        isRunning = false;
    }
}