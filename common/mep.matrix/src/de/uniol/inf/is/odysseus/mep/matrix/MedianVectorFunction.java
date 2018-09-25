/**
 *
 */
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorPreservingVisitor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MedianVectorFunction extends AbstractFunction<Double> {

    /**
     *
     */
    private static final long serialVersionUID = -580010727600142067L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS };
    final DescriptiveStatistics statistics = new DescriptiveStatistics();

    public MedianVectorFunction() {
        super("Median", 1, MedianVectorFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final RealVector a = new ArrayRealVector((double[]) this.getInputValue(0), false);
        return this.getValueInternal(a);
    }

    protected Double getValueInternal(final RealVector a) {
        return new Double(a.walkInOptimizedOrder(new RealVectorPreservingVisitor() {

            @Override
            public void start(final int dimension, final int start, final int end) {
                MedianVectorFunction.this.statistics.clear();
            }

            @Override
            public void visit(final int index, final double value) {
                MedianVectorFunction.this.statistics.addValue(value);

            }

            @Override
            public double end() {
                return MedianVectorFunction.this.statistics.getPercentile(50);
            }

        }));
    }

}
