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
		if(newOperator.isPipe()){
			toUpdate = transformationConfig.getTransformationHelper().replace(oldOperator, (IPipe<?, ?>)newOperator);
		}else if(newOperator.isSource()){
			toUpdate =  transformationConfig.getTransformationHelper().replace(oldOperator, (ISource<?>)newOperator);
		}else if(newOperator.isSink()){
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
