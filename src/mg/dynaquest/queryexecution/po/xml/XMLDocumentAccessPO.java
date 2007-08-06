package mg.dynaquest.queryexecution.po.xml;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:11 $ 
 Version: $Revision: 1.5 $
 Log: $Log: XMLDocumentAccessPO.java,v $
 Log: Revision 1.5  2004/09/16 08:57:11  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.4  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.3  2003/11/27 15:47:02  grawund
 Log: no message
 Log:
 Log: Revision 1.2  2002/01/31 16:15:39  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class XMLDocumentAccessPO extends mg.dynaquest.queryexecution.po.access.PhysicalAccessPO {

	/**
	 * @uml.property  name="xmlfiles" multiplicity="(0 -1)" dimension="1"
	 */
	private String[] xmlfiles;

	/**
	 * @uml.property  name="currentDoc"
	 */
	private int currentDoc;

	public String getInternalPOName() {
		return "XMLDocumentAccessPO";
	}

	// Achtung nur Referenz übergeben
	public XMLDocumentAccessPO(String[] xmlFiles) {
		this.xmlfiles = xmlFiles;
		currentDoc = 0;
	}

	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	}

	protected void initInternalBaseValues(NodeList childNodes) {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	}

	protected Object process_next() throws POException {
		System.out.println("XMLDocumentAccessPO process next" + currentDoc
				+ " " + xmlfiles.length);
		Object retObj = null;
		if (currentDoc < xmlfiles.length) {
			try {
				System.out
						.println("Parse das Dokument " + xmlfiles[currentDoc]);
				retObj = mg.dynaquest.xml.DOMHelp.parse(xmlfiles[currentDoc], false);
				System.out.println("Parsen des Dokuments"
						+ xmlfiles[currentDoc] + " fertig");
				currentDoc++;
			} catch (Exception e) {
				e.printStackTrace();
				POException ex = new POException(
						"Fehler in XMLDocumentAccessPO\n" + e.getMessage());
				ex.fillInStackTrace();
				throw ex;
			}
		}
		return retObj;
	}

	public boolean process_open() throws POException {
		return true;
	}

	public boolean process_close() throws POException {
		return true;
	}

	public SupportsCloneMe cloneMe() {
		throw new NotImplementedException();
	}

}