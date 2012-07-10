/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.planmanagement.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan.ScheduleMeta;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public interface IPartialPlan {
	public long getId();
	public List<IIterableSource<?>> getIterableSources();
	public boolean hasIteratableSources();
	public IIterableSource<?> getIterableSource(int id);
	public int getSourceId(IIterableSource<?> source);
	public List<IPhysicalOperator> getRoots();
	
	public long getCurrentPriority();
	public void setCurrentPriority(long newPriority);
	public long getBasePriority();

	ScheduleMeta getScheduleMeta();
	void setScheduleMeta(ScheduleMeta scheduleMeta);	
	
	@Override
	public int hashCode();
	
	@Override
	public String toString();
	List<IPhysicalOperator> getQueryRoots();
	List<IPhysicalQuery> getQueries();


}
