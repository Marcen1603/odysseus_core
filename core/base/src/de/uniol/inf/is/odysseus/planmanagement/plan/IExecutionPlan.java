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
package de.uniol.inf.is.odysseus.planmanagement.plan;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public interface IExecutionPlan extends IClone{
		
	public void setLeafSources(List<IIterableSource<?>> leafSources);
	public List<IIterableSource<?>> getLeafSources();
	
	public void setPartialPlans(List<IPartialPlan> patialPlans);
	public List<IPartialPlan> getPartialPlans();
	
	public void close();
	//public void open() throws OpenFailedException;

//	public void initWith(IExecutionPlan newExecutionPlan);
	public Set<IPhysicalOperator> getRoots();
//	public void setRoots(List<IPhysicalOperator> roots);
	public IExecutionPlan clone();
	
}
