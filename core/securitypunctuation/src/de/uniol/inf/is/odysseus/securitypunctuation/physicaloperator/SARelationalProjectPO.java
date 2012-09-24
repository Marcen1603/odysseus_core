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

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.sp.SecurityPunctuation;

public class SARelationalProjectPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends AbstractPipe<T, T> {
	
    private static Logger LOG = LoggerFactory.getLogger(SARelationalProjectPO.class);

	private int[] restrictList;

	public SARelationalProjectPO(int[] restrictList) {
		this.restrictList = restrictList;
	}

	public SARelationalProjectPO(SARelationalProjectPO<T> relationalProjectPO) {
		super();
		int length = relationalProjectPO.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(relationalProjectPO.restrictList, 0, restrictList, 0, length);
	}	

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		try {
			T out = (T) ((Tuple<IMetaAttribute>) object).restrict(this.restrictList, false);
			transfer(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SARelationalProjectPO<T> clone() {
		return new SARelationalProjectPO<T>(this);
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof SARelationalProjectPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		SARelationalProjectPO<T> rppo = (SARelationalProjectPO<T>) ipo;
		if(this.hasSameSources(ipo) &&
				this.restrictList.length == rppo.restrictList.length) {
			for(int i = 0; i<this.restrictList.length; i++) {
				if(this.restrictList[i] != rppo.restrictList[i]) {
					return false;
				}
			}
			return true;
		}
        return false;
	}
	
	public int[] getRestrictList() {
		return restrictList;
	}
	
	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		if(projectSPEvaluate(sp)) {
			this.transferSecurityPunctuation(sp);
			LOG.debug("sent SP in ProjectPO");
		} else {
			this.transferSecurityPunctuation(new SecurityPunctuation(sp.getSchema(), sp.getLongAttribute("ts")));
			LOG.debug("sent Empty SP in ProjectPO");
		}
	}
	
	public Boolean projectSPEvaluate(ISecurityPunctuation sp) {
		ArrayList<String> spAttributes = sp.getStringArrayListAttribute("attributeNames");
		SDFSchema tupleSchema = null;		
		
		if(spAttributes == null) {
			return false;
		}
		if(spAttributes.size() == 1 && spAttributes.get(0).equals("*")) {
			return true;
		}
		
		for(PhysicalSubscription<ISource<? extends T>> subscribedTo:this.getSubscribedToSource()) {
			tupleSchema = subscribedTo.getSchema();
			
			for(String spAttribute:spAttributes) {
				for(int index:restrictList) {
					if(tupleSchema.get(index).getAttributeName().equals(spAttribute)) {
						return true;
					}
				} 	
			}
		}
		return false;
	}
	
	@Override
	public String getName() {
		return "SAProject";
	}
}
