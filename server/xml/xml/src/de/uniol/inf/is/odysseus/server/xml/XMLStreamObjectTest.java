package de.uniol.inf.is.odysseus.server.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.server.xml.XMLStreamObjectDataHandler;

public class XMLStreamObjectTest
{
	public static void main(String[] args)
	{
		//System.out.println( new File("test.xml").getAbsolutePath());
		XMLStreamObjectDataHandler dataHandler;
		try
		{
			dataHandler = new XMLStreamObjectDataHandler();
			FileInputStream fis = new FileInputStream("c:/test.xml");
			XMLStreamObject<?> xmlDoc = dataHandler.readData(fis, false);
			//XMLStreamObject<?> xmlDoc2 = xmlDoc.createInstance(xmlDoc.xpathToDocument("//mail"));
			//System.out.println(xmlDoc.xpathToString("//node()"));
			//System.out.println(xmlDoc2.isEmpty() ? "EMPTY" : "NON EMPTY");
			System.out.println(xmlDoc.xpathToString("count(//mail)"));
			
			
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
/*
package de.uniol.inf.is.odysseus.server.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;

public class XMLStreamObjectTest
{
	public static void main(String[] args)
	{
		//System.out.println( new File("test.xml").getAbsolutePath());
		XMLStreamObjectDataHandler dataHandler;
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventReader eventReader;
        XMLEventWriter eventWriter = null;
        StringWriter output = new StringWriter();
        List<String> list = new LinkedList<String>(); 
        int i = 0;
		try
		{
			eventReader = inputFactory.createXMLEventReader(new FileInputStream("c:/test.xml"));
	        dataHandler = new XMLStreamObjectDataHandler();
	        String elementName = "mail";
			while(eventReader.hasNext())
			{
				XMLEvent event = eventReader.nextEvent();
				if(event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement().getName().getLocalPart().equalsIgnoreCase(elementName))
				{
					i++;
					eventWriter = outputFactory.createXMLEventWriter(output);
					eventWriter.add(event);
				}
				else if(event.getEventType() == XMLStreamConstants.END_ELEMENT && event.asEndElement().getName().getLocalPart().equalsIgnoreCase(elementName))
				{
					eventWriter.add(event);
					list.add(output.toString());
					i++;
					eventWriter.close();
					eventWriter = null;
					output = new StringWriter();
				}
				else if(eventWriter != null) 
				{
					eventWriter.add(event);
				}
			}			
			eventReader.close();
		
		} catch (FileNotFoundException | XMLStreamException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list.forEach(v -> { System.out.println(v); System.out.println("------------------------"); });
		System.out.println(i);
	}
}
*/