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
package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.StandardSecurityEvaluator;

/**
 * @author Jan Sören Schwarz
 */
public class SecurityShieldPO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private StandardSecurityEvaluator<T> evaluator = new StandardSecurityEvaluator<T>((AbstractPipe<T, T>) this);
	
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();

	@Override
	protected void process_next(T object, int port) {
		if(evaluator.evaluate(object, this.getOwner(), this.getOutputSchema())) {
			transfer(object);
			evaluator.cleanCache(object.getMetadata().getStart().getMainPoint());
		} else {			
			transfer(object,1);
			heartbeatGenerationStrategy.generateHeartbeat(object, this);
		}
	}	

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {		
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new SecurityShieldPO<T>();
	}

	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		evaluator.addToCache(sp);
		this.transferSecurityPunctuation(sp);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
}
