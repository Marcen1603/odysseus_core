package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class defines the exception thrown if the CEPListView the object which
 * should be added to a list is not a instance of the class
 * StateMachineInstance.
 * 
 * @author Christian
 */
@SuppressWarnings("serial")
public class NoStateMachineInstanceException extends RuntimeException {

	// the constructor
	public NoStateMachineInstanceException() {
		super(StringConst.EXCEPTION_NO_STATEMACHINEINSTANCE);
	}
}
