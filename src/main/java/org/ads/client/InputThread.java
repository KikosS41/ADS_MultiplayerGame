package org.ads.client;

import java.io.BufferedReader;
import java.io.IOException;

public class InputThread implements Runnable{
    private final BufferedReader input;
    private final GamePanel gamePanel;
    public InputThread(BufferedReader input, GamePanel gamePanel) {
        this.input = input;
        this.gamePanel = gamePanel;
    }
    @Override
    public void run() {
        String message;
        try {
            while ((message = input.readLine()) != null) {
                gamePanel.messageParser.parseMessage(message, gamePanel);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de l'entrée.");
        }
    }
}