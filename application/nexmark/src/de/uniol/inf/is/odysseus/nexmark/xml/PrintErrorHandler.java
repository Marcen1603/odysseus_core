/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.nexmark.xml;

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
	@Override
	public void warning(SAXParseException e) throws SAXException {
		System.err.print("WARNING: ");
		printLocation(e);
		printMessage(e);
	}

	/**
	 * Prints information about a parsing error.
	 */
	@Override
	public void error(SAXParseException e) throws SAXException {
		System.err.print("ERROR: ");
		printLocation(e);
		printStackTrace(e);
	}

	/**
	 * Prints information about a fatal parsing error and throws the exception.
	 */
	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		System.err.print("FATAL ERROR: ");
		printLocation(e);
		printStackTrace(e);
		throw e;
	}

	/**
	 * Prints the location of the error.
	 */
	private static void printLocation(SAXParseException e) {
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
	private static void printMessage(SAXException e) {
		if (e.getMessage() != null)
			System.err.println(e.getMessage());
	}

	/**
	 * Prints the stack trace of the given exception and its nested exceptions.
	 */
	private static void printStackTrace(SAXException e) {
		Exception ex = e;
		while (ex != null) {
			ex.printStackTrace();
			if (ex instanceof SAXException)
				ex = ((SAXException) ex).getException();
		}
	}

}