package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class defines the exception thrown if the CEPListView could not be
 * found within the registry of the workbench.
 * 
 * @author Christian
 */
@SuppressWarnings("serial")
public class CEPListViewNotFoundException extends RuntimeException {

	// the constructor
	public CEPListViewNotFoundException() {
		super(StringConst.EXCEPTION_CEP_LIST_VIEW_NOT_FOUND);
	}
}
