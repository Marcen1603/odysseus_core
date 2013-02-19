package de.uniol.inf.is.odysseus.fastflowerdelivery.json;

import com.google.gson.GsonBuilder;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;

/**
 * Abstract base type of any result of a web service.
 * Returns a JSON representation of the instance of the derived class
 * if the toString method is called.
 * NOTE: all derived classes need a default constructor to be serialized by gson.
 * 
 * @author Weert Stamm
 * @version 1.0
 */
abstract public class JsonData {

	/**
	 * Serializes this object into JSON format.
	 * @return a JSON representation of this instance
	 */
	public String toJson() {
		return  new GsonBuilder().registerTypeAdapter(AbstractEvent.class, new JsonAbstractEventAdapter()).create().toJson(this);
	}
	
	/**
	 * Returns the JSON representation of this object
	 */
	@Override
	public String toString() {
		return toJson();
	}
	
}
