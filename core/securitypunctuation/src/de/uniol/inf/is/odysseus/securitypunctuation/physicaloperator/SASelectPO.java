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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.SecurityPunctuationCache;

public class SASelectPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends SelectPO<T> {

	private SecurityPunctuationCache spCache = new SecurityPunctuationCache();	
	
	public SASelectPO(IPredicate<? super T> predicate) {
		super(predicate);
	}
	
	public SASelectPO(SASelectPO<T> po){
		super(po);
	}
	
	@Override
	protected void process_next(T object, int port) {
		if (getPredicate().evaluate(object)) {			
			Long ts = object.getMetadata().getStart().getMainPoint();
			ISecurityPunctuation sp = spCache.getMatchingSP(ts);
			if(sp != null) {
				super.processSecurityPunctuation(sp, port);
				spCache.cleanCache(sp.getLongAttribute("ts"));
			}
			transfer(object);
		} else {
			// Send filtered data to output port 1
			transfer(object,1);
			getHeartbeatGenerationStrategy().generateHeartbeat(object, this);
		}
	}
	
	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		spCache.add(sp);
	}
	
	@Override
	public String getName() {
		return "SASelect";
	}
}
