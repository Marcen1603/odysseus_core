package mg.dynaquest.queryexecution.po.relational;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.7 $
 Log: $Log: FileDumperPO.java,v $
 Log: Revision 1.7  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.6  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.5  2004/02/25 14:57:27  grawund
 Log: *** empty log message ***
 Log:
 Log: Revision 1.4  2003/06/05 13:44:10  grawund
 Log: XMLFileDumperPO benötigt Zugriff auf einige Attribute deswegen Protected statt Private
 Log:
 Log: Revision 1.3  2002/02/07 13:33:58  grawund
 Log: Umstellung der Planoperatoren auf mehrere Ausgabeströme
 Log:
 Log: Revision 1.2  2002/01/31 16:14:26  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author  Marco Grawunder
 */
public class FileDumperPO extends UnaryPlanOperator {
	/**
	 * @uml.property  name="out"
	 */
	protected BufferedWriter out = null;

	/**
	 * @uml.property  name="filename"
	 */
	String filename = "";

	/**
	 * @uml.property  name="isTop"
	 */
	protected boolean isTop = false;

	public FileDumperPO() {
		super();
	};

	public FileDumperPO(FileDumperPO po){
		super(po);
		this.filename = po.filename;
		this.isTop = po.isTop;
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
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public FileDumperPO(String filename, boolean isTop) {
		this.filename = filename;
		this.isTop = isTop;
	}

	public synchronized boolean process_open() throws POException {
		boolean retVal = true;
		try {
			out = new BufferedWriter(new FileWriter(filename));
		} catch (Exception e) {
			retVal = false;
			POException ex = new POException("Fehler beim Zugriff auf "
					+ filename + "\n" + e.getMessage());
			ex.fillInStackTrace();
			throw ex;
		}
		return retVal;
	}

	public Object process_next() throws POException {
		Object elem = null;
		try {
			elem = this.getInputNext(this, -1);
			if (elem != null) {
				out.write(elem.toString() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			POException ex = new POException("Fehler in FileDumperPO\n"
					+ e.getMessage());
			ex.fillInStackTrace();
			throw ex;
		}
		return elem;
	}

	public synchronized boolean process_close() throws POException {
		boolean retVal = true;
		try {
			out.flush();
			out.close();
			retVal = true;
		} catch (Exception e) {
			retVal = false;
			POException ex = new POException("Fehler beim Schliessen von "
					+ filename);
			ex.fillInStackTrace();
			throw ex;
		}
		return retVal;
	}

	public String getInternalPOName() {
		return "FileDumperPO";
	}

	protected void initInternalBaseValues(NodeList children) {
		//int restrAttributesFound = 0;
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

	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer retBuffer) {
		retBuffer.append(baseIndent + "<filename>" + this.getFilename()
				+ "</filename>\n");
	}

	public SupportsCloneMe cloneMe() {
		return new FileDumperPO(this);
	}

 
}