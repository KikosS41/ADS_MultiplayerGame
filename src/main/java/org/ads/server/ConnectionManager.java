package org.ads.server;

import java.io.IOException;
import java.net.Socket;

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
                gameThread.players.add(inputThread); // a changer ?
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
