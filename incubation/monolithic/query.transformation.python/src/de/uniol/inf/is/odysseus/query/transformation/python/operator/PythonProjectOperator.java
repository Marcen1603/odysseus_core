package de.uniol.inf.is.odysseus.query.transformation.python.operator;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class PythonProjectOperator extends AbstractTransformationOperator {
	
	PythonProjectOperator(){
		super(PythonProjectOperator.class, "ProjectAO","Python");
	}
	

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Set<String> getNeededImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void defineImports() {
		// TODO Auto-generated method stub
		
	}

}
