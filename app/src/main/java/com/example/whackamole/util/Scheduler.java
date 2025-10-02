package com.example.whackamole.util;

public interface Scheduler {
    void postDelayed(Runnable r, long delayMs);

    void removeCallbacks(Runnable r);

    void removeCallbacksAndMessages(Runnable r);
}
