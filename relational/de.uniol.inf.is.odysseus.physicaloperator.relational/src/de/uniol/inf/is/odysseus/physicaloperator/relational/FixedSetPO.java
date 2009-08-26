package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.concurrent.atomic.AtomicBoolean;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;


public final class FixedSetPO<T extends IMetaAttribute<? extends IClone>> extends
		AbstractSource<T> implements
		IIterableSource<T> {

	private final T[] tuples;
	private int index;
	private AtomicBoolean isDone;

	public FixedSetPO(T... tuples) {
		this.tuples = tuples;
		this.isDone = new AtomicBoolean();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		index = 0;
		this.isDone.set(false);
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

}
