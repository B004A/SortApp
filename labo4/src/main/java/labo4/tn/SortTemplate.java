package labo4.tn;

import javafx.application.Platform;

public abstract class SortTemplate implements Runnable {
    protected int startIndex;
    protected int endIndex;
    protected int[] arrayToSort;
    protected App sortApp;
    protected volatile boolean running = true;
    protected volatile boolean pause = false;
    protected boolean isSorted = false;
    protected int sleepTime;

    public SortTemplate(App sortApp) {
        this.sortApp = sortApp;
    }

    public void sortArray() {
        sort(arrayToSort, startIndex, endIndex);
        stopAlgorithm();
    }

    public void sort(int[] array, int start, int fin) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (start >= fin) {
            return;
        }
        int splitPoint = findSplitPoint(array, start, fin);
        sort(array, start, splitPoint);
        sort(array, splitPoint, fin);
    }

    public void setupAlgorithm(int[] array, int startIndex, int endIndex) {
        this.arrayToSort = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.running = true;
    }

    public int findSplitPoint(int[] array, int start, int fin) {
        return 0;
    }

    public void stopAlgorithm() {
        running = false;
    }

    public void pauseAlgorithm() {
        pause = true;
    }

    public synchronized void resumeAlgorithm() {
        pause = false;
        notify();
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    protected void sleepThread(int sleepTime, int[] array) {
        try {
            Thread.sleep(sleepTime);
            checkForPause();
            Platform.runLater(() -> sortApp.updateContent(array));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void checkForPause() {
        if (pause) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public boolean isPaused() {
        return this.pause;
    }

    @Override
    public void run() {
        while (running) {
            if (!pause) {
                sortArray();
            }
            if (pause) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
