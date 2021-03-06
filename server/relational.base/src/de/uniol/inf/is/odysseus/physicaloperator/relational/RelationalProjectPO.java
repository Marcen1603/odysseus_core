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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Jonas Jacobi
 */
public class RelationalProjectPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	Logger logger = LoggerFactory.getLogger(RelationalProjectPO.class);
	
	
	private int[] restrictList;

	public RelationalProjectPO(int[] restrictList) {
		this.restrictList = restrictList;
	}

	public RelationalProjectPO(RelationalProjectPO<T> relationalProjectPO) {
		super();
		int length = relationalProjectPO.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(relationalProjectPO.restrictList, 0, restrictList, 0, length);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override public boolean deliversStoredElement(int outputPort) { 
		return false; 
	}
	
	@Override
	final protected void process_next(Tuple<T> object, int port) {
		try {
			Tuple<T> out = object.restrict(this.restrictList, false);
//			logger.debug(this+" transferNext() "+object);			
			transfer(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof RelationalProjectPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		RelationalProjectPO<T> rppo = (RelationalProjectPO<T>) ipo;
		if(this.restrictList.length == rppo.restrictList.length) {
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
}
