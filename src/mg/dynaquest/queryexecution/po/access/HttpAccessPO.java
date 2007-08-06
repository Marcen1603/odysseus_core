package mg.dynaquest.queryexecution.po.access;

/** 
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:13 $ 
 Version: $Revision: 1.10 $
 Log: $Log: HttpAccessPO.java,v $
 Log: Revision 1.10  2004/09/16 08:57:13  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.9  2004/05/28 07:58:36  grawund
 Log: no message
 Log:
 Log: Revision 1.8  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.7  2003/07/15 13:13:58  hobelmann
 Log: Http-Exceptions werden vorläufig einfach geschluckt.
 Log:
 Log: Revision 1.6  2002/02/20 15:51:56  grawund
 Log: Fall 2 beim IMDB-Zugriff umgesetzt
 Log:
 Log: Revision 1.5  2002/02/06 14:02:24  grawund
 Log: Einbindung beliebiger Araneus-Konverter moeglich
 Log:
 Log: Revision 1.4  2002/02/06 09:00:53  grawund
 Log: Schreibe String statt StringBuffer!
 Log:
 Log: Revision 1.3  2002/02/05 16:28:56  grawund
 Log: Kommentare eingefügt
 Log:
 Log: Revision 1.1  2002/02/05 13:37:32  grawund
 Log: [no comments]
 Log:
 Log: Revision 1.2  2002/01/31 16:14:11  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.wrapper.access.HttpAccess;
import mg.dynaquest.wrapper.access.HttpAccessParams;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author  Marco Grawunder
 */
public class HttpAccessPO extends NAryPlanOperator {

	// Falls die Seiten auf die zugeriffen werden soll,
	// statisch sind, stehen die Alternative hier drin
	private List<HttpAccessParams> httpAccessParams = new ArrayList<HttpAccessParams>();

	/**
	 * @uml.property  name="maxRetries"
	 */
	private int maxRetries = 5;

	/**
	 * @uml.property  name="successfullProcessed"
	 */
	private boolean successfullProcessed = false;

	/**
	 * @uml.property  name="baseURL"
	 */
	private String baseURL = "";

	public HttpAccessPO(HttpAccessPO accessPO) {
		super(accessPO);
		this.httpAccessParams = accessPO.httpAccessParams;
		this.maxRetries = accessPO.maxRetries;
		this.successfullProcessed = accessPO.successfullProcessed;
		this.baseURL = accessPO.baseURL;
	}

	public HttpAccessPO(AccessPO schemaTransformationAccessPO){
		super(schemaTransformationAccessPO);
	}
	
	public HttpAccessPO() {
		super();
	}

	// Falls die URLs von einem Vorgänger kommen und hier
	// keine komplette URL eingetragen ist, wird dies
	// durch diese baseURL ergänzt
	/**
	 * @param baseURL  the baseURL to set
	 * @uml.property  name="baseURL"
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public void appendHttpAccessParams(HttpAccessParams params) {
		httpAccessParams.add(params);
	}

	/**
	 * @param maxRetries  the maxRetries to set
	 * @uml.property  name="maxRetries"
	 */
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public String getInternalPOName() {
		return "HttpAccessPO";
	}

	private HttpAccessParams getInputNext() throws POException, TimeoutException {
		Object val = this.getInputNext(0, this, -1);
		//System.out.println("HttpAccessParams getInputNext() -->"+val);
		if (val == null) {
			return null;
		}
		if (val instanceof HttpAccessParams) {
			return (HttpAccessParams) val;
		}
		// Dann kommt die Eingabe in Form von <url>link</url>
		if (val instanceof Node) {
			Node n = (Node) val;
			Text t = (Text) n.getFirstChild().getFirstChild();
			// System.out.println(t.getData());
			HttpAccessParams param = new HttpAccessParams();
			param.setGet();
			try {
				// falls baseURL leer ist tut es nicht weh,
				// ansonsten wird sie hier angehaengt.
				param.setURL(baseURL + t.getData());
			} catch (Exception ex) {
				ex.printStackTrace();
				param = null;
			}
			return param;
		}
		return null;
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

	protected boolean process_open() throws POException {
		successfullProcessed = false;

		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
		return true;
	}

	protected Object process_next() throws POException, TimeoutException {
		BufferedReader in = null;
		String retString = null;
		HttpAccessParams param = null;

		if (this.getNumberOfInputs() == 0) { // statische Version
			if (!successfullProcessed) {
				// 0 ist noch statisch!
				param = (HttpAccessParams) httpAccessParams.get(0);
				// nur ein erfolgreicher Durchlauf!
			}
		} else {
			// Die dynamische Version, d.h. hole den AccessParam von Vorgänger
			param = getInputNext();
		}

		try {
			if (param != null) {
				if (param.isGet()) {
					in = HttpAccess.get(param.getURL(), maxRetries);
				} else {
					in = HttpAccess.post(param.getURL(), param.getParams(),
							maxRetries);
				}

				if (in != null) {
					StringBuffer retBuffer = new StringBuffer();
					String line = null;
					while (null != (line = in.readLine())) {
						retBuffer.append(line + "\n");
					}
					in.close();
					// Achtung den String in den Buffer packen, nicht den
					// StringBuffer!
					retString = retBuffer.toString();
					// Fuer den statischen Fall muss ich wissen, dass
					// es einen erfolgreichen Durchlauf gegeben hat
					//System.out.println(retBuffer.toString());
					successfullProcessed = true;
				}
			}
		} catch (Exception e) {
			// ToDo
			/*
			 * Hier kann theoretisch jede Exception auftreten, was in der
			 * Java-API leider nicht genauer spezifiziert wird. Meistens dürften
			 * es aber Http- oder DNS-Fehler sein.
			 */
			System.err.println("HttpAccessPO: " + e.toString());
			e.printStackTrace();
			//throw new POException(e);
		}
		return retString;
	}

	protected boolean process_close() throws POException {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
		return true;
	}

	public SupportsCloneMe cloneMe() {
		return new HttpAccessPO(this);
	}



}