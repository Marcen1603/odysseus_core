package de.uniol.inf.is.odysseus.action.services.actuator.workflow;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * MessagePart of a webservice method.
 * Used to store property information about the part
 * @author Simon Flandergan
 *
 */
public class MessagePart {
	
	private Object inputObject;
	private Class<?> partClass;
	private List<Object> propertyDescriptors;
	private static Iterator<Object> iterator = null;
	
	private boolean correlation;
	
	private boolean primitve;
	
	/**
	 * Creates a new MessagePart
	 * @param inputObject Object holding properties
	 * @param isCorrelation flag defining if this messagePart should be used
	 * as Correlation
	 */
	public MessagePart(Class<?> inputObject, boolean isCorrelation){
		this.propertyDescriptors = new ArrayList<Object>();
		try {
			this.inputObject = inputObject.newInstance();
		}catch (Exception e){
			this.inputObject = null;
		}
		this.partClass = inputObject;
		this.correlation = isCorrelation;
		this.primitve = false;
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
	
	public void addMessagePart(MessagePart part) {
		this.propertyDescriptors.add(part);
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
		for (Object descriptor : this.propertyDescriptors){
			Object val = ((PropertyDescriptor)descriptor).getReadMethod().invoke(input);
			properties.put(((PropertyDescriptor)descriptor).getName(), val);
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
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void setValsForProperties(Object[] values) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (this.propertyDescriptors.size() != values.length){
			throw new IllegalArgumentException();
		}else {
			for (Object value : values){
				this.writeNextProperty(value);
			}
			
			this.resetIterators();
		}
	}
	
	private void resetIterators() {
		if (iterator == null){
			return;
		}else {
			//reset to invoke childs again
			iterator = this.propertyDescriptors.iterator();
		}
		
		//invoke child messageParts
		while(iterator.hasNext()){
			Object prop = iterator.next();
			if (prop instanceof MessagePart){
				((MessagePart)prop).resetIterators();
			}
		}
		
		//remove
		iterator = null;
	}

	private void writeNextProperty(Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchElementException {
		if (iterator == null){
			iterator = this.propertyDescriptors.iterator();
		}
		Object prop = iterator.next();
		if (prop instanceof PropertyDescriptor){
			((PropertyDescriptor)prop).getWriteMethod().invoke(this.inputObject, value);
		}else{
			((MessagePart)prop).writeNextProperty(value);
		}
	}

	public List<Object> getPropertyDescriptors() {
		return propertyDescriptors;
	}
	
	public void setPrimitve(boolean primitve) {
		this.primitve = primitve;
	}
	
	public boolean isPrimitve() {
		return primitve;
	}
	
	public void setInputObject(Object inputObject) {
		this.inputObject = inputObject;
	}
	
}
