package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class defines the exception thrown if the CEPViewer perspective could
 * not be shown in the workbench
 * 
 * @author Christian
 */
@SuppressWarnings("serial")
public class CEPViewerNotShownException extends RuntimeException {

	// the constructor
	public CEPViewerNotShownException() {
		super(StringConst.EXCEPTION_CEPVIEWER_NOT_SHOWN);
	}
}
