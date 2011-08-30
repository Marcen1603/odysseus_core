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
import de.uniol.inf.is.odysseus.monitoring.ISubscriber;
import de.uniol.inf.is.odysseus.monitoring.MonitoringDataScheduler;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;

public class Datarate extends AbstractPeriodicalMonitoringData<Double>
		implements IPOEventListener {

	private int writeCount;
	private long lastTimestamp;
	private double value;

	public Datarate(IPhysicalOperator target) {
		super(target, MonitoringDataTypes.DATARATE.name);
		reset();
		target.subscribe(this, POEventType.PushDone);
	}

	public Datarate(Datarate datarate) {
		super(datarate.getTarget(), MonitoringDataTypes.DATARATE.name);
		this.writeCount = datarate.writeCount;
		this.lastTimestamp = datarate.lastTimestamp;
		this.value = datarate.value;
		((IPhysicalOperator) datarate.getTarget()).subscribe(this,
				POEventType.PushDone);
	}

	@Override
	public void reset() {
		this.writeCount = 0;
		this.lastTimestamp = System.nanoTime();
		this.value = new Double(0);
	}

	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public void run() {
//		synchronized (this) {
			long currentTime = System.nanoTime();

			this.value = (double) writeCount / ( (currentTime - lastTimestamp) / 1000000.0);
			notifySubscribers(value);
			lastTimestamp = currentTime;
			this.writeCount = 0;
//		}
	}

	@Override
	public void subscribe(ISubscriber<Double> subscriber) {
		if (subscribtionCount() == 0) {
			((ISource<?>) getTarget()).subscribe(this, POEventType.PushDone);
		}
		super.subscribe(subscriber);
	}

	@Override
	public void unsubscribe(ISubscriber<Double> subscriber) {
		super.unsubscribe(subscriber);
		if (subscribtionCount() == 0) {
			((ISource<?>) getTarget()).unsubscribe(this, POEventType.PushDone);
			MonitoringDataScheduler.getInstance().cancelPeriodicalMetadataItem(
					this);
		}
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long eventNanoTime) {
		++writeCount;
	}

	@Override
	public AbstractPeriodicalMonitoringData<Double> clone() {
		return new Datarate(this);
	}
}
