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
package de.uniol.inf.is.odysseus.loadshedding;

import java.util.Random;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.buffer.DirectInterlinkBufferedPipe;

public class DirectLoadSheddingBuffer<T extends IMetaAttributeContainer<? extends IPriority>>
extends DirectInterlinkBufferedPipe<T> {

	public static final int NO_LOAD_SHEDDING = -1;

	private Random rand = new Random(System.currentTimeMillis());

	private int rate = NO_LOAD_SHEDDING;
	private double weight = 0;

	public DirectLoadSheddingBuffer(){};

	public DirectLoadSheddingBuffer(
			DirectLoadSheddingBuffer<T> directLoadSheddingBuffer) {
		super(directLoadSheddingBuffer);
		rate = directLoadSheddingBuffer.rate;
		weight = directLoadSheddingBuffer.weight;
	}

	@Override
	protected void process_next(T next, int port) {

		if (rate != NO_LOAD_SHEDDING) {

			IPriority prio = (IPriority) next.getMetadata();

			if (prio.getPriority() > 0) {
				super.process_next(next, port);
			} else {

				rand.setSeed(System.currentTimeMillis());
				// Verwerfe zufaellig Elemente, um bei periodischen Daten nicht
				// alles kaputt zu machen (=> nicht so etwas wie
				// "loesche jedes 4.")
				if (rand.nextBoolean()) {
					super.process_next(next, port);
				} else {
					rate--;
				}
			}
		} else {
			super.process_next(next, port);
		}

	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}	
	
	@Override
	public DirectLoadSheddingBuffer<T> clone(){
		return new DirectLoadSheddingBuffer<T>(this);
	}
	
}
