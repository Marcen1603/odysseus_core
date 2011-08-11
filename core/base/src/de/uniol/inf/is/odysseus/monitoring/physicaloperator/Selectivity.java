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
package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.monitoring.AbstractPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.event.POPortEvent;

public abstract class Selectivity extends AbstractPeriodicalMonitoringData<Double> implements IPOEventListener {

	private Integer writeCount;
	private int[] readCount;
	private int readCountSum;
	private int sourceCount;

	private Double value;

	public Selectivity(IPhysicalOperator target, int sourceCount, String type) {
		super(target, type);
		this.sourceCount = sourceCount;
		reset();
		target.subscribe(this, POEventType.PushDone);
		target.subscribe(this, POEventType.ProcessDone);
	}

	public Selectivity(Selectivity other) {
		super(other.getTarget(), other.getType());
		this.sourceCount = other.sourceCount;
		this.writeCount = other.writeCount;
		this.readCount = new int[other.readCount.length];
		System.arraycopy(other.readCount, 0, this.readCount, 0, other.readCount.length);
		this.readCountSum = other.readCountSum;
		this.value = other.value;
		((IPhysicalOperator) other.getTarget()).subscribe(this, POEventType.PushDone);
		((IPhysicalOperator) other.getTarget()).subscribe(this, POEventType.ProcessDone);
	}

	public Double getValue() {
		return value;
	}

	@Override
	public void reset() {
		this.writeCount = 0;
		if (this.readCount == null) {
			this.readCount = new int[sourceCount];
		}
		for (int i = 0; i < sourceCount; i++) {
			this.readCount[i] = 0;
		}
		this.readCountSum = 0;
	}

	final protected double getWriteCount() {
		return (double) this.writeCount;
	}

	final protected double getReadCount(int port) {
		return (double) this.readCount[port];
	}

	final protected double getReadCountSum() {
		return (double) this.readCountSum;
	}

	final protected double getReadCountProduct() {
		int c = 1;
		for (int i = 0; i < readCount.length; i++) {
			c *= readCount[i];
		}
		return (double) c;
	}

	final protected int getReadCountLength() {
		return readCount.length;
	}

	@Override
	public void eventOccured(IEvent<?, ?> event) {
		POEvent poEvent = (POEvent) event;

		if (poEvent.getPOEventType() == POEventType.PushDone) {
			++writeCount;
		} else if (poEvent.getPOEventType() == POEventType.ProcessDone) {
			this.readCount[((POPortEvent) poEvent).getPort()]++;
			readCountSum++;
		}
	}

	@Override
	public void run() {
		this.value = calcValue();
		notifySubscribers(this.value);
		reset();
	}

	protected abstract Double calcValue();

}
