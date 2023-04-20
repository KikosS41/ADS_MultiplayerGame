package org.ads.statistics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResourceMonitor implements Runnable {
    private static final int MONITORING_INTERVAL_SECONDS = 1;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String[] CSV_HEADER = {"Date Time", "Memory Usage", "Heap Memory Usage", "Non-Heap Memory Usage", "Thread Count", "Peak Thread Count", "Total Started Thread Count"};

    private final MemoryMXBean memoryMxBean;
    private final ThreadMXBean threadMxBean;

    private boolean isRunning;
    private final FileWriter writer;

    public ResourceMonitor(String fileName) throws IOException {
        memoryMxBean = ManagementFactory.getMemoryMXBean();
        threadMxBean = ManagementFactory.getThreadMXBean();

        isRunning = true;
        createDirectoryIfNotExists("stats");
        writer = new FileWriter(fileName);
        // Écriture de l'entête
        writer.write(String.join(",", CSV_HEADER) + "\n");
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                // Récupération du temps courant
                LocalDateTime now = LocalDateTime.now();
                // Get the Java runtime
                Runtime runtime = Runtime.getRuntime();
                // Run the garbage collector
                runtime.gc();

                // Récupération des informations sur le système d'exploitation
                // Calculate the used memory
                String systemCpuLoad = String.valueOf(runtime.totalMemory() - runtime.freeMemory());


                // Récupération des informations sur la mémoire
                String heapMemoryUsage = Long.toString(memoryMxBean.getHeapMemoryUsage().getUsed());
                String nonHeapMemoryUsage = Long.toString(memoryMxBean.getNonHeapMemoryUsage().getUsed());

                // Récupération des informations sur les threads
                String threadCount = Integer.toString(threadMxBean.getThreadCount());
                String peakThreadCount = Integer.toString(threadMxBean.getPeakThreadCount());
                String totalStartedThreadCount = Long.toString(threadMxBean.getTotalStartedThreadCount());

                // Écriture des informations dans le fichier CSV
                String[] row = {now.format(DATE_TIME_FORMATTER), systemCpuLoad, heapMemoryUsage, nonHeapMemoryUsage, threadCount, peakThreadCount, totalStartedThreadCount};
                writer.write(String.join(",", row) + "\n");

                // Attente de l'intervalle de temps
                Thread.sleep(MONITORING_INTERVAL_SECONDS * 1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                stop();
            }
        }

        // Fermeture du fichier
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                System.err.println("Failed to create directory: " + directoryPath);
            }
        }
    }
}
