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
package de.uniol.inf.is.odysseus.intervalapproach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;

public class NElementHeartbeatGeneration <K extends ITimeInterval, T extends IStreamObject<K>> implements IHeartbeatGenerationStrategy<T>{

	static private final Logger logger = LoggerFactory.getLogger(NElementHeartbeatGeneration.class);
	
	private int counter = 0;
	final private int eachElement;
	
	public NElementHeartbeatGeneration() {
		eachElement = 10;
	}

	public NElementHeartbeatGeneration(int eachElement) {
		this.eachElement = eachElement;
	}

	public NElementHeartbeatGeneration(
			NElementHeartbeatGeneration<K, T> nElementHeartbeatGeneration) {
		this.eachElement = nElementHeartbeatGeneration.eachElement;
		this.counter = nElementHeartbeatGeneration.counter;
	}

	@Override
	public void generateHeartbeat(T object, ISource<?> source) {
		if (counter % eachElement == 0){
			counter = 0;
			source.sendPunctuation(new Heartbeat(object.getMetadata().getStart()));
			logger.debug("Sending punctuation ... ");
		}
		counter++;
	}
	

	@Override
	public NElementHeartbeatGeneration<K, T> clone() {
		return new NElementHeartbeatGeneration<K, T>(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eachElement;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		NElementHeartbeatGeneration other = (NElementHeartbeatGeneration) obj;
		if (eachElement != other.eachElement)
			return false;
		return true;
	}

	
	
}
