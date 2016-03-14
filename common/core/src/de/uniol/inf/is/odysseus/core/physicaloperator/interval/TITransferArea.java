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
package de.uniol.inf.is.odysseus.core.physicaloperator.interval;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransfer;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class TITransferArea<R extends IStreamObject<? extends ITimeInterval>, W extends IStreamObject<? extends ITimeInterval>>
		extends AbstractTISyncArea<R, W> implements ITransferArea<R, W> {

	private static final long serialVersionUID = -2968616402073295957L;

	// the operator that uses this sink
	protected transient ITransfer<W> po;


	public TITransferArea() {
		super();
	}

	private TITransferArea(TITransferArea<R, W> tiTransferFunction) {
		super(tiTransferFunction);
	}
	


	@Override
	public void init(ITransfer<W> po, int inputPortCount) {
		this.po = po;
		super.init(po, inputPortCount);
	}
	
	@Override
	protected void transferPunctuation(Pair<IStreamable, Integer> elem) {
		po.sendPunctuation((IPunctuation) elem.getE1(), elem.getE2());	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void transfer(Pair<IStreamable, Integer> elem) {
		po.transfer((W) elem.getE1(), elem.getE2());
	}
	
	@Override
	public void setTransfer(ITransfer<W> po) {
		this.po = po;
		super.setOperator(po);
	}
	
	@Override
	public TITransferArea<R, W> clone() {
		return new TITransferArea<R, W>(this);
	}

}
