package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class defines the exception thrown if the CEPListView is called without
 * selecting an CepOperator within the GraphEditor of the rcp component.
 * 
 * @author Christian
 */
@SuppressWarnings("serial")
public class NoCepOperatorSelectedException extends RuntimeException {

	// the constructor
	public NoCepOperatorSelectedException() {
		super(StringConst.EXCEPTION_NO_CEPOPERATOR_SELECTED);
	}
}
