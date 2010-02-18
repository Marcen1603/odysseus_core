package de.uniol.inf.is.odysseus.action.services.actuator.workflow;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * MessagePart of a webservice method.
 * Used to store property information about the part
 * @author Simon Flandergan
 *
 */
public class MessagePart {
	private Object inputObject;
	private Class<?> partClass;
	private List<PropertyDescriptor> propertyDescriptors;
	private boolean correlation;
		
	public MessagePart(QName messageName, Object inputObject, boolean isCorrelation){
		this.propertyDescriptors = new ArrayList<PropertyDescriptor>();
		this.inputObject = inputObject;
		this.partClass = inputObject.getClass();
		this.correlation = isCorrelation;
	}
	
	/**
	 * Adds a new property to this messagePart
	 * @param propertyName
	 * @throws IntrospectionException
	 */
	public void addPropertyDescriptor(String propertyName) throws IntrospectionException{
		PropertyDescriptor propDescriptor = new PropertyDescriptor(propertyName, this.partClass);
		this.propertyDescriptors.add(propDescriptor);
	}
	
	
	public Object getInputObject() {
		return inputObject;
	}
	
	public int getNumberOfProperties(){
		return this.propertyDescriptors.size();
	}
	
	public Class<?> getPartClass() {
		return partClass;
	}
	
	public Map<String, Object> getValsForProperties(Object input) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Map<String, Object> properties = new HashMap<String, Object>();
		for (PropertyDescriptor descriptor : this.propertyDescriptors){
			Object val = descriptor.getReadMethod().invoke(input);
			properties.put(descriptor.getName(), val);
		}
		return properties;
	}
	
	public boolean isCorrelation() {
		return correlation;
	}
	
	/**
	 * Write values to properties with values
	 * @param values
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void setValsForProperties(Object[] values) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if (this.propertyDescriptors.size() != values.length){
			throw new IllegalArgumentException();
		}else {
			Iterator<PropertyDescriptor> iterator = this.propertyDescriptors.iterator();
			for (Object val : values){
				iterator.next().getWriteMethod().invoke(inputObject, val);
			}
		}
	}
	
	
}
