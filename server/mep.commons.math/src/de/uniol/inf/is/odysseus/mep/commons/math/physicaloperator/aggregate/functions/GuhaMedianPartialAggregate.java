/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.mep.commons.math.Interval;

/**
 * Implementation of Sudipto Guha and Andrew McGregor:
 * Stream order and order statistics: Quantile estimation in random-order
 * streams
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 *         FIXME: Not working yet!!
 */
@SuppressWarnings("unused")
public class GuhaMedianPartialAggregate<R> {

    // McGregor stuff
    private final static double DELTA = 0.95;
    private final static double BETA = 1.5;
    private double aggregate;
    private int count;
    private int substream = 0;

    private Queue<Instance> queue = new LinkedList<>();
    private int instatiation = 0;

    /**
 * 
 */
    public GuhaMedianPartialAggregate() {
        // TODO Auto-generated constructor stub
    }

    /**
 * 
 */
    public GuhaMedianPartialAggregate(double value) {
        add(value);
    }

    /**
 * 
 */
    public GuhaMedianPartialAggregate(GuhaMedianPartialAggregate<R> partialAggregate) {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */

    public GuhaMedianPartialAggregate<R> add(Double value) {
        // int i = instatiation;
        // if (count == start(i)) {
        // Instance instance = new Instance(length(i), end(i));
        // System.out.println("Create new Instance, ending at: " +
        // instance.end);
        // queue.add(instance);
        // instatiation++;
        // }
        // Instance instance = queue.peek();
        // instance.add(value);
        // if (count == instance.end) {
        // queue.poll();
        // System.out.println("Result of " + count);
        // System.out.println(instance.getAggValue());
        //
        // }
        // // S.clear();
        // }
        // S.add(value);
        // if (count == end(i)) {
        // int n = length(i);
        // double k = n / 2.0;
        // double upsilon = upsilon(delta(), n, k);
        // double phi = phi(delta(), n, upsilon);
        // double l1 = l1(delta(), n, phi, upsilon);
        // double l2 = l2(delta(), n, phi, upsilon, k);
        //
        // System.out.println("Upsilon: " + upsilon);
        // System.out.println("phi: " + phi);
        // System.out.println("l1: " + l1);
        // System.out.println("l2: " + l2);
        // System.out.println("n: " + n);
        //
        // List<Map<List<Double>, List<Double>>> partitions = partition(l1, l2,
        // phi, S);
        // for (int i2 = 0; i2 < phi; i2++) {
        // Iterator<Entry<List<Double>, List<Double>>> iter =
        // partitions.get(i2).entrySet().iterator();
        // Entry<List<Double>, List<Double>> entry = iter.next();
        // List<Double> Si = entry.getKey();
        // List<Double> Ei = entry.getValue();
        // // Is S=<S1,E1,...,Sp,Ep> a patition on the data stream? And if
        // // so, are
        // // the elements in S and E sorted?
        //
        // double u = sample(ab, new Interval(Si.get(0), Si.get(Si.size() -
        // 1)));
        // double rPrime = estimate(u, Ei, n, l2);
        // ab = update(u, ab, rPrime, k, gamma);
        // }
        // substream++;
        // }
        //
        // if (count == remember(i)) {
        // this.aggregate = ab.inf() + (ab.sup() - ab.inf()) / 2.0;
        // }
        // TODO Auto-generated method stub
        return this;
    }

    /**
     * {@inheritDoc}
     */

    public Double getAggValue() {
        return aggregate;
    }

    /**
     * {@inheritDoc}
     */

    public void clear() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuhaMedianPartialAggregate<R> clone() {
        return new GuhaMedianPartialAggregate<R>(this);
    }

    // McGregor Stuff
    private double sample(Interval ab, Collection<Double> S) {
        int elements = 0;
        double min = Double.MAX_VALUE;
        for (Double s : S) {
            if ((ab.inf() < s) && (s < ab.sup())) {
                elements++;
                if (s < min) {
                    min = s;
                }
            }
        }
        if (elements == 0) {
            return ab.inf();
        }
        else {
            return min;
        }
    }

    private double estimate(double u, List<Double> E, double n, double l2) {
        double r = rank(u, E);
        return (n - 1) * (r - 1) / l2 + 1;
    }

    private Interval update(double u, Interval a, double r, double k, double upsilon) {
        if (r < k - upsilon / 2.0) {
            return new Interval(u, a.sup());
        }
        else if (r > k + upsilon / 2.0) {
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

    /**
     * Calculates the Rank of the value x in S
     * The Rank is defined as:
     * 
     * RANK_{S}(x) = |{x' \in S|x' < x}| + 1
     * 
     * @param x
     *            The value x
     * @param S
     *            The multi set S
     * @return The rank
     */
    private int rank(double x, Collection<Double> S) {
        int rank = 0;
        for (Double s : S) {
            if (s < x) {
                rank++;
            }
        }
        return rank + 1;
    }

    private int gamma(double a, double b, Collection<Double> S) {
        int elements = 0;
        for (Double s : S) {
            if ((a < s) && (s < b)) {
                elements++;
            }
        }
        return elements;
    }

    private boolean isApproximateRank(double x, Collection<Double> S, int k, double upsilon) {
        return isApproximate(rank(x, S), k, upsilon);
    }

    private boolean isApproximate(double a, double b, double c) {
        return Math.abs(a - b) <= c;
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
        final GuhaMedianPartialAggregate<?> agg = new GuhaMedianPartialAggregate<>(1.0);
        for (int i = 0; i < 100; i++) {
            agg.add(3.0);
            agg.add(2.0);
            agg.add(0.0);
            agg.add(5.0);
            agg.add(4.0);
        }
        assert (agg.getAggValue() == 2.5);
        System.out.println(agg);
    }

    private class Instance {
        private List<Double> S;
        private List<List<Double>> partition = new ArrayList<List<Double>>();
        private Interval ab = Interval.INFINITY;
        private int end;
        private final double upsilon;
        private final double phi;
        private final double l1;
        private final double l2;
        private final double l;

        /**
 * 
 */
        public Instance(int n, int end) {
            this.end = end;
            this.S = new ArrayList<Double>(n);

            double k = n / 2.0;
            this.upsilon = upsilon(delta(), n, k);
            this.phi = phi(delta(), n, this.upsilon);
            this.l1 = l1(delta(), n, this.phi, this.upsilon);
            this.l2 = l2(delta(), n, this.phi, this.upsilon, k);
            this.l = l1 + l2;
        }

        public void add(Double value) {
            S.add(value);
        }

        public Double getAggValue() {
            System.out.println("[a,b]: " + ab);
            System.out.println("Upsilon: " + upsilon);
            System.out.println("phi: " + phi);
            System.out.println("l1: " + l1);
            System.out.println("l2: " + l2);
            System.out.println("l: " + l);
            System.out.println("n: " + S.size());
            System.out.println("k: " + (S.size() / 2.0));
            System.out.println("Gamma: " + gamma(ab.inf(), ab.sup(), S));
            System.out.println("P should be:" + (S.size() / this.l));
            return ab.inf();
        }

        /**
         * \phi = 4(log_{4/3}(n/\Upsilon) + (ln(3/\delta)
         * log_{4/3}(n/\Upsilon))^{1/2})
         * 
         * @param delta
         *            The delta
         * @param n
         *            The stream size
         * @param upsilon
         *            The upsilon value
         * @return The phi value
         */
        private double phi(double delta, double n, double upsilon) {
            // return 4.0 * (FastMath.log(4.0 / 3.0, n / upsilon) +
            // FastMath.pow(FastMath.log(3.0 / delta) * FastMath.log(4.0 / 3.0,
            // n / upsilon), 1.0 / 2.0));
            return 2.0 * (FastMath.log10(n / upsilon) + FastMath.pow(FastMath.log(3.0 / delta) * FastMath.log10(n / upsilon), 1.0 / 2.0));
        }

        private double l1(double delta, double n, double phi, double upsilon) {
            return n * FastMath.pow(upsilon, -1.0) * FastMath.log(3.0 * FastMath.pow(n, 2.0) * phi / delta);
        }

        private double l2(double delta, double n, double phi, double upsilon, double k) {
            return 2.0 * (n - 1.0) * FastMath.pow(upsilon, -1.0) * FastMath.sqrt((k + upsilon) * FastMath.log(6.0 * n * phi / delta));
        }

        /**
         * 
         * \Upsilon = 20 ln^{2}(n) ln(\delta^{-1}) \sqrt(k)
         * 
         * @param delta
         *            The delta
         * @param n
         *            The size of the stream
         * @param k
         *            The k
         * @return The upsilon value
         */
        private double upsilon(double delta, double n, double k) {
            // return 10.0 * FastMath.pow(FastMath.log(n), 2.0) *
            // Math.log(FastMath.pow(delta, -1.0)) * FastMath.sqrt(k);
            return 20.0 * FastMath.pow(FastMath.log(n), 2.0) * Math.log(FastMath.pow(delta, -1.0)) * FastMath.sqrt(k);
        }

        private double delta() {
            return DELTA;
        }

    }

}
