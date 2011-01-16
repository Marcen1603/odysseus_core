package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

@SuppressWarnings("serial")
public class CEPListViewNotFoundException extends RuntimeException {

	public CEPListViewNotFoundException() {
		super("CEPListView could not be found!");
	}
}
