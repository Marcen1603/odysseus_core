/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.pnapproach.pn.physicaloperator.relational.join.helper;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.sweeparea.DefaultPNSweepArea;

public class PNTransferFunction<R extends IMetaAttributeContainer<? extends IPosNeg>,W extends IMetaAttributeContainer<? extends IPosNeg>> implements ITransferArea<R,W>{

	final protected PointInTime[] minTs;
	private AbstractSource<W> po;
	final protected DefaultPNSweepArea<W> sweepArea;
	private int counter;

	public PNTransferFunction() {
		minTs = new PointInTime[2];
		this.sweepArea = new DefaultPNSweepArea<W>();
		this.counter = 0;
	}

	public PNTransferFunction(PNTransferFunction<R,W> pnTransferFunction) {
		minTs = new PointInTime[2];
		this.minTs[0] = pnTransferFunction.minTs[0].clone();
		this.minTs[1] = pnTransferFunction.minTs[1].clone();
		this.sweepArea = pnTransferFunction.sweepArea.clone();
	}

	@Override
	public void init(AbstractSource<W> po) {
		this.po = po;
		this.minTs[0] = PointInTime.getZeroTime();
		this.minTs[1] = PointInTime.getZeroTime();
		this.sweepArea.clear();
	}
	
	@Override
	public void setSourcePo(AbstractSource<W> source) {
		this.po = source;
	}
	
	@Override
	public void newElement(R object, int port) {
		PointInTime minimum;
		synchronized (minTs) {
			minTs[port] = object.getMetadata().getTimestamp();
			minimum = PointInTime.before(minTs[0], minTs[1]) ? minTs[0] : minTs[1];
		}
		synchronized (this.sweepArea) {
			Iterator<W> elements = this.sweepArea.extractElementsBefore(minimum);
			while (elements.hasNext()) {
				po.transfer(elements.next());
			}
		}
	}

	@Override
	public void transfer(W object) {
		synchronized (this.sweepArea) {
			this.counter += sweepArea.insertAndRemovedWrongScheduled(object);	
		}
	}

	@Override
	public void done() {
		for (W element : sweepArea) {
			po.transfer(element);
		}
		sweepArea.clear();
	}
	
	public int getWrongResultCount(){
		return counter;
	}
	
	@Override
	public int size(){
		return sweepArea.size();
	}
	
	@Override
	public PNTransferFunction<R,W> clone() {
		return new PNTransferFunction<R,W>(this);
	}

	@Override
	public void newHeartbeat(PointInTime heartbeat, int port) {
		
	}




}
