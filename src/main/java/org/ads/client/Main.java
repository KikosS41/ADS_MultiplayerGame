package org.ads.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String name, skin;
        if (args.length > 0) {
            name = args[0];
        } else {
            name = "Player" + (int) (Math.random() * 1000);
        }
        if (args.length > 1){
            skin = args[1];
        } else {
            int rdmSkin = (int) (Math.random() * 1000);
            if (rdmSkin < 333){
                skin = "boy";
            } else if(rdmSkin < 666){
                skin = "oldman";
            } else {
                skin = "girl";
            }
        }

        GamePanel gamePanel = new GamePanel(name, skin,16,12);
        gamePanel.setupGame();
        gamePanel.startGame();
    }
}