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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class TimeStampOrderValidatorTIPO<K extends ITimeInterval, T extends IStreamObject<K>>
		extends AbstractPipe<T, T> {

	Logger logger = LoggerFactory.getLogger(TimeStampOrderValidatorTIPO.class);

	PointInTime lastTimestamp = null;
	T lastObject = null;
	T currObject = null;
	int debugMode = 0;

	private int suppressed;

	public TimeStampOrderValidatorTIPO(boolean debug, int debugMode) {
		setDebug(debug);
		this.debugMode = debugMode;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (validate(punctuation.getTime(), port)) {
			sendPunctuation(punctuation);
		} else {
			sendPunctuation(punctuation, Integer.MAX_VALUE);
		}
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
		currObject = object;
		if (validate(object.getMetadata(), port)) {
			transfer(object);
			lastObject = currObject;
		} else {
			transfer(object, Integer.MAX_VALUE);
		}
	}

	private boolean validate(K metadata, int port) {
		return validate(metadata.getStart(), port);
	}

	private boolean validate(PointInTime timestamp, int port) {
		if (lastTimestamp != null) {
			if (timestamp.before(lastTimestamp)) {
				if (debug) {
					switch (debugMode) {
					case 0:
						if (suppressed==0){
							logger.warn("Wrong timestamp order " + timestamp + " after " + lastTimestamp);
						}
						break;
					case 1:
						logger.warn("Wrong timestamp order " + timestamp + " after " + lastTimestamp);
						break;
					case 2:
						logger.warn("Wrong timestamp order " + timestamp + " after " + lastTimestamp
								+ " from previous operator: " + this.getSubscribedToSource(port).toString()
								+ "\nlast Object = " + lastObject + "\ncurrent Object=" + currObject);
						break;
					default:

					}
				}
				suppressed++;
				return false;
			}else{
				if (suppressed>0 && debug){
					logger.info("In Order again: "+timestamp+" after "+lastTimestamp+" suppressed "+suppressed+" elements");
				}
				suppressed = 0;
			}
		}
		lastTimestamp = timestamp.clone();
		return true;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof TimeStampOrderValidatorTIPO)) {
			return false;
		} else {
			return true;
		}
	}

}
