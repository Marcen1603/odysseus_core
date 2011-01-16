package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

@SuppressWarnings("serial")
public class CEPViewerNotShownException extends RuntimeException {

	public CEPViewerNotShownException() {
		super("CEPViewer could not be shown in the workbench!");
	}
}
