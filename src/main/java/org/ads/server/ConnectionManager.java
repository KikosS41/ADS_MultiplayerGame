package org.ads.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionManager implements Runnable{
    boolean isRunning = true;
    GameThread gameThread;
    public ConnectionManager(GameThread gameThread){
        this.gameThread = gameThread;
    }
    @Override
    public void run() {
        while (isRunning) {
            Socket playerSocket;
            try {
                playerSocket = gameThread.serverSocket.accept();
                System.out.println("Nouvelle connexion entrante : " + playerSocket);
                InputThread inputThread = new InputThread(playerSocket, gameThread);
                ArrayList<InputThread> player = gameThread.getPlayers();
                player.add(inputThread);
                gameThread.setPlayers(player);
                inputThread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public  void stop() {
        isRunning = false;
    }
}
