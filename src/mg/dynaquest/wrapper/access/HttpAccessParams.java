package mg.dynaquest.wrapper.access;

/** Klasse dient dazu, die Parameter fuer den Zugriff auf einen Web-Server
 zu kapseln
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.7 $
 Log: $Log: HttpAccessParams.java,v $
 Log: Revision 1.7  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.6  2004/05/28 07:58:36  grawund
 Log: no message
 Log:
 Log: Revision 1.5  2003/11/27 15:47:02  grawund
 Log: no message
 Log:
 Log: Revision 1.4  2003/09/23 09:17:02  grawund
 Log: UTF-8
 Log:
 Log: Revision 1.3  2002/02/20 15:51:40  grawund
 Log: Fall 2 beim IMDB-Zugriff umgesetzt
 Log:
 Log: Revision 1.2  2002/02/06 14:02:11  grawund
 Log: Einbindung beliebiger Araneus-Konverter moeglich
 Log:
 Log: Revision 1.1  2002/02/05 13:37:32  grawund
 Log: [no comments]
 Log:
 Log: Revision 1.2  2002/01/31 16:14:11  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author  Marco Grawunder
 */
public class HttpAccessParams {

	/**
	 * @uml.property  name="url"
	 */
	private URL url = null;

	/**
	 * @uml.property  name="params"
	 */
	private StringBuffer params = new StringBuffer();

	/**
	 * @uml.property  name="post"
	 */
	private boolean post = false;

	public void setURL(String seite) throws MalformedURLException {
		this.url = new URL(seite);
	}

	public void setPost() {
		this.post = true;
	}

	public void setGet() {
		this.post = false;
	}

	public void appendParam(String name, String value) {
		// erst mal hart codiert UTF-8
		try {
			this.appendParam(name, value, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void appendParam(String name, String value, String encoding)
			throws UnsupportedEncodingException {

		if (params.length() > 0) {
			params.append("&");
		}
		params.append(URLEncoder.encode(name, encoding)).append("=").append(
				URLEncoder.encode(value, encoding));
	}

	/**
	 * @return  the post
	 * @uml.property  name="post"
	 */
	public boolean isPost() {
		return this.post;
	}

	public boolean isGet() {
		return !this.post;
	}

	public URL getURL() throws MalformedURLException {
		if (isGet()) {
			// Falls Get, dann müssen die Parameter
			// noch angehaengt werden
			if (params.length() != 0) {
				System.out.println(params);
				System.out.println(url);
				return new URL(this.url.toString() + "?" + params);
			} else {
				return new URL(this.url.toString());
			}
		} else {
			return new URL(this.url.toString());
		}

	}

	/**
	 * @return  the params
	 * @uml.property  name="params"
	 */
	public String getParams() {
		return params.toString();
	}

}