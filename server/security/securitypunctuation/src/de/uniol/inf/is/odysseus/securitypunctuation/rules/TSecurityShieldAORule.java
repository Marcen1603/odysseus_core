/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.securitypunctuation.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.FileSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSecurityShieldAORule extends AbstractTransformationRule<TopAO> {
	
	private Integer counter = 0;
	
	@Override
	public int getPriority() {
		return 100;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(TopAO topAO,
			TransformationConfiguration transformConfig) throws RuleException {
		if(transformConfig.getOption("isSecurityAware") != null) {		
			if (Boolean.parseBoolean((String)transformConfig.getOption("isSecurityAware")) )  {
				Collection<IPhysicalOperator> physInputPOs = topAO.getPhysInputPOs();
				ISource oldFather = null;

				Collection<ILogicalOperator> topAOCollection = new ArrayList<ILogicalOperator>();
				topAOCollection.add(topAO);
				
				for(IPhysicalOperator e:physInputPOs) {
					if(e.isSource()) {
						oldFather = (ISource<?>) e;
						if(!(oldFather instanceof SecurityShieldPO)) {
							IPipe securityShieldPO = new SecurityShieldPO();
							securityShieldPO.setOutputSchema(oldFather.getOutputSchema()); 					
							transformConfig.getTransformationHelper().insertNewFather(oldFather, topAOCollection, securityShieldPO);
						}
					} else if(e instanceof FileSinkPO && counter++ < 1) {
						List<AbstractPhysicalSubscription<ISource<? extends IStreamObject<?>>>> fileSink = ((FileSinkPO) e).getSubscribedToSource();
						for(AbstractPhysicalSubscription<ISource<? extends IStreamObject<?>>> source:fileSink) {
							// Wenn auch CalcLatency direkt vor FileSInk, dann SecShield auch davor noch setzen... - f�r Benchmarks
//							if(source.getTarget() instanceof LatencyCalculationPipe) {
//								LatencyCalculationPipe pipe = (LatencyCalculationPipe) source.getTarget();
//								List<PhysicalSubscription<ISource<? extends Object>>> subscribedTo = pipe.getSubscribedToSource();
//								for(PhysicalSubscription<ISource<? extends Object>> source2:subscribedTo) {
//									oldFather = (ISource<?>) source2.getTarget();
//									IPipe securityShieldPO = new SecurityShieldPO();
//									securityShieldPO.setOutputSchema(oldFather.getOutputSchema()); // ???
//									Collection<ISubscription<ISink>> testds = new ArrayList<ISubscription<ISink>>();
//									PhysicalSubscription phsy = new PhysicalSubscription(source.getTarget(), 0, 0, oldFather.getOutputSchema());
//									testds.add(phsy);
//									transformConfig.getTransformationHelper().insertNewFatherPhysical(oldFather, testds, securityShieldPO);
//								}
//							} else {
								oldFather = source.getTarget();
								IPipe securityShieldPO = new SecurityShieldPO();
								securityShieldPO.setOutputSchema(e.getOutputSchema());
								Collection<ISubscription<ISink>> testds = new ArrayList<ISubscription<ISink>>();
								AbstractPhysicalSubscription phsy = new ControllablePhysicalSubscription(e, 0, 0, e.getOutputSchema());
								testds.add(phsy);
								transformConfig.getTransformationHelper().insertNewFatherPhysical(oldFather, testds, securityShieldPO);
//							}
						}
					}
				}						
			}	
		}
	}

	@Override
	public boolean isExecutable(TopAO topAO,
			TransformationConfiguration config) {	
		return topAO.isAllPhysicalInputSet() 
				&& (config.getOption("isSecurityAware") != null) 
				&& ((Boolean)config.getOption("isSecurityAware")) ;	
	}

	@Override
	public String getName() {
		return "SecurityShieldAO -> SecurityShieldPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SECURITY;
	}

	@Override
	public Class<? super TopAO> getConditionClass() {	
		return TopAO.class;
	}


}
