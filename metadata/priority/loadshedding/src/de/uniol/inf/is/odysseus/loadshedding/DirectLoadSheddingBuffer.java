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
