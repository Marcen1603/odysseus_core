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
package de.uniol.inf.is.odysseus.intervalapproach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class TimeStampOrderValidatorTIPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>> extends AbstractPipe<T, T> {

	Logger logger = LoggerFactory.getLogger(TimeStampOrderValidatorTIPO.class);

	PointInTime lastTimestamp = null;

	public TimeStampOrderValidatorTIPO(TimeStampOrderValidatorTIPO<K, T> timeStampOrderValidator) {
		super(timeStampOrderValidator);
		this.lastTimestamp = timeStampOrderValidator.lastTimestamp;
	}

	public TimeStampOrderValidatorTIPO() {
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		validate(timestamp, port);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		lastTimestamp = null;
	}

	@Override
	protected void process_next(T object, int port) {
		validate(object.getMetadata(), port);
		transfer(object);
	}

	private void validate(K metadata, int port) {
		validate(metadata.getStart(), port);
	}

	private void validate(PointInTime timestamp, int port) {
		if (lastTimestamp != null) {
			if (timestamp.before(lastTimestamp)) {
				logger.error("Wrong timestamp order " + timestamp + " after " + lastTimestamp + " from previous operator: "+this.getSubscribedToSource(port).toString());
				// TODO:Exception
			}
		}
		lastTimestamp = timestamp.clone();
	}

	@Override
	public TimeStampOrderValidatorTIPO<K, T> clone() {
		return new TimeStampOrderValidatorTIPO<K, T>(this);
	}

}
