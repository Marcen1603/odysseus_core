package mg.dynaquest.queryexecution.po.xml;

import mg.dynaquest.queryexecution.po.relational.FileDumperPO;
import mg.dynaquest.xml.*;
import mg.dynaquest.queryexecution.event.POException;
import org.w3c.dom.Node;

/**
 * Ausgabe von XML-Dokumenten in eine Datei Author: $Author: grawund $ Date:
 * $Date: 2004/09/16 08:57:11 $ Version: $Revision: 1.3 $ Log: $Log:
 * XMLFileDumperPO.java,v $ Log: Revision 1.2 2004/05/14 09:41:26 grawund Log:
 * Umstellung: process_next liefert nun das Objekt, putToBuffer ist private Log:
 * Log: Revision 1.1 2003/06/05 13:45:01 grawund Log: XMLFileDumperPO ist die
 * Analogie zum FileDumperPO nur dass hier Knoten in XML Serialisierung in das
 * File geschrieben werden Log:
 *  
 */

public class XMLFileDumperPO extends FileDumperPO {

	public XMLFileDumperPO(String filename, boolean isTop) {
		super(filename, isTop);
	}

	public Object process_next() throws POException {
		Object elem = null;
		try {
			elem = this.getInputNext(this, -1);
			if (elem != null) {
				DOMHelp.dumpNode((Node) elem, out, false);
				out.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			POException ex = new POException("Fehler in XMLFileDumperPO\n"
					+ e.getMessage());
			ex.fillInStackTrace();
			throw ex;
		}
		return elem;
	}

	public String getInternalPOName() {
		return "XMLFileDumperPO";
	}

}