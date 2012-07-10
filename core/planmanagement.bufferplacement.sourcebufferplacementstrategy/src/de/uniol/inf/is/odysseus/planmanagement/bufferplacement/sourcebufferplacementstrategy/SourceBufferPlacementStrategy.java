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
package de.uniol.inf.is.odysseus.planmanagement.bufferplacement.sourcebufferplacementstrategy;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferedPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;

@SuppressWarnings({"rawtypes"})
public class SourceBufferPlacementStrategy extends AbstractBufferPlacementStrategy{
	         
	
	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(SourceBufferPlacementStrategy.class);
		}
		return _logger;
	}
	
	@Override
	public String getName() {
		return "Source Buffer Placement";
	}

	@Override
	protected IBuffer<?> createNewBuffer() {
		return new BufferedPipe();
	}

	@Override
	protected void initBuffer(IBuffer buffer) {
		// do nothing.
	}

	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink, ISink<?> sink) {
		boolean setBuffer = sink instanceof MetadataCreationPO;
		if (setBuffer){
			getLogger().debug(childSink+" "+sink+" true");
		}
		return setBuffer;
	}

}
