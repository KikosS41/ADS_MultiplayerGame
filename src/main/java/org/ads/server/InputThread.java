package org.ads.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
                String response = messageParser.parseMessage(message, this);
                if (!response.equals("")){
                    broadcast(response, this);
                    if (response.split(" ")[0].equals("JOIN")){
                        for (InputThread player : gameThread.players) {
                            sendMessage("JOIN " + player.player.name + " " + player.player.worldX + " " + player.player.worldY + " " + player.player.speed + " " + player.player.skin);
                        }
                    }
                    if (response.split(" ")[0].equals("QUIT")){
                        gameThread.players.remove(this);
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

    public static void broadcast(String message, InputThread excludePlayer) {
        for (InputThread player : gameThread.players) {
            player.sendMessage(message);
        }
    }

    public void update() {
        if (player != null) {
            player.update();
        }
    }
}
