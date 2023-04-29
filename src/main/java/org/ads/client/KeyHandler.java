package org.ads.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;

public class KeyHandler implements KeyListener {

    boolean keyAlreadyPressed = false;
    PrintWriter output;

    public KeyHandler(PrintWriter output) {
        this.output = output;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!keyAlreadyPressed){
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                output.println("EVENT LEFT YES");
                keyAlreadyPressed = true;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                output.println("EVENT RIGHT YES");
                keyAlreadyPressed = true;
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                output.println("EVENT UP YES");
                keyAlreadyPressed = true;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                output.println("EVENT DOWN YES");
                keyAlreadyPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            output.println("EVENT LEFT NO");
            keyAlreadyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            output.println("EVENT RIGHT NO");
            keyAlreadyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            output.println("EVENT UP NO");
            keyAlreadyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            output.println("EVENT DOWN NO");
            keyAlreadyPressed = false;
        }
    }
}
