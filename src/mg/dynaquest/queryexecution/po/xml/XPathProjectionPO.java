package mg.dynaquest.queryexecution.po.xml;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:11 $ 
 Version: $Revision: 1.8 $
 Log: $Log: XPathProjectionPO.java,v $
 Log: Revision 1.8  2004/09/16 08:57:11  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.7  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.6  2003/11/27 15:47:02  grawund
 Log: no message
 Log:
 Log: Revision 1.5  2003/06/16 15:58:20  hobelmann
 Log: println() -> debug()
 Log:
 Log: Revision 1.4  2002/02/20 15:51:55  grawund
 Log: Fall 2 beim IMDB-Zugriff umgesetzt
 Log:
 Log: Revision 1.3  2002/02/07 13:34:04  grawund
 Log: Umstellung der Planoperatoren auf mehrere Ausgabeströme
 Log:
 Log: Revision 1.2  2002/01/31 16:15:37  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.xml.DOMHelp;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public class XPathProjectionPO extends UnaryPlanOperator {

	/**
	 * @uml.property  name="xpathExpression"
	 */
	private String xpathExpression = "";

	/**
	 * @uml.property  name="xpathNodeIterator"
	 * @uml.associationEnd  
	 */
	private NodeIterator xpathNodeIterator = null;

	public String getInternalPOName() {
		return "XPathProjectionPO";
	}

	public XPathProjectionPO(String xpathExpression) {
		this.xpathExpression = xpathExpression;
	}

	public XPathProjectionPO(XPathProjectionPO projectionPO) {
		super(projectionPO);
		this.xpathExpression = projectionPO.xpathExpression;
	}

	public synchronized boolean process_open() throws POException {
		// TODO: Override this mg.dynaquest.queryexecution.po.base.PlanOperator
		// method
		return true;
	}

	private Object process_node() throws POException, TimeoutException {
		// Hier gibt es jetzt mehrere Situationen
		// 1. es gibt ein Dokument, welches verarbeitet wird
		// d.h. xpathNodeIterator ist ungleich null
		// System.out.println("Process Node");
		Node n = null;
		if (xpathNodeIterator != null) {
			if ((n = xpathNodeIterator.nextNode()) != null) {
				// und es gibt noch einen Knoten zum Verarbeiten
				// System.out.println("PutToBuffer ");
				Node n_neu = DOMHelp.createNode(n);
				//TODO: debug(DOMHelp.dumpNode(n_neu, false));
				return n_neu;
			} else {
				// Keine Knoten mehr vorhanden, ist wie der initiale Zustand
				xpathNodeIterator = null;
			}
		}

		// ab hier ist der xpathNodeIterator IMMER null!
		// Es muss ein neues Dokument angefordert werden
		Node node = (Node) this.getInputNext(this, -1);
		if (node != null) {
			try {
				//System.out.println("Neuer Iterator");
				xpathNodeIterator = XPathAPI.selectNodeIterator(node,
						xpathExpression);
				return process_node(); // Rekursiv aufrufen, damit auch
				// ein eventueller Knoten verarbeitet wird
			} catch (Exception e) {
				e.printStackTrace();
				POException ex = new POException(
						"Fehler in XPathProjectionPO\n" + e.getMessage());
				ex.fillInStackTrace();
				throw ex;
			}
		} else {
			return null;
		}
	}

	protected Object process_next() throws POException, TimeoutException {
		return process_node();
	}

	public boolean process_close() throws POException {
		// TODO: Override this mg.dynaquest.queryexecution.po.base.PlanOperator
		// method
		return true;
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

	public SupportsCloneMe cloneMe() {
		return new XPathProjectionPO(this);
	}



}