package org.ads.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String host;
        if (args.length > 0) {
            host = args[0];
        } else {
            host = "localhost";
        }

        String name = "Player" + (int) (Math.random() * 1000);
        GamePanel gamePanel = new GamePanel(host, name,16,12);
        gamePanel.setupGame();
        gamePanel.startGame();
    }
}