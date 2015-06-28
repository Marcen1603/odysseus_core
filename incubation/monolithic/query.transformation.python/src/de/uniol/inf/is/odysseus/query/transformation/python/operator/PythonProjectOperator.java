package de.uniol.inf.is.odysseus.query.transformation.python.operator;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;

public class PythonProjectOperator extends AbstractTransformationOperator {

	private final String name =  "ProjectAO";
	private final String targetPlatform = "Python";
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	@Override
	public String getCode(ILogicalOperator operator) {
		// TODO Auto-generated method stub
		return "ProjectMachES";
	}

	@Override
	public Set<String> getNeededImports() {
		// TODO Auto-generated method stub
		return null;
	}

}
