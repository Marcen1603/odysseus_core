package de.uniol.inf.is.odysseus.store;

import java.io.IOException;

public class StoreException extends RuntimeException {

	private static final long serialVersionUID = 1244925426430692075L;
	
	public StoreException(IOException e) {
		super(e);
	}

}
