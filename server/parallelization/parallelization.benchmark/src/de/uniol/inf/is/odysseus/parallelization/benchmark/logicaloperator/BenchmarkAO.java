package de.uniol.inf.is.odysseus.parallelization.benchmark.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

// no annotation, because it is not useful in pql
public class BenchmarkAO extends UnaryLogicalOp implements IStatefulAO{

	private static final long serialVersionUID = -880712993171839675L;
	private Long numberOfElements;

	public BenchmarkAO(){
        super();
    }
     
    public BenchmarkAO(BenchmarkAO benchmarkAO){
        super(benchmarkAO);
        this.numberOfElements = benchmarkAO.numberOfElements;
    }
	
	@Override
	public AbstractLogicalOperator clone() {
		return new BenchmarkAO(this);
	}	
}
