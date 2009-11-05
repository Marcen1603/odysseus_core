package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractIterableSource;

abstract public class AbstractInputStreamAccessPO <In, Out extends IMetaAttributeContainer<?>> extends
AbstractIterableSource<Out>{
	
	protected ObjectInputStream iStream;
	final protected IDataTransformation<In, Out> transformation;
	protected Out buffer;
	protected boolean done = false;
	
	public AbstractInputStreamAccessPO(IDataTransformation<In, Out> transformation) {
		this.transformation = transformation;
	}
	
	@Override
	protected void process_done() {
		done = true;
		try {
			this.iStream.close();
		} catch (IOException e) {
			// we are done, we don't care anymore for exceptions
		}
	}

	@Override
	synchronized public void transferNext() {
		if (buffer == null) {
			if (!hasNext()) {
				propagateDone();// TODO wie soll mit diesem fehler umgegangen
				// werden
				throw new NoSuchElementException();
			}
		}
		transfer(buffer);
		buffer = null;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	

}
