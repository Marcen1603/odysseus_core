package de.uniol.inf.is.odysseus.server.nosql.base.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Erstellt von RoBeaT
 * Date: 15.12.2014
 */
public class BatchSizeTimer<T>{

    Timer timer;
    Task task;
    List<T> elements;
    BatchSizeTimerTask<T> batchSizeTimerTask;
    int maxCount;
    long period;

    public BatchSizeTimer(BatchSizeTimerTask<T> batchSizeTimerTask, int maxCount, int period) {

        if(maxCount <= 0){
            throw new IllegalArgumentException("maxCount must be > 0");
        }

        if(period <= 0){
            throw new IllegalArgumentException("period must be > 0");
        }

        this.batchSizeTimerTask = batchSizeTimerTask;
        this.maxCount = maxCount;
        this.period = period;

        elements = new ArrayList<>();

        task = new Task();
        timer = new Timer(period, task);
        timer.start();
    }

    public synchronized void add(T element) {
        elements.add(element);

        if(elements.size() == maxCount){
            ring();
        }
    }

    private synchronized void ring() {
        batchSizeTimerTask.onTimerRings(elements);   // give list to physical operator
        elements = new ArrayList<>();  // new list for next iteration
        timer.restart();
    }

    private class Task implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ring();
        }
    }
}
