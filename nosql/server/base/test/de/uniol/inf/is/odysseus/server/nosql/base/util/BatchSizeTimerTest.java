package de.uniol.inf.is.odysseus.server.nosql.base.util;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BatchSizeTimerTest {

    public static final int MAX_TIMEOUT = Integer.MAX_VALUE;
    BatchSizeTimerTaskForTest<String> task;
    private BatchSizeTimer<String> batchSizeTimer;

    @Before
    public void beforeTest(){
        task = new BatchSizeTimerTaskForTest<>();
    }

    @Test
    public void testMaxLengthReached(){

        batchSizeTimer = new BatchSizeTimer<>(task, 3, MAX_TIMEOUT);

        batchSizeTimer.add("erste");
        batchSizeTimer.add("zweite");
        batchSizeTimer.add("dritte");
        batchSizeTimer.add("vierte");

        List<String> firstReceivedElements = task.getFirstReceivedElements();

        List<String> expectedList = new ArrayList<>();

        expectedList.add("erste");
        expectedList.add("zweite");
        expectedList.add("dritte");

        assertEquals(expectedList, firstReceivedElements);
    }

    @Test
    public void testTimeout() throws InterruptedException {

        int waitMillis = 2000;
        batchSizeTimer = new BatchSizeTimer<>(task, 4, waitMillis);

        batchSizeTimer.add("erste");
        batchSizeTimer.add("zweite");
        batchSizeTimer.add("dritte");

        Thread.sleep(waitMillis + 500);

        List<String> firstReceivedElements = task.getFirstReceivedElements();
        List<String> expectedList = new ArrayList<>();

        expectedList.add("erste");
        expectedList.add("zweite");
        expectedList.add("dritte");

        assertEquals(expectedList, firstReceivedElements);
    }

    @Test
    public void testNothingAdded() throws Exception {

        int waitMillis = 2000;
        batchSizeTimer = new BatchSizeTimer<>(task, 4, waitMillis);

        Thread.sleep(waitMillis + 500);

        List<String> firstReceivedElements = task.getFirstReceivedElements();

        ArrayList<String> emptyList = new ArrayList<>();

        assertEquals(emptyList, firstReceivedElements);
    }

    @Test
    public void testStress() throws Exception {

        int bufferSize = 8;
        int countElementsToAdd = 123698745;

        batchSizeTimer = new BatchSizeTimer<>(task, bufferSize, MAX_TIMEOUT);

        for (int i = 0 ; i < countElementsToAdd ; i++) {
            batchSizeTimer.add("it's just a string");
        }

        int ringCounter = task.getRingCounter();
        int expectedCounter = countElementsToAdd / bufferSize;
        assertEquals(expectedCounter, ringCounter);
    }

    @Test
    public void testHandlingWithNull() throws Exception {

        batchSizeTimer = new BatchSizeTimer<>(task, 3, MAX_TIMEOUT);

        batchSizeTimer.add(null);
        batchSizeTimer.add(null);
        batchSizeTimer.add(null);
        batchSizeTimer.add(null);

        List<String> firstReceivedElements = task.getFirstReceivedElements();

        List<String> expectedList = new ArrayList<>();

        expectedList.add(null);
        expectedList.add(null);
        expectedList.add(null);

        assertEquals(expectedList, firstReceivedElements);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalPeriodTime(){

        batchSizeTimer = new BatchSizeTimer<>(task, 10, -123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalBufferSize() throws Exception {

        batchSizeTimer = new BatchSizeTimer<>(task, -10, 123);
    }

    @Test
    public void testVariety() throws Exception {

        int waitMillis = 2000;
        batchSizeTimer = new BatchSizeTimer<>(task, 4, waitMillis);

        // wait at first when time is expired
        batchSizeTimer.add("erste");
        batchSizeTimer.add("zweite");
        batchSizeTimer.add("dritte");

        Thread.sleep(waitMillis + 500);

        List<String> firstReceivedElements = task.getFirstReceivedElements();
        List<String> expectedList = new ArrayList<>();

        expectedList.add("erste");
        expectedList.add("zweite");
        expectedList.add("dritte");

        assertEquals(expectedList, firstReceivedElements);


        // fill the buffer
        batchSizeTimer.add("1");
        batchSizeTimer.add("2");
        batchSizeTimer.add("3");
        batchSizeTimer.add("4");

        List<String> lastReceivedElements = task.getLastReceivedElements();
        expectedList.clear();
        expectedList.add("1");
        expectedList.add("2");
        expectedList.add("3");
        expectedList.add("4");

        assertEquals(expectedList, lastReceivedElements);

        // fill the buffer again
        batchSizeTimer.add("11");
        batchSizeTimer.add("22");
        batchSizeTimer.add("33");
        batchSizeTimer.add("44");

        lastReceivedElements = task.getLastReceivedElements();
        expectedList.clear();
        expectedList.add("11");
        expectedList.add("22");
        expectedList.add("33");
        expectedList.add("44");

        assertEquals(expectedList, lastReceivedElements);

        // timer should be restarted now
        // wait again when time is expired
        batchSizeTimer.add("hi");
        batchSizeTimer.add("hey");
        batchSizeTimer.add("hy");

        Thread.sleep(waitMillis + 500);

        lastReceivedElements = task.getLastReceivedElements();
        expectedList.clear();

        expectedList.add("hi");
        expectedList.add("hey");
        expectedList.add("hy");

        assertEquals(expectedList, lastReceivedElements);
    }

    private class BatchSizeTimerTaskForTest<T> implements BatchSizeTimerTask<T>{

        List<T> firstReceivedElements;
        List<T> lastReceivedElements;
        int ringCounter = 0;

        @Override
        public void onTimerRings(List<T> elements) {
            if(firstReceivedElements == null){
                firstReceivedElements = elements;
            }

            lastReceivedElements = elements;
            ringCounter++;
        }

        public int getRingCounter() {
            return ringCounter;
        }

        public List<T> getLastReceivedElements() {
            return lastReceivedElements;
        }

        public List<T> getFirstReceivedElements() {
            return firstReceivedElements;
        }
    }
}
