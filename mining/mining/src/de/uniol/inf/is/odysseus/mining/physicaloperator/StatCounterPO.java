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
package de.uniol.inf.is.odysseus.mining.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Dennis Geesen
 *
 */
public class StatCounterPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>>{

	private int counter = 0;
	private long lastTime = 0L;
	private long startTime = 0L;
	private int outputeach;

	public StatCounterPO(int outputeach){
		this.outputeach = outputeach;
	}
	/**
	 * @param statCounterPO
	 */
	public StatCounterPO(StatCounterPO<M> statCounterPO) {
		this(statCounterPO.outputeach);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();		
		startTime = System.currentTimeMillis();
		lastTime = startTime;
	}


	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_next(java.lang.Object, int)
	 */
	@Override
	protected void process_next(Tuple<M> object, int port) {
		if (counter % outputeach == 0) {
			long now = System.currentTimeMillis();
			long needed = (now - lastTime);
			long total = (now - startTime);

			lastTime = now;
			Tuple<M> countTuple = new Tuple<M>(3, false);
			countTuple.setAttribute(0, counter);
			countTuple.setAttribute(1, needed);
			countTuple.setAttribute(2, total);
			@SuppressWarnings("unchecked")
			M clonedMD = (M) object.getMetadata().clone();
			countTuple.setMetadata(clonedMD);
			transfer(countTuple, 0);
		}
		counter++;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#processPunctuation(de.uniol.inf.is.odysseus.core.metadata.PointInTime, int)
	 */
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone()
	 */
	@Override
	public StatCounterPO<M> clone() {
		return new StatCounterPO<M>(this);
	}

}
