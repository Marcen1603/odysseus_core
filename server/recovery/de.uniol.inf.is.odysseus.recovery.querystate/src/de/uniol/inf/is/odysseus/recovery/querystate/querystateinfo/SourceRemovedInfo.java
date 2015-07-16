package de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo;

/**
 * All information needed to recover a source removal event. <br />
 * To write those information to the system log and to read them,
 * {@link #toBase64Binary()} and {@link AbstractQueryStateInfo#fromBase64Binary(String)} can be used.
 * 
 * @author Michael Brand
 *
 */
public class SourceRemovedInfo extends AbstractQueryStateInfo {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 1588955084029013203L;

	/**
	 * The source name.
	 */
	private String mName;

	/**
	 * Gets the source name.
	 * 
	 * @return A string identifying the source.
	 */
	public String getSourceName() {
		return this.mName;
	}

	/**
	 * Sets the source name.
	 * 
	 * @param name
	 *            A string identifying the source.
	 */
	public void setSourceName(String name) {
		this.mName = name;
	}

}