package de.uniol.inf.is.odysseus.physicaloperator.base;


/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractIterableSource<T> extends AbstractSource<T>
		implements IIterableSource<T> {


	public AbstractIterableSource() {

	}

	public AbstractIterableSource(AbstractIterableSource<T> source) {
		super(source);
	}

}
