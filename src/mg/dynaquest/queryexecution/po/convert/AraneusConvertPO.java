package mg.dynaquest.queryexecution.po.convert;

/** 
 Diese Klasse dient wandelt einen String, der eine
 HTML-Seiten repräsentiert mit Hilfe des Araneus-Toolkits
 in eine XML-Seite um.
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:13 $ 
 Version: $Revision: 1.8 $
 Log: $Log: AraneusConvertPO.java,v $
 Log: Revision 1.8  2004/09/16 08:57:13  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.7  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.6  2003/06/16 15:57:58  hobelmann
 Log: println() -> debug()
 Log:
 Log: Revision 1.5  2003/06/04 15:16:18  grawund
 Log: no message
 Log:
 Log: Revision 1.4  2002/02/20 15:51:50  grawund
 Log: Fall 2 beim IMDB-Zugriff umgesetzt
 Log:
 Log: Revision 1.3  2002/02/07 13:33:56  grawund
 Log: Umstellung der Planoperatoren auf mehrere Ausgabeströme
 Log:
 Log: Revision 1.2  2002/02/06 14:02:26  grawund
 Log: Einbindung beliebiger Araneus-Konverter moeglich
 Log:
 Log: Revision 1.1  2002/02/06 09:03:01  grawund
 Log: [no comments]
 Log:
 */

import editor.EditorDocument;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.xml.DOMHelp;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author  Marco Grawunder
 */
public class AraneusConvertPO extends ConvertPO {
    /**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger logger = Logger.getLogger(this.getClass().getName());
	/**
	 * @uml.property  name="convertClassName"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="editor.EditorDocument"
	 */
	String convertClassName = "";

	/**
	 * @uml.property  name="startMethodName"
	 */
	String startMethodName = "";

	/**
	 * @uml.property  name="doc"
	 * @uml.associationEnd  
	 */
	EditorDocument doc = null;

	/**
	 * @uml.property  name="startMethod"
	 */
	Method startMethod = null;

	/**
	 * @uml.property  name="reInitMethod"
	 */
	Method reInitMethod = null;

	public AraneusConvertPO(AraneusConvertPO convertPO) {
		super(convertPO);
	}

	public AraneusConvertPO() {
		super();
	}

	public String getInternalPOName() {
		return "AraneusConvertPO";
	}

	/**
	 * @param convertClassName  the convertClassName to set
	 * @uml.property  name="convertClassName"
	 */
	public void setConvertClassName(String name) {
		this.convertClassName = name;
	}

	public void setStartMethodeName(String name) {
		this.startMethodName = name;
	}

	protected boolean process_open() throws POException {
		boolean success = false;
		try {
			// Vom Classloader die Klasse laden
			Class doc_class = Class.forName(convertClassName);
			doc = (EditorDocument) doc_class.newInstance();
			// und die Startmethode einlesen (als Parameter hat
			// die Startmethode ein EditorDokument)
			Class[] pars = new Class[1];
			pars[0] = doc_class;
			this.startMethod = doc_class.getMethod(startMethodName, pars);
			this.reInitMethod = doc_class.getMethod("ReInit", (Class[])null);
			success = true;
		} catch (Exception e) {
			// Das ist schlecht, denn dann kann ich diesen
			// Converter nicht benutzen. Aus Recovery-Gründen
			// sollte aber trotzdem eine Exception abgefangen werden
			// und u.U. mit einer anderen Quelle oder mit einem
			// anderen PO weiter gemacht werden
			e.printStackTrace();
			POException poEx = new POException("ConverterClassProblems "
					+ convertClassName);
			poEx.fillInStackTrace();
			throw poEx;
		}
		return success;
	}

	protected boolean process_close() throws POException {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
		return true;
	}

	protected Object process_next() throws POException, TimeoutException {
		Document xmlDoc = null;
		String htmlPage = (String) this.getInputNext(this, -1);
		if (htmlPage != null) {
			Reader reader = new StringReader(htmlPage);
			try {
				logger.debug("Verarbeite neue HTML-Seite");
				doc.open(reader);
				reInitMethod.invoke(doc, (Object[]) null);
				// Parameter (das Dokument)
				Object[] args = new Object[1];
				args[0] = doc;
				// Methode ist static, deswegen null
				try {
					editor.Document result = (editor.Document) startMethod
							.invoke(null, args);
					// Noch eben schnell alle "&" ersetzen ... die kommen
					// ja leider in HTML-Seiten vor (replace ersetzt nur _einen_
					// Eintrag!)
					while (result.replace("&", "&amp;")) {
					}
					;
					//System.out.println("Nach dem Ersetzen
					// "+result.toString());

					try {
						xmlDoc = DOMHelp.parseString(result.toString(), false);
					} catch (Exception e) {
						System.err
								.println("Fehler beim Parsen des XML-Dokumentes "
										+ e.getMessage());
						e.printStackTrace();
					}
				} catch (Exception e) {
					System.err.println("Fehler (innen) --->");
					e.printStackTrace();
				}
			} catch (Exception e) {
				System.err.println("Fehler (aussen) --->");
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw new POException(e);
			}
		}
		return xmlDoc;
	}

	protected void initInternalBaseValues(NodeList childNodes) {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	}

	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	}

	public SupportsCloneMe cloneMe() {
		return new AraneusConvertPO(this);
	}


}