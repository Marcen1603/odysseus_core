package de.uniol.inf.is.odysseus.nexmark.xml;


/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:13 $ 
 Version: $Revision: 1.8 $
 Log: $Log: DOMHelp.java,v $
 Log: Revision 1.8  2004/09/16 08:57:13  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.7  2003/11/27 15:47:02  grawund
 Log: no message
 Log:
 Log: Revision 1.6  2003/08/20 13:42:44  grawund
 Log: Ergaenzung bei dump wird jetzt auch der XML Header mit ausgegeben
 Log:
 Log: Revision 1.5  2003/06/05 13:45:12  grawund
 Log: no message
 Log:
 Log: Revision 1.4  2002/02/07 12:48:46  grawund
 Log: Alle Methoden haben jetzt ein Synchronized-Modifier!
 Log:
 Log: Revision 1.3  2002/02/06 14:02:22  grawund
 Log: Einbindung beliebiger Araneus-Konverter moeglich
 Log:
 Log: Revision 1.2  2002/01/31 16:14:00  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.Reader;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class DOMHelp {

    static Transformer serializer = null;

    public static synchronized void initSerializer() {
        // Set up an identity transformer to use as serializer.
        try {
            serializer = TransformerFactory.newInstance().newTransformer();
            serializer
                    .setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DocumentBuilder newBuilder(boolean validation)
            throws ParserConfigurationException {
        // Create JAXP factory for document builders
        DocumentBuilderFactory domFactory = DocumentBuilderFactory
                .newInstance();

        // Konfiguration der Factory
        domFactory.setValidating(validation);
        domFactory.setNamespaceAware(true);

        // Create a new document builder
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();

        // Setzen des Error Handlers
        domBuilder.setErrorHandler(new PrintErrorHandler());

        return domBuilder;

    }

    public static synchronized Document parse(String path, boolean validation)
            throws ParserConfigurationException, IOException, SAXException {

        // Builder erzeugen
        DocumentBuilder domBuilder = newBuilder(validation);

        // Parsen der Datei
        Document document = domBuilder.parse(new File(path));

        // zurueckliefern des geparsten
        return document;
    }

    public static synchronized Document parseString(String text,
            boolean validation) throws ParserConfigurationException,
            IOException, SAXException {

        // Builder erzeugen
        DocumentBuilder domBuilder = newBuilder(validation);

        // Erzeugs aus dem String einen Strom
        Reader reader = new StringReader(text);

        InputSource stream = new InputSource(reader);

        // Parsen des Stroms
        Document document = domBuilder.parse(stream);

        // zurueckliefern des geparsten
        return document;
    }

    // Nimmt einen Knoten und trennt ihn von seinem Baum
    public static synchronized Node createNode(Node node) {
        if (serializer == null)
            initSerializer();
        DOMResult res = new DOMResult();
        try {
            serializer.transform(new DOMSource(node), res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.getNode();
    }

    public static synchronized void dumpNode(Node node, boolean useXMLDeclaration) {
        dumpNode(node, new StreamResult(System.out), useXMLDeclaration);
    }

    public static synchronized void dumpNode(Node node, Writer writer, boolean useXMLDeclaration) {
        dumpNode(node, new StreamResult(writer), useXMLDeclaration);
    }

    public static synchronized void dumpNode(Node node, StreamResult out, boolean useXMLDeclaration) {
        if (serializer == null)
            initSerializer();
        if (useXMLDeclaration){
            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        }
        try {
            serializer.transform(new DOMSource(node), out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    }
}