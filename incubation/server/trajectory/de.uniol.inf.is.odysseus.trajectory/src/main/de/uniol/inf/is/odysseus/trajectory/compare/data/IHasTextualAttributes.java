package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.Map;

/**
 * An object that has set of textual attributes. Textual Attributes are 
 * stored in a <tt>Map</tt> where the <i>key</i> of an entry is a 
 * <tt>String</tt> and represents the attribute's <i>name</i>. The values
 * of the entries store the value for the associated attribute names.
 * 
 * @author marcus
 *
 */
public interface IHasTextualAttributes {

	/**
	 * Returns the the <tt>Map</tt> which stores the attribute names and
	 * their respective values.
	 * 
	 * @return the the <tt>Map</tt> which stores the attribute names and
	 *         their respective values
	 */
	public Map<String, String> getTextualAttributes();
	
	/**
	 * Returns the number of textual attributes stored in this 
	 * <tt>object</TT>.
	 * 
	 * @return the number of textual attributes
	 */
	public int numAttributes();
}
