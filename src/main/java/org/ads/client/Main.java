package org.ads.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            name = "Player" + (int) (Math.random() * 1000);
        }

        GamePanel gamePanel = new GamePanel(name,16,12);
        gamePanel.setupGame();
        gamePanel.startGame();
    }
}