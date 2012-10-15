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
package de.uniol.inf.is.odysseus.intervalapproach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;

public class DefaultHeartbeatGeneration<K extends ITimeInterval, T extends IStreamObject<K>> implements IHeartbeatGenerationStrategy<T>{

	Logger logger = LoggerFactory.getLogger(DefaultHeartbeatGeneration.class);
	
	@Override
	public void generateHeartbeat(T object, ISource<?> source) {
		source.sendPunctuation(object.getMetadata().getStart());
		logger.debug("Send Heartbeat"+object.getMetadata().getStart());
	}

	@Override
	public DefaultHeartbeatGeneration<K, T> clone() {
		return new DefaultHeartbeatGeneration<K, T>();
	}

		
}