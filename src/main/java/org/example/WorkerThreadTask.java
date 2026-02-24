package org.example;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.*;


public class WorkerThreadTask {

    // Each worker increments a counter as fast as it can
    public static class WorkerTask implements Callable<Long> {
        private final int threadId;
        private final double durationMs;
        private final double workloadPercent;


        public WorkerTask(int threadId, double durationMs, double workload) {
            this.threadId = threadId;
            this.durationMs = durationMs;
            this.workloadPercent = workload;
        }

        @Override
        public Long call() throws Exception {
            long cycleMs = 100;
            long busyMs  = (long)(cycleMs * (workloadPercent / 100.0));
            long sleepMs = cycleMs - busyMs;
            long count = 0;
            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start < durationMs) {
                // --- BUSY phase: spin hard ---
                long busyEnd = System.currentTimeMillis() + busyMs;
                while (System.currentTimeMillis() < busyEnd) {
                    count++;
                }

                // --- IDLE phase: sleep ---
                if (sleepMs > 0) {
                    Thread.sleep(sleepMs);
                }
            }

            System.out.printf("[Thread %d] Finished — iterations: %,d%n", threadId, count);
            return count;
        }
    }
}