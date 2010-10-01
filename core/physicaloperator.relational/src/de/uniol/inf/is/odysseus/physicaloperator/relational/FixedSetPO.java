package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.concurrent.atomic.AtomicBoolean;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;


public final class FixedSetPO<T extends IMetaAttributeContainer<? extends IClone>> extends
		AbstractIterableSource<T>{

	private final T[] tuples;
	private int index;
	private AtomicBoolean isDone;

	public FixedSetPO(T... tuples) {
		this.tuples = tuples;
		this.isDone = new AtomicBoolean(false);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		index = 0;
	}

	@Override
	public synchronized boolean hasNext() {
		return index < tuples.length;
	}

	@Override
	public synchronized void transferNext() {
		transfer(tuples[index++]);
		if (index == tuples.length) {
			this.isDone.set(true);
			propagateDone();
		}
	}

	@Override
	public boolean isDone() {
		return this.isDone.get();
	}

	@Override
	public FixedSetPO<T> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

}
