package de.uniol.inf.is.odysseus.server.nosql.base.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *  The BatchSizeTimer helps NoSQL sinks to collect objects which will be saved in a NoSQL database.
 *  The BatchSizeTimer ist used in AbstractNoSQLSinkPO
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

    /**
     * adds an element to the list of elements.
     * if the list of elements reached the maxCount ring() will be called
     *
     * @param element which will be added to the list of elements
     */
    public synchronized void add(T element) {
        elements.add(element);

        if(elements.size() == maxCount){
            ring();
        }
    }

    /**
     *  If max batch size is reached or batch time (period) is expired, this method will be called.
     *  If batch time (period) is expired and no elements are in the list, the batchSizeTimerTask will not be called
     */
    public synchronized void ring() {

        if(!elements.isEmpty()) {
            batchSizeTimerTask.onTimerRings(elements);
            elements = new ArrayList<>();
        }
        timer.restart();
    }

    private class Task implements ActionListener {

        /**
         *  actionPerformed will be called when the time of period is expired
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ring();
        }
    }
}
