
/*
 * Created on 15.11.2005
 *
 */
package mg.dynaquest.sourceselection.exception;

/**
 * @author Marco Grawunder
 *
 */
public class ReplaceByConCompensationImpossibleException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8501077474476495010L;

	/**
     * 
     */
    public ReplaceByConCompensationImpossibleException() {
        super("Generierung des neuen Zugriffsplan zur Kompensation der fehlenden Belegungen ist fehlgeschlagen!");
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public ReplaceByConCompensationImpossibleException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public ReplaceByConCompensationImpossibleException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public ReplaceByConCompensationImpossibleException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    }
}