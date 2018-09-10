package de.uniol.inf.is.odysseus.timeseries.imputation;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

/**
 * Imputation is used to detect and replace missing data.
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
	 * @param punctuation
	 *            to get time progress This implies, that missing data can be
	 *            detect by time progress.
	 * 
	 * @return a List with data to impute, which replace missing data.
	 */
	public List<T> getImputationDataByPunctuation(IPunctuation punctuation);

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
