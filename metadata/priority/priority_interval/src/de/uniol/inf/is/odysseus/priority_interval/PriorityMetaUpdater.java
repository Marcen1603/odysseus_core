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
package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

/**
 * Used to create IntervalPriorty metadata, if a ITimeInterval is already available inside
 * the plan.
 * @author jan
 * @deprecated only for testing purposes. don't use it!! ;)
 */
public class PriorityMetaUpdater extends AbstractMetadataUpdater<ITimeInterval,IMetaAttributeContainer<ITimeInterval>>{

	@Override
	public void updateMetadata(IMetaAttributeContainer<ITimeInterval> inElem) {
		ITimeInterval interval = inElem.getMetadata();
		IntervalPriority priority = new IntervalPriority();
		priority.setStart(interval.getStart());
		priority.setEnd(interval.getEnd());
		inElem.setMetadata(priority);
	}

}
