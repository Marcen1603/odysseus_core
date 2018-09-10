/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * base abstract class for filtering algorithms
 * 
 * @author ChrisToenjesDeye
 *
 */
public abstract class AbstractFiltering<T extends IStreamObject<?>> implements IFiltering<T>{

	private boolean needsReinitialization;

	@Override
	public void setReinitializationMode(boolean mode) {
		this.needsReinitialization = mode;
	}
	
	@Override
	public boolean needsReinitialization() {
		return needsReinitialization;
	}
	

	
}
