package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IValidTimes extends IMetaAttribute {

	public List<IValidTime> getValidTimes();

	/**
	 * Adds a valid time to the list without changing the existing values.
	 * 
	 * @param validTime
	 *            The valid to add
	 */
	public void addValidTime(IValidTime validTime);

	/**
	 * Removes all valid times from the list.
	 */
	public void clear();

}
