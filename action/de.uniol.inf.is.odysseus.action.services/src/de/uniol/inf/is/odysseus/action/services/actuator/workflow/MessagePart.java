package de.uniol.inf.is.odysseus.action.services.actuator.workflow;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	/**
	 * Creates a new MessagePart
	 * @param inputObject Object holding properties
	 * @param isCorrelation flag defining if this messagePart should be used
	 * as Correlation
	 */
	public MessagePart(Object inputObject, boolean isCorrelation){
		this.propertyDescriptors = new ArrayList<PropertyDescriptor>();
		this.inputObject = inputObject;
		this.partClass = inputObject.getClass();
		this.correlation = isCorrelation;
	}
	
	/**
	 * Adds a new property for r/w access to this messagePart
	 * @param propertyName
	 * @throws IntrospectionException
	 */
	public void addPropertyDescriptor(String propertyName) throws IntrospectionException{
		PropertyDescriptor propDescriptor = new PropertyDescriptor(propertyName, this.partClass);
		this.propertyDescriptors.add(propDescriptor);
	}
	
	/**
	 * return object holding the properties
	 * @return
	 */
	public Object getInputObject() {
		return inputObject;
	}
	
	/**
	 * returns number of properties
	 * @return
	 */
	public int getNumberOfProperties(){
		return this.propertyDescriptors.size();
	}
	
	/**
	 * returns class of the inputObject
	 * @return
	 */
	public Class<?> getPartClass() {
		return partClass;
	}
	
	/**
	 * reads values from provided object
	 * @param input object of same class, as the inputObject provided at instantiation
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Map<String, Object> getValsForProperties(Object input) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Map<String, Object> properties = new HashMap<String, Object>();
		for (PropertyDescriptor descriptor : this.propertyDescriptors){
			Object val = descriptor.getReadMethod().invoke(input);
			properties.put(descriptor.getName(), val);
		}
		return properties;
	}
	
	/**
	 * returns correlation flag
	 * @return
	 */
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
