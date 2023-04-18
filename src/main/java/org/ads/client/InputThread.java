package org.ads.client;

import org.ads.client.entities.ConnectedPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputThread implements Runnable{
    private BufferedReader input;
    private GamePanel gamePanel;

    public InputThread(BufferedReader input, GamePanel gamePanel) {
        this.input = input;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = input.readLine()) != null) {
                ArrayList<ConnectedPlayer> connectedPlayers = gamePanel.messageParser.parseMessage(message, gamePanel);
                gamePanel.setConnectedPlayers(connectedPlayers);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de l'entrée.");
        }
    }
}
