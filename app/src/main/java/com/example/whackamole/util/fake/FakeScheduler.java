package com.example.whackamole.util.fake;

import com.example.whackamole.util.Scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * FakeScheduler allows controlling "time" in tests.
 * You can manually run scheduled Runnables instead of relying on real time.
 */
public class FakeScheduler implements Scheduler {

    private final List<Runnable> tasks = new ArrayList<>();

    @Override
    public void postDelayed(Runnable r, long delayMs) {
        // Ignore delay, just queue the task
        tasks.add(r);
    }

    @Override
    public void removeCallbacks(Runnable r) {
        // Remove all occurrences of the Runnable
        tasks.removeIf(task -> task == r); // identity check
    }

    @Override
    public void removeCallbacksAndMessages(Runnable r) {
        tasks.clear();
    }

    /**
     * Runs the next scheduled task if any.
     */
    public void runNext() {
        if (!tasks.isEmpty()) {
            Runnable task = tasks.remove(0); // remove first task (FIFO)
            task.run();
        }
    }

    /**
     * Runs all scheduled tasks in order.
     */
    public void runAll() {
        while (!tasks.isEmpty()) {
            runNext();
        }
    }

    /**
     * Returns how many tasks are pending.
     */
    public int pendingTasks() {
        return tasks.size();
    }
}
