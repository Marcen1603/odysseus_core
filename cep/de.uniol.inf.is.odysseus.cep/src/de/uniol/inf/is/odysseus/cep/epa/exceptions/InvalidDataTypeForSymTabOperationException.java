package de.uniol.inf.is.odysseus.cep.epa.exceptions;

/**
 * Diese Exception besagt, dass der Datentyp des in der Symboltabelle
 * gespeicherten Wertes von der auszuführenden Symboltabellen-Operation nicht
 * unterstützt wird.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class InvalidDataTypeForSymTabOperationException extends
		RuntimeException {

	private static final long serialVersionUID = -8861088345694270394L;

	public InvalidDataTypeForSymTabOperationException() {
	}

	public InvalidDataTypeForSymTabOperationException(String arg0) {
		super(arg0);
	}

	public InvalidDataTypeForSymTabOperationException(Throwable arg0) {
		super(arg0);
	}

	public InvalidDataTypeForSymTabOperationException(String arg0,
			Throwable arg1) {
		super(arg0, arg1);
	}

}
