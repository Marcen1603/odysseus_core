package de.uniol.inf.is.odysseus.loadshedding;

import java.util.Random;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class LoadShedderPO<T extends IMetaAttributeContainer<?>> extends
		AbstractPipe<T, T> {

	public static final int NO_LOAD_SHEDDING = -1;

	private Random rand = new Random(System.currentTimeMillis());

	private int rate = NO_LOAD_SHEDDING;

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T next, int port) {

		if (rate != NO_LOAD_SHEDDING) {

			IPriority prio = (IPriority) next.getMetadata();

			if (prio.getPriority() > 0) {
				transfer(next);
			} else {

				rand.setSeed(System.currentTimeMillis());
				// Verwerfe zufaellig Elemente, um bei periodischen Daten nicht
				// alles kaputt zu machen (=> nicht so etwas wie
				// "loesche jedes 4.")
				if (rand.nextBoolean()) {
					transfer(next);
				} else {
					rate--;
				}
			}
		} else {
			transfer(next);
		}

	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

}
