/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.mep.commons.math.Interval;

/**
 * Implementation of Sudipto Guha and Andrew McGregor:
 * Stream order and order statistics: Quantile estimation in random-order
 * streams
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings("unused")
public class Median4PartialAggregate<R> implements IMedianPartialAggregate<R> {
    private final static double DELTA = 0.03;
    private final static double BETA = 1.5;
    private double aggregate;
    private int count;
    private int substream = 0;
    private Interval ab = Interval.MAX;
    private List<Double> S = new ArrayList<Double>();

    /**
 * 
 */
    public Median4PartialAggregate() {
        // TODO Auto-generated constructor stub
    }

    /**
 * 
 */
    public Median4PartialAggregate(double value) {
        add(value);
    }

    /**
 * 
 */
    public Median4PartialAggregate(Median4PartialAggregate<R> partialAggregate) {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void add(Double value) {
        count++;
        int i = substream;

        if (count == start(i)) {
            // S.clear();
        }
        S.add(value);
        if (count == end(i)) {
            int n = length(i);
            double k = n / 2.0;
            double gamma = gamma(delta(), n, k);
            double phi = phi(delta(), n, gamma);
            double l1 = l1(delta(), n, phi, gamma);
            double l2 = l2(delta(), n, phi, gamma, k);

            System.out.println("Gamma: " + gamma);
            System.out.println("phi: " + phi);
            System.out.println("l1: " + l1);
            System.out.println("l2: " + l2);
            System.out.println("n: " + n);

            List<Map<List<Double>, List<Double>>> partitions = partition(l1, l2, phi, S);
            for (int i2 = 0; i2 < phi; i2++) {
                Iterator<Entry<List<Double>, List<Double>>> iter = partitions.get(i2).entrySet().iterator();
                Entry<List<Double>, List<Double>> entry = iter.next();
                List<Double> Si = entry.getKey();
                List<Double> Ei = entry.getValue();
                // Is S=<S1,E1,...,Sp,Ep> a patition on the data stream? And if
                // so, are
                // the elements in S and E sorted?

                double u = sample(ab, new Interval(Si.get(0), Si.get(Si.size() - 1)));
                double rPrime = estimate(u, Ei, n, l2);
                ab = update(u, ab, rPrime, k, gamma);
            }
            substream++;
        }

        if (count == remember(i)) {
            this.aggregate = ab.inf() + (ab.sup() - ab.inf()) / 2.0;
        }
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getAggValue() {
        return aggregate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Median4PartialAggregate<R> clone() {
        return new Median4PartialAggregate<R>(this);
    }

    private double sample(Interval a, Interval S) {
        if (!S.intersects(a)) {
            return a.inf();
        }
        else {
            return S.intersection(a).inf();
        }
    }

    private double estimate(double u, List<Double> E, double n, double l2) {
        double r = rank(u, E);
        return (n - 1) * (r - 1) / l2 + 1;
    }

    private Interval update(double u, Interval a, double r, double k, double gamma) {
        if (r < k - gamma / 2.0) {
            return new Interval(u, a.sup());
        }
        else if (r > k + gamma / 2.0) {
            return new Interval(a.inf(), u);
        }
        else {
            return new Interval(u, u);
        }
    }

    private List<Map<List<Double>, List<Double>>> partition(double l1, double l2, double phi, List<Double> S) {
        List<Map<List<Double>, List<Double>>> partitions = new ArrayList<Map<List<Double>, List<Double>>>();
        for (int i = 0; i < phi * l1 + phi * l2; i++) {

            List<Double> Si = new ArrayList<Double>();
            for (int l = 0; l < l1; l++) {
                Si.add(S.get(i));
                i++;
            }

            List<Double> Ei = new ArrayList<Double>();
            for (int l = 0; l < l2; l++) {
                Si.add(S.get(i));
                i++;
            }
            Map<List<Double>, List<Double>> map = new HashMap<List<Double>, List<Double>>();
            map.put(Si, Ei);
            partitions.add(map);
        }
        return partitions;
    }

    private double rank(double x, List<Double> S) {
        double rank = 0;
        for (Double s : S) {
            if (s < x) {
                return rank + 1;
            }
            else {
                rank++;
            }
        }
        return rank + 1;
    }

    private double phi(double delta, double n, double gamma) {
        return 4.0 * (FastMath.log(4.0 / 3.0, n / gamma) + FastMath.pow(FastMath.log(3.0 / delta) * FastMath.log(4.0 / 3.0, n / gamma), 1.0 / 2.0));
    }

    private double l1(double delta, double n, double phi, double gamma) {
        return n * FastMath.pow(gamma, -1.0) * FastMath.log(3 * FastMath.pow(n, 2.0) * phi / delta);
    }

    private double l2(double delta, double n, double phi, double gamma, double k) {
        return 2.0 * (n - 1) * FastMath.pow(gamma, -1.0) * FastMath.sqrt((k + gamma) * FastMath.log(6 * n * phi / delta));
    }

    private double gamma(double delta, double n, double k) {
        return 20.0 * FastMath.pow(FastMath.log(n), 2.0) * Math.log(FastMath.pow(delta, -1.0)) * FastMath.sqrt(k);
    }

    private double delta() {
        return DELTA;
    }

    private double beta() {
        return BETA;
    }

    private int start(int i) {
        return (int) FastMath.ceil(FastMath.pow(beta(), i));
    }

    private int end(int i) {
        return (int) FastMath.floor(4.0 * FastMath.pow(beta(), i));
    }

    private int length(int i) {
        return (int) (FastMath.floor(4.0 * FastMath.pow(beta(), i)) - FastMath.ceil(FastMath.pow(beta(), i)) + 1);
    }

    private int remember(int i) {
        return (int) (2 * (FastMath.floor(4.0 * FastMath.pow(beta(), i)) - FastMath.ceil(FastMath.pow(beta(), i)) + 1));
    }

    public static void main(final String[] args) {
        final Median4PartialAggregate<?> agg = new Median4PartialAggregate<>(1.0);
        for (int i = 0; i < 1000; i++) {
            agg.add(3.0);
            agg.add(2.0);
            agg.add(0.0);
            agg.add(5.0);
            agg.add(4.0);
        }
        assert (agg.getAggValue() == 2.5);
        System.out.println(agg);
    }
}
