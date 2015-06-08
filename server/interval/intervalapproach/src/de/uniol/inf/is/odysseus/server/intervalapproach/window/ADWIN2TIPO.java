/**
 * 
 */
package de.uniol.inf.is.odysseus.server.intervalapproach.window;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * Implementation of the ADWIN2 adaptive window algorithm based on:
 * 
 * Learning from Time-Changing Data with Adaptive Windowing, Albert Bifet and
 * Ricard Gavalda, 2006
 * 
 * @author Christian Kuka <christian@kuka.cc>
 *         FIXME  20140319 christian@kuka.cc Implement ADWIN2 (CKu)
 */
@SuppressWarnings("unused")
public class ADWIN2TIPO<T extends Tuple<ITimeInterval>> extends AbstractPipe<T, T> {
    private static final int MAXBUCKETS = 10;

    private final List<T> queue = new LinkedList<T>();
    /** The confidence. */
    private final double delta;
    /** The position of the attribute. */
    private final int pos;

    private int width;
    private double variance;
    private int total;

    public ADWIN2TIPO(final int pos, final double delta) {
        this.pos = pos;
        this.delta = delta;
        this.width = 0;
        this.variance = 0.0;
        this.total = 0;
    }

    public ADWIN2TIPO(final ADWIN2TIPO<T> operator) {
        this.pos = operator.pos;
        this.delta = operator.delta;
        this.width = operator.width;
        this.variance = operator.variance;
        this.total = operator.total;
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    @Override
    public void process_open() throws OpenFailedException {
        super.process_open();
    }

    @Override
    protected synchronized void process_next(final T object, final int port) {
        final Double value = ((Number) object.getAttribute(this.pos)).doubleValue();
        if (this.insert(value)) {
            this.produceData(object.getMetadata().getStart());
        }
        else {
            this.queue.add(object);
        }
        throw new RuntimeException("Not implemented yet!");
    }

    private void produceData(final PointInTime endTimestamp) {
        PointInTime start = null;
        if (this.queue.size() > 0) {
            start = this.queue.get(0).getMetadata().getStart();
        }
        while (!this.queue.isEmpty()) {
            final T toTransfer = this.queue.remove(0);
            if (start != null) {
                toTransfer.getMetadata().setStart(start);
            }
            toTransfer.getMetadata().setEnd(endTimestamp);
            this.transfer(toTransfer);
        }
    }

    /**
     * Gets the value of the confidence.
     * 
     * @return the delta
     */
    public double getDelta() {
        return this.delta;
    }

    @Override
    public void processPunctuation(final IPunctuation punctuation, final int port) {
        throw new RuntimeException("Not implemented yet!");
    }

    private void setInput() {
    }

    private void insertElement(double value) {
        // create a new bucket b with content e and capacity 1

        // add e to the head of W

        // update WIDTH, VARIANCE and TOTAL
        this.variance += variance(value);
        this.width++;
        this.total += value;
        // Compress buckets
        compressBuckets();

    }

    private void deleteElement() {
    }

    private void compressBuckets() {
    }

    private boolean insert(final double value) {
        return false;
    }

    private void compress() {

    }

    private double variance(double value) {
        if (width > 0) {
            return Math.pow((value - total / width), 2.0) / (width - 1);
        }
        else {
            return 0.0;
        }
    }

    /**
     * Calculates the harmonic mean of window 0 and window 1
     * 
     * @param n0
     *            The length of window 0
     * @param n1
     *            The length of window 1
     * @return The harmonic mean
     */
    private double harmonicMean(final double n0, final double n1) {
        return 1.0 / ((1.0 / n0) + (1.0 / n1));
    }

    /**
     * Estimate the value epsilon_cut for a partition window0 and window1 of a
     * window.
     * 
     * Eq. 3.1
     * 
     * @param m
     *            The harmonic mean
     * @param sigmaSquare
     *            The observed variance of the elements in the window
     * @param deltaPrime
     *            The confidence'
     * @return The value for epsilon_cut
     */
    private double epsilonCut(final double m, final double sigmaSquare, final double deltaPrime) {
        return Math.sqrt((2.0 / (m)) * sigmaSquare * Math.log(2.0 / deltaPrime)) + ((2.0 / (3.0 * m)) * Math.log(2.0 / deltaPrime));
    }

    /**
     * Estimate delta' to protect from the multiple hypothesis testing.
     * 
     * @param n
     *            The window length
     * @param delta
     *            The desired confidence
     * @return The value for delta'
     */
    private double deltaPrime(final double n, final double delta) {
        return delta / Math.log(n);
    }

    /**
     * Estimate the size of a bucket.
     * 
     * @param i
     * @return The size of the bucket
     */
    private double bucketSize(final int i) {
        return Math.pow(2, i);
    }

}
