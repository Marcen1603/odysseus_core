package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Iterator;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

public class LinearRegressionPO<T extends IMetaAttribute> extends
AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void process_next(ProbabilisticTuple<T> object, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
