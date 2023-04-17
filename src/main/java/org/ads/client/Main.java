package org.ads.client;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String name;
        if (args[0] != ""){
            name = args[0];
        } else {
             name = "Joueur" + (int) (Math.random() * 1000);
        }
        new GamePanel(name);
        JFrame window = new JFrame();
    }
}