package de.uniol.inf.is.odysseus.transform.rule;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;

public abstract class AbstractTransformationRule<T> extends AbstractRule<T,TransformationConfiguration> {
	
	protected void replace(ILogicalOperator oldOperator, IPhysicalOperator newOperator, TransformationConfiguration transformationConfig){
		
		Collection<ILogicalOperator> toUpdate = new ArrayList<ILogicalOperator>();
		if(newOperator instanceof IPipe){
			toUpdate = transformationConfig.getTransformationHelper().replace(oldOperator, (IPipe<?, ?>)newOperator);
		}else if(newOperator instanceof ISource){
			toUpdate =  transformationConfig.getTransformationHelper().replace(oldOperator, (ISource<?>)newOperator);
		}else if(newOperator instanceof ISink){
			toUpdate=  transformationConfig.getTransformationHelper().replace(oldOperator, (ISink<?>)newOperator);
		}else{
			LoggerSystem.printlog("ERROR");
			throw 
			new RuntimeException(new TransformationException("replace in rule \""+getName()+"\" failed because the new operator is not an ISink, ISource or IPipe!"));
		}		
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		
	}
	
}
