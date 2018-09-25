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
package de.uniol.inf.is.odysseus.server.intervalapproach;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.AbstractTISyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IInputStreamSyncArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IProcessInternal;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TIInputStreamSyncArea<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractTISyncArea<T,T> implements IInputStreamSyncArea<T> {


	private static final long serialVersionUID = -7253572485695648190L;
	private IProcessInternal<T> po;
	
	public TIInputStreamSyncArea() {
		super();
	}

	public TIInputStreamSyncArea(TIInputStreamSyncArea<T> other) {
		super(other);
	}

	@Override
	public void init(IProcessInternal<T> po, int inputPortCount) {
		this.po = po;
		super.init(po, inputPortCount);
	}
	
	/**
	 * Add a new element and produce output
	 * Remark: This is different than in transferArea where new element only states that
	 * there is a new element
	 * @param object
	 * @param inPort
	 */
	@Override
	public void transfer(T object, int inPort){
		super.transfer(object,inPort);
		super.newElement(object, inPort);
		
	}
	

	@Override
	public TIInputStreamSyncArea<T> clone() {
		return new TIInputStreamSyncArea<T>(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void transfer(Pair<IStreamable, Integer> elem) {
		po.process_internal((T) elem.getE1(), elem.getE2());
	}

	@Override
	protected void transferPunctuation(Pair<IStreamable, Integer> elem) {
		po.process_punctuation_intern((IPunctuation) elem.getE1(), elem.getE2());
	}


}
