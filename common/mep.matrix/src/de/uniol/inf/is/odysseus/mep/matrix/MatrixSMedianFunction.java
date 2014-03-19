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
 * 
 */
public class MatrixSMedianFunction extends AbstractFunction<Double> {

    private static final long serialVersionUID = 617735287831668170L;

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.MATRIXS };
    private final DescriptiveStatistics stats = new DescriptiveStatistics();

    public MatrixSMedianFunction() {
        super("sMedian", 1, accTypes, SDFDatatype.DOUBLE);
    }

    @Override
    public Double getValue() {
        RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        return getValueInternal(a);
    }

    protected double getValueInternal(RealMatrix a) {

        return a.walkInOptimizedOrder(new RealMatrixPreservingVisitor() {

            @Override
            public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
                stats.clear();
            }

            @Override
            public void visit(int row, int column, double value) {
                stats.addValue(value);

            }

            @Override
            public double end() {
                return stats.getPercentile(50);
            }

        });
    }

}
