/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.transform.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
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
//		System.out.println("Transform view: " + accessAO);
		String sourceName = accessAO.getSource().getURI(false);
		//ILogicalOperator cPlan = AbstractTreeWalker.prefixWalk($view, new CopyLogicalPlanVisitor());
		ILogicalOperator view = getDataDictionary().getStreamForTransformation(accessAO.getSource().getURI(), getCaller());
		ILogicalOperator cPlan = view;

		ITransformation transformation = new TransformationExecutor();
		
		ArrayList<IPhysicalOperator> roots = null;
		try {
			roots = transformation.transform(cPlan, transformConfig, getCaller(), getDataDictionary());
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
			
			insert(source);
			getDataDictionary().putAccessPlan(sourceName, source);
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
		if(getDataDictionary().getAccessPlan(accessAO.getSource().getURI(false))==null){
			ILogicalOperator view = getDataDictionary().getStreamForTransformation(accessAO.getSource().getURI(), getCaller());
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
	
	@Override
	public Class<?> getConditionClass() {	
		return AccessAO.class;
	}

}
