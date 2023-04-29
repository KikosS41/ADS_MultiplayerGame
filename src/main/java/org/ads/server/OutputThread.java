package org.ads.server;


public class OutputThread implements Runnable{
    boolean isRunning = true;
    GameThread gameThread;
    public OutputThread(GameThread gameThread){
        this.gameThread = gameThread;
    }
    @Override
    public void run() {
        while (isRunning) {
            for (InputThread player :gameThread.players) {
                for (InputThread playerToSend :gameThread.players) {
                    player.sendMessage("UPDATE " + playerToSend.player.name+ " " + playerToSend.player.worldX + " " + playerToSend.player.worldY + " " + playerToSend.player.direction + " " + playerToSend.player.isMoving);
                }
            }
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
