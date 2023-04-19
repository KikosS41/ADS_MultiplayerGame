package org.ads.client;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String name, skin;
        if (args.length > 0) {
            name = args[0];
        } else {
             name = "Joueur" + (int) (Math.random() * 1000);
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
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                gamePanel.close();
            }
        });

        window.setResizable(false);
        window.setTitle("Client : " + name);

        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGame();
    }
}