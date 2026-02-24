import org.example.WorkerThreadTask;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.*;



    public static void main(String[] args) throws Exception {
        int numThreads;
        long durationMs;
        long workload;

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of threads : ");
        numThreads = scanner.nextInt();

        System.out.print("Enter duration (seconds): ");
        durationMs = scanner.nextLong() * 1000;

        System.out.print("Enter workload %% (1-100): ");
        workload = scanner.nextLong();


        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Long>> futures = new ArrayList<>();

        long wallStart = System.currentTimeMillis();

        // Submit all worker tasks
        for (int i = 1; i <= numThreads; i++) {
            futures.add(executor.submit(new WorkerThreadTask.WorkerTask(i, durationMs, workload)));
        }

        // Collect results
        long totalIterations = 0;
        for (int i = 0; i < futures.size(); i++) {
            totalIterations += futures.get(i).get(); // blocks until done
        }

        executor.shutdown();
        long elapsed = System.currentTimeMillis() - wallStart;

        // Report
        System.out.println("\n========= REPORT =========");
        System.out.printf("Threads run      : %d%n", numThreads);
        System.out.printf("Duration (target): %d s%n", durationMs / 1000);
        System.out.printf("Elapsed (actual) : %.2f s%n", elapsed / 1000.0);
        System.out.printf("Total iterations : %,d%n", totalIterations);
        System.out.printf("Avg per thread   : %,d%n", totalIterations / numThreads);
        System.out.println("==========================");
    }
