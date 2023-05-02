package org.ads.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class InputThread extends Thread {
    private final Socket playerSocket;
    private static GameThread gameThread;
    public PrintWriter output;
    public Player player;
    public InputThread(Socket playerSocket, GameThread gameThread) {
        this.playerSocket = playerSocket;
        InputThread.gameThread = gameThread;
    }

    public void run() {
        MessageParser messageParser = new MessageParser();
        try {
            output = new PrintWriter(playerSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

            String message;
            while ((message = input.readLine()) != null){
                String response = messageParser.parseMessage(message, this, gameThread);
                if (!response.equals("")){
                    broadcast(response);
                    if (response.split(" ")[0].equals("JOIN")){
                        for (InputThread player : gameThread.getPlayers()) {
                            sendMessage("JOIN " + player.player.name + " " + player.player.worldX + " " + player.player.worldY + " " + player.player.speed + " " + player.player.skin);
                        }
                    }
                    if (response.split(" ")[0].equals("QUIT")){
                        ArrayList<InputThread> players = gameThread.getPlayers();
                        players.remove(this);
                        gameThread.setPlayers(players);
                        this.interrupt();
                    }
                }
            }
            playerSocket.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la communication avec le client " + player.name);
            e.printStackTrace();
        }
    }
    public void sendMessage(String message) {
        output.println(message);
    }

    public static void broadcast(String message) {
        for (InputThread player : gameThread.getPlayers()) {
            player.sendMessage(message);
        }
    }

    public void update() {
        if (player != null) {
            player.update();
        }
    }
}
