package de.uniol.inf.is.odysseus.query.transformation.python.operator;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;

public class PythonSelectOperator extends AbstractTransformationOperator {
	  
	 PythonSelectOperator(){
			super(SelectAO.class, "SelectAO","Python");
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
