package org.ads.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerUpdateThread implements Runnable {
    private final GamePanel gamePanel;

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    long timestampTotal;

    public ServerUpdateThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        boolean shouldRun = true;
        while (shouldRun) {
            LocalDateTime now = LocalDateTime.now();
            if(gamePanel.timestampReceived != 0 && gamePanel.timestampSent!= 0){
                timestampTotal = gamePanel.timestampReceived - gamePanel.timestampSent;
            }
            // Write the timestampTotal to a CSV file with headers and a time stamp
            try {
                FileWriter writer = new FileWriter("stats/" + gamePanel.player.name+ "_ping.csv", true); // true indicates that the file is opened in append mode
                File file = new File("stats/" + gamePanel.player.name+ "_ping.csv");
                if (file.length() == 0) { // add headers if file is empty
                    writer.write("Date Time,Response Time\n");
                }
                writer.write(now.format(DATE_TIME_FORMATTER) + "," + timestampTotal + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gamePanel.timestampSent = System.currentTimeMillis();
            gamePanel.output.println("UPDATE " + gamePanel.player.name + " " + gamePanel.player.worldX + " " + gamePanel.player.worldY + " " + gamePanel.player.speed + " " + gamePanel.player.direction + " " + gamePanel.player.isMoving);
            try {
                Thread.sleep(100); // wait 1 second before sending the next update
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt(); // restore interrupted status
                shouldRun = false;
            }
        }
    }
}
