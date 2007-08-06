package mg.dynaquest.queryexecution.po.debug;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:13 $ 
 Version: $Revision: 1.5 $
 Log: $Log: LineReader.java,v $
 Log: Revision 1.5  2004/09/16 08:57:13  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.4  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.3  2004/02/25 14:57:27  grawund
 Log: *** empty log message ***
 Log:
 Log: Revision 1.2  2002/01/31 16:14:24  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.io.BufferedReader;
import java.io.FileReader;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.access.PhysicalAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author  Marco Grawunder
 */
public class LineReader extends PhysicalAccessPO{
	/**
	 * @uml.property  name="in"
	 */
	private BufferedReader in = null;

	/**
	 * @uml.property  name="filename"
	 */
	private String filename;

	public LineReader(String Filename) {
		this.filename = Filename;
	}

	public LineReader() {
		this.filename = null;
	}

	public String getInternalPOName() {
		return "LineReader";
	}

	/**
	 * @return  the filename
	 * @uml.property  name="filename"
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename  the filename to set
	 * @uml.property  name="filename"
	 */
	public synchronized void setFilename(String filename) {
		this.filename = filename;
	}

	public synchronized boolean process_open() throws POException {
		boolean success = false;
		try {
			in = new BufferedReader(new FileReader(filename));
			success = true;
		} catch (Exception e) {
			POException poE = new POException("Fehler beim ÷ffnen der Datei "
					+ filename);
			poE.fillInStackTrace();
			throw poE;
		}
		return success;
	}

	protected Object process_next() throws POException {
		synchronized (in) {
			String line = null;
			try {
				line = in.readLine();
			} catch (Exception e) {
				POException poE = new POException(
						"Fehler beim Zugriff auf die Datei " + filename);
				poE.fillInStackTrace();
				throw poE;
			}
			return line;
		}
	}

	public synchronized boolean process_close() throws POException {
		try {
			in.close();
			return true;
		} catch (Exception e) {
			POException poE = new POException(
					"Fehler beim Schlieﬂen der Datei " + filename);
			poE.fillInStackTrace();
			throw poE;
		}
	}

	public void initInternalBaseValues(NodeList children) {
		for (int i = 0; i < children.getLength(); i++) {
			Node cNode = children.item(i);

			// uns interessieren nur Element-Knoten
			// Kommentare, Textnodes etc. sind mir egal!
			if (cNode.getNodeType() != Node.ELEMENT_NODE)
				continue;

			if (cNode.getLocalName().equals("filename")) {
				Node firstChildNode = cNode.getFirstChild();
				if (firstChildNode != null) {
					this.setFilename(firstChildNode.getNodeValue());
				}
				continue;
			}
		}
	}

	public void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer retBuffer) {
		//super.getInternalXMLRepresentation(baseIndent, indent, retBuffer);
		retBuffer.append(baseIndent + "<filename>" + this.getFilename()
				+ "</filename>\n");
	}

	public SupportsCloneMe cloneMe() {
		// TODO Auto-generated method stub
		//return null;
		throw new NotImplementedException();
	}

}