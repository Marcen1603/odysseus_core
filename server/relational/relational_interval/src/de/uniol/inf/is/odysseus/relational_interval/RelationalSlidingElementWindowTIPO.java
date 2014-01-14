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
package de.uniol.inf.is.odysseus.relational_interval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingElementWindowTIPO;

public class RelationalSlidingElementWindowTIPO extends
		SlidingElementWindowTIPO<Tuple<ITimeInterval>> {
	
	static Logger LOG = LoggerFactory.getLogger(RelationalSlidingElementWindowTIPO.class);



	@SuppressWarnings("unchecked")
	public RelationalSlidingElementWindowTIPO(AbstractWindowAO ao) {
		super(ao);
		@SuppressWarnings("rawtypes")
		RelationalGroupProcessor r = new RelationalGroupProcessor<>(ao.getInputSchema(), ao.getOutputSchema(), ao.getPartitionBy(), null);
		setGroupProcessor(r);
	}



}
