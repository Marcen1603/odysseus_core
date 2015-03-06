package de.uniol.inf.is.odysseus.sensors.utilities;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlMarshalHelper<T>
{
	private Class<T> baseType;
	private JAXBContext context;
	
	public XmlMarshalHelper(Class<T> baseType)
	{
		this.baseType = baseType;
		try 
		{
			context = JAXBContext.newInstance(baseType);
		} 
		catch (JAXBException e) 
		{
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public XmlMarshalHelper(Class<T> baseType, Class[] derivedTypes)
	{
		Class[] types = new Class[derivedTypes.length+1];
		types[0] = baseType;
		System.arraycopy(types, 1, derivedTypes, 0, derivedTypes.length);
		
		this.baseType = baseType;
		try 
		{
			context = JAXBContext.newInstance(types);
		} 
		catch (JAXBException e) 
		{
			throw new RuntimeException(e);
		}
	}	
	
	public T fromXmlFile(File file) throws IOException
	{
		try 
		{			
			Unmarshaller xmlUnmarshaller = context.createUnmarshaller();
			T result = baseType.cast(xmlUnmarshaller.unmarshal(file));
			
			if (XmlMarshalHelperHandler.class.isAssignableFrom(baseType))
				((XmlMarshalHelperHandler) result).onUnmarshalling(file);
			
			return result;
		} 
		catch (JAXBException e) 
		{
			throw new RuntimeException(e);
		}		
	}
	
	public T fromXml(String xmlString)
	{
		try 
		{
			Unmarshaller xmlUnmarshaller = context.createUnmarshaller();
			T result = baseType.cast(xmlUnmarshaller.unmarshal(new StringReader(xmlString)));
			
			if (XmlMarshalHelperHandler.class.isAssignableFrom(baseType))
				((XmlMarshalHelperHandler) result).onUnmarshalling(xmlString);
			
			return result;
			
		} 
		catch (JAXBException e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	public String toXml(T input)
	{
		StringWriter sw = new StringWriter();
	    try 
	    {
			Marshaller xmlMarshaller = context.createMarshaller();
			xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			xmlMarshaller.marshal(baseType.cast(input), sw);
			return sw.toString();
		} 
	    catch (JAXBException e) 
	    {
			throw new RuntimeException(e);
		}						
	}
	
	public void toXmlFile(T input, File file) throws IOException
	{
	    try 
	    {
			Marshaller xmlMarshaller = context.createMarshaller();
			xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			xmlMarshaller.marshal(baseType.cast(input), file);
		} 
	    catch (JAXBException e) 
	    {
			throw new RuntimeException(e);
		}						
	}	
	
}