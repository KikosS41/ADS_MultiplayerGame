package org.ads.statistics;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResourceMonitor implements Runnable {
    private static final int MONITORING_INTERVAL_SECONDS = 2;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String[] CSV_HEADER = {"Date Time", "System CPU Load", "Process CPU Load", "Heap Memory Usage", "Non-Heap Memory Usage", "Thread Count", "Peak Thread Count", "Total Started Thread Count"};

    private OperatingSystemMXBean osMxBean;
    private MemoryMXBean memoryMxBean;
    private ThreadMXBean threadMxBean;

    private boolean isRunning;
    private FileWriter writer;

    public ResourceMonitor(String fileName) throws IOException {
        osMxBean = ManagementFactory.getOperatingSystemMXBean();
        memoryMxBean = ManagementFactory.getMemoryMXBean();
        threadMxBean = ManagementFactory.getThreadMXBean();

        isRunning = true;
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

                // Récupération des informations sur le système d'exploitation
                // Récupération de l'utilisation CPU système
                String systemCpuLoad = Double.toString(Runtime.getRuntime().availableProcessors());

                // Récupération de l'utilisation CPU du processus
                String processCpuLoad = "N/A";  // L'utilisation CPU du processus n'est pas disponible via la classe Runtime

                //String systemCpuLoad = Double.toString(osMxBean.getSystemCpuLoad());
                //String processCpuLoad = Double.toString(osMxBean.getProcessCpuLoad());

                // Récupération des informations sur la mémoire
                String heapMemoryUsage = Long.toString(memoryMxBean.getHeapMemoryUsage().getUsed());
                String nonHeapMemoryUsage = Long.toString(memoryMxBean.getNonHeapMemoryUsage().getUsed());

                // Récupération des informations sur les threads
                String threadCount = Integer.toString(threadMxBean.getThreadCount());
                String peakThreadCount = Integer.toString(threadMxBean.getPeakThreadCount());
                String totalStartedThreadCount = Long.toString(threadMxBean.getTotalStartedThreadCount());

                // Écriture des informations dans le fichier CSV
                String[] row = {now.format(DATE_TIME_FORMATTER), systemCpuLoad, processCpuLoad, heapMemoryUsage, nonHeapMemoryUsage, threadCount, peakThreadCount, totalStartedThreadCount};
                writer.write(String.join(",", row) + "\n");

                // Attente de l'intervalle de temps
                Thread.sleep(MONITORING_INTERVAL_SECONDS * 1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Fermeture du fichier
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
