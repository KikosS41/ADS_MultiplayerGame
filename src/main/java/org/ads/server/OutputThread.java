package org.ads.server;


import java.util.ArrayList;

public class OutputThread implements Runnable{
    boolean isRunning = true;
    GameThread gameThread;
    public OutputThread(GameThread gameThread){
        this.gameThread = gameThread;
    }
    @Override
    public void run() {
        while (isRunning) {
            ArrayList<InputThread> players = gameThread.getPlayers();
            for (InputThread player : players) {
                for (InputThread playerToSend : players) {
                    if (player != null && playerToSend.player != null){
                        player.sendMessage("UPDATE " + playerToSend.player.name+ " " + playerToSend.player.worldX + " " + playerToSend.player.worldY + " " + playerToSend.player.direction + " " + playerToSend.player.isMoving);
                    }
                }
            }
            /*if (players.size()>= 3) {
                for (InputThread player : players) {
                    System.out.println(player.player.name+ " " + player.player.worldX + " " + player.player.worldY);
                }
            }*/

            try {
                Thread.sleep(50); // wait 0.1 second before sending the next update
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt(); // restore interrupted status
                isRunning = false;
            }
        }
    }
    public  void stop() {
        isRunning = false;
    }
}
