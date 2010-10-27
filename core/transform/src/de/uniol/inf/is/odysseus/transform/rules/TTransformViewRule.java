package de.uniol.inf.is.odysseus.transform.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.engine.TransformationExecutor;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTransformViewRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration transformConfig) {
		System.out.println("Transform view: " + accessAO);
		String sourceName = accessAO.getSource().getURI(false);
		//ILogicalOperator cPlan = AbstractTreeWalker.prefixWalk($view, new CopyLogicalPlanVisitor());
		ILogicalOperator view = DataDictionary.getInstance().getViewForTransformation(accessAO.getSource().getURI());
		ILogicalOperator cPlan = view;

		ITransformation transformation = new TransformationExecutor();
		
		ArrayList<IPhysicalOperator> roots = null;
		try {
			roots = transformation.transform(cPlan, transformConfig);
		} catch (TransformationException e) {		
			e.printStackTrace();
		}
		
		
		// get the first root, since this is the physical operator for the passed plan
		// and this will be the connection to the current plan.
		if(roots.get(0).isSource()){
			ISource<?> source = (ISource<?>)roots.get(0);
			List<AccessAO> accessAOs = new ArrayList<AccessAO>();
			List<?> currentWM = super.getCollection();
			for(Object o: currentWM){
				if(o instanceof AccessAO){
					AccessAO other = (AccessAO)o;
					if(other.getSource().getURI().equals(accessAO.getSource().getURI())){
						accessAOs.add(other);
					}
				}
			}
			
			
			WrapperPlanFactory.putAccessPlan(sourceName, source);
			for(AccessAO curAO : (List<AccessAO>)accessAOs){
//				Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(curAO,source);
//				for (ILogicalOperator o:toUpdate){
//					update(o);
//				}
				replace(curAO, source, transformConfig);
				retract(curAO);
			}
		}
		else{
			throw new RuntimeException("Cannot transform view: Root of view plan is no source.");
		}
		
	}

	@Override
	public boolean isExecutable(AccessAO accessAO, TransformationConfiguration transformConfig) {
		if(WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI(false))==null){
			ILogicalOperator view = DataDictionary.getInstance().getViewForTransformation(accessAO.getSource().getURI());
			if(view!=null){		
				if(view.getSubscribedToSource().size()!=1 || (!view.getSubscribedToSource(0).getTarget().equals(accessAO))){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Transform View";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

}
