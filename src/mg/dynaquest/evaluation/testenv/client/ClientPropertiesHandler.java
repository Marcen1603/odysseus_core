/**
 * 
 */
package mg.dynaquest.evaluation.testenv.client;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Liest mit Hilfe eines <code>SAXParser</code>s die Einstellungen des Clients aus einer Datei, das Ergebnis kann mit  {@link #getProperties()  getProperties()}  als String-Feld  abgerufen werden
 * @author  <a href="mailto:tmueller@polaris-neu.offis.uni-oldenburg.de">Tobias Mueller</a>
 */
public class ClientPropertiesHandler extends DefaultHandler {

	// Anzahl der einzulesenden Einstellungen
	private static final int PROPERTY_NUMBER = 4;
	// root-Element der xml-Datei
	private static final String DOCUMENT_ROOT = "properties";
	// genaue Ausgaben der einzelnen Schritte?
	private static final boolean DEBUG = true;
	
	// Name des aktuellen xml-Tags
	/**
	 * @uml.property  name="currentElement"
	 */
	private String currentElement = "";
	// Index, an dem im property-Feld als nächstes geschrieben wird
	/**
	 * @uml.property  name="propertyIndex"
	 */
	private int propertyIndex = 0;
	// die eingelesenen Einstellungen
	/**
	 * @uml.property  name="properties"
	 */
	private String[] properties;
	
	/**
	 * Erzeugt einen neuen <code>ClientPropertiesHandler</code>.
	 *
	 */
	public ClientPropertiesHandler()  {
		properties = new String[PROPERTY_NUMBER];
	}

	/* 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	/**
	 * Diese Methode ist aus Implementierungsgründen <code>public</code>.
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		String val = new String(ch,start,length).trim();
		if (DEBUG)  {
			System.out.println("ClientPropertiesHandler.characters: Lese " + val + ", currentElement = "
					+ currentElement);
		}
		// root-Element sowie Leerzeichen und Zeilenumbrüche im
		// Inhalt der Elemente ignorieren
		if (val.equals("") || currentElement.equals(DOCUMENT_ROOT) )  {
			if (DEBUG) {
				System.out.println("ClientPropertiesHandler.characters: Überspringe root-Element "
						+ "oder Whitespace");
			}
			return;
		}
		// Wert speichern
		else  {
			if (DEBUG)  {
				System.out.println("ClientPropertiesHandler.characters: " + currentElement + " = " + val);
			}
			properties[propertyIndex] = val;
			propertyIndex ++;
		}
	}

	/* 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement
	 *          (java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	/**
	 * Diese Methode ist aus Implementierungsgründen <code>public</code>.
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) 
																				throws SAXException {
		currentElement = qName;
	}
	
	/**
	 * Übergibt die eingelesenen Einstellungen.
	 * @return  die eingelesenen Einstellungen in Form eines String-Feldes.
	 * @uml.property  name="properties"
	 */
	public String[] getProperties()  {
		return properties;
	}
}
