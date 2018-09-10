package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

@SuppressWarnings("rawtypes")
public class XmlMarshalHelper
{
	private static Marshaller getMarshaller(Class[] classes) throws JAXBException
	{
    	JAXBContext context = JAXBContext.newInstance(classes);
		Marshaller xmlMarshaller = context.createMarshaller();
		xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		xmlMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");
		
		return xmlMarshaller;
	}	

	private static Unmarshaller getUnmarshaller(Class[] classes) throws JAXBException
	{
    	JAXBContext context = JAXBContext.newInstance(classes);
    	Unmarshaller xmlUnmarshaller = context.createUnmarshaller();

		return xmlUnmarshaller;
	}	
	
	public static String toXml(Object input)
	{
		return toXml(input, new Class[]{input.getClass()});
	}

	public static String toXml(Object input, Class[] classes)
	{
	    try 
	    {
	    	StringWriter sw = new StringWriter();
	    	getMarshaller(classes).marshal(input, sw);
			return sw.toString();
		} 
	    catch (JAXBException e) 
	    {
			throw new RuntimeException(e);
		}						
	}	
	
	public static void toXmlFile(Object input, File file) throws IOException
	{
		toXmlFile(input, file, new Class[]{input.getClass()});
	}
	
	public static void toXmlFile(Object input, File file, Class[] classes) throws IOException
	{
	    try 
	    {
			getMarshaller(classes).marshal(input, file);
		} 
	    catch (JAXBException e) 
	    {
			throw new RuntimeException(e);
		}						
	}
	
	public static <T> T fromXmlFile(File file, Class<T> clazz) throws IOException
	{
		return clazz.cast(fromXmlFile(file, new Class[]{clazz}));
	}
	
	public static Object fromXmlFile(File file, Class[] classes) throws IOException
	{
		try 
		{			
			Object result = getUnmarshaller(classes).unmarshal(file);
			
			if (XmlMarshalHelperHandler.class.isAssignableFrom(result.getClass()))
				((XmlMarshalHelperHandler) result).onUnmarshalling(file);
			
			return result;
		} 
		catch (JAXBException e) 
		{
			throw new RuntimeException(e);
		}		
	}
	
	public static <T> T fromXml(String xmlString, Class<T> clazz)
	{
		return clazz.cast(fromXml(xmlString, new Class[]{clazz}));
	}	
	
	public static Object fromXml(String xmlString, Class[] classes)
	{
		try 
		{			
			Object result = getUnmarshaller(classes).unmarshal(new StringReader(xmlString));
			
			if (XmlMarshalHelperHandler.class.isAssignableFrom(result.getClass()))
				((XmlMarshalHelperHandler) result).onUnmarshalling(xmlString);
			
			return result;
		} 
		catch (JAXBException e) 
		{
			throw new RuntimeException(e);
		}		
	}	
}