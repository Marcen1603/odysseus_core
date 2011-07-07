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
package de.uniol.inf.is.odysseus.planmanagement.bufferplacement.standardbufferplacementstrategy;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.BufferedPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;

/**
 *  
 * @author Jonas Jacobi, Marco Grawunder
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class StandardBufferPlacementStrategy 
	   extends	AbstractBufferPlacementStrategy {
 
	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}
	
	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink, ISink<?> sink) {
		return !(sink instanceof MetadataCreationPO);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		return new BufferedPipe();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {
		// do nothing. It's only a standard placement strategy.
	}

	
	
	@Override
	public String getName() {
		return "Standard Buffer Placement";
	}


}
