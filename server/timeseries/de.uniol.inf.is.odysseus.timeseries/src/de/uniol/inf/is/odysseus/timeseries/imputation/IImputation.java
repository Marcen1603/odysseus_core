package de.uniol.inf.is.odysseus.timeseries.imputation;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * 
 * @author Christoph Schröer
 *
 */
public interface IImputation<T extends IStreamObject<M>, M extends IMetaAttribute> {

	/**
	 * @param newElement
	 * 
	 * @return a List with data to impute, which replace missing data.
	 */
	public List<T> getImputationData(T newElement);

	/**
	 * name of the imputation strategy
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Factory-method to create instance
	 * 
	 * @return
	 */
	public IImputation<T, M> createInstance(Map<String, String> optionsMap);

}
