package de.uniol.inf.is.odysseus.broker.sensors.generator;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Represents an interface for a stream type. 
 * 
 * @author Dennis Geesen
 */
public interface IStreamType{
	
	/**
	 * Gets the schema of the stream.
	 *
	 * @return the schema
	 */
	public SDFAttributeList getSchema();
	
	/**
	 * Gets the next tuple from this stream.
	 *
	 * @param currentTime the current time
	 * @return the next tuple
	 */
	public RelationalTuple<ITimeInterval> getNextTuple(long currentTime);
	
	/**
	 * Gets the time to wait between to tuples in milliseconds.
	 *
	 * @return the time to wait
	 */
	public long getWaitingMillis();
	
	/**
	 * Gets the max count of items this stream will provide.
	 *
	 * @return the count of items
	 */
	public int getMaxItems();
	
	/**
	 * Checks if punctuation is enabled.
	 *
	 * @return true, if punctuation is enabled
	 */
	public boolean isPunctuationEnabled();
	
	/**
	 * Gets the name of the stream
	 *
	 * @return the name
	 */
	public String getName();	
	
	/**
	 * Gets the next punctuation.
	 *
	 * @param currentTime the current time
	 * @return the next punctuation
	 */
	public long getNextPunctuation(long currentTime);			
}
