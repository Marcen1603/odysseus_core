package de.uniol.inf.is.odysseus.base.xml;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:13 $ 
 Version: $Revision: 1.3 $
 Log: $Log: PrintErrorHandler.java,v $
 Log: Revision 1.3  2004/09/16 08:57:13  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.2  2002/01/31 16:13:58  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Error handler that prints the messages to the standard error stream.
 */
public class PrintErrorHandler implements ErrorHandler {

	/**
	 * Prints a warning message.
	 */
	public void warning(SAXParseException e) throws SAXException {
		System.err.print("WARNING: ");
		printLocation(e);
		printMessage(e);
	}

	/**
	 * Prints information about a parsing error.
	 */
	public void error(SAXParseException e) throws SAXException {
		System.err.print("ERROR: ");
		printLocation(e);
		printStackTrace(e);
	}

	/**
	 * Prints information about a fatal parsing error and throws the exception.
	 */
	public void fatalError(SAXParseException e) throws SAXException {
		System.err.print("FATAL ERROR: ");
		printLocation(e);
		printStackTrace(e);
		throw e;
	}

	/**
	 * Prints the location of the error.
	 */
	private void printLocation(SAXParseException e) {
		if (e.getPublicId() != null)
			System.err.print(e.getPublicId() + " ");
		if (e.getSystemId() != null)
			System.err.print(e.getSystemId() + " ");
		if (e.getLineNumber() != -1)
			System.err.print("line " + e.getLineNumber() + " ");
		if (e.getColumnNumber() != -1)
			System.err.print("column " + e.getColumnNumber() + " ");
		System.err.println();
	}

	/**
	 * Prints the message of the given exception.
	 */
	private void printMessage(SAXException e) {
		if (e.getMessage() != null)
			System.err.println(e.getMessage());
	}

	/**
	 * Prints the stack trace of the given exception and its nested exceptions.
	 */
	private void printStackTrace(SAXException e) {
		Exception ex = e;
		while (ex != null) {
			ex.printStackTrace();
			if (ex instanceof SAXException)
				ex = ((SAXException) ex).getException();
		}
	}

}