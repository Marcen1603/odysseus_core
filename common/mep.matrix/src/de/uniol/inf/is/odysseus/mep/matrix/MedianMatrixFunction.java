/**
 *
 */
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MedianMatrixFunction extends AbstractFunction<Double> {

    private static final long serialVersionUID = 617735287831668170L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS };
    final DescriptiveStatistics statistics = new DescriptiveStatistics();

    public MedianMatrixFunction() {
        super("Median", 1, MedianMatrixFunction.ACC_TYPES, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        final RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        return this.getValueInternal(a);
    }

    protected Double getValueInternal(final RealMatrix a) {
        return new Double(a.walkInOptimizedOrder(new RealMatrixPreservingVisitor() {

            @Override
            public void start(final int rows, final int columns, final int startRow, final int endRow, final int startColumn, final int endColumn) {
                MedianMatrixFunction.this.statistics.clear();
            }

            @Override
            public void visit(final int row, final int column, final double value) {
                MedianMatrixFunction.this.statistics.addValue(value);

            }

            @Override
            public double end() {
                return MedianMatrixFunction.this.statistics.getPercentile(50);
            }

        }));
    }

}
