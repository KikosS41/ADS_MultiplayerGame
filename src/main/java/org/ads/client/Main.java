package org.ads.client;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
             name = "Joueur" + (int) (Math.random() * 1000);
        }
        GamePanel gamePanel = new GamePanel(name, 16,12);
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