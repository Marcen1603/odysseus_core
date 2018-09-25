package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;

/**
 * Use this class for serializable pairs.
 * @author Michael Brand
 */
public class SerializablePair<E1 extends Serializable, E2 extends Serializable>
		extends Pair<E1, E2> implements Serializable{

	private static final long serialVersionUID = -3290341737990285490L;

	public SerializablePair(){
		e1=null;e2=null;
	}
	
	public SerializablePair(E1 e1, E2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

}