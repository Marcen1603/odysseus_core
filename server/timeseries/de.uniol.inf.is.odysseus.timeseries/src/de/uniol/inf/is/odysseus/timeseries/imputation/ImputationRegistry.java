package de.uniol.inf.is.odysseus.timeseries.imputation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * 
 * This is the registry of possible imputation strategies.
 * 
 * @author Christoph Schröer
 *
 * @param <T>
 *            Type of data object, on which to operate
 * @param <M>
 *            Type of Metadata of the data object.
 */
public class ImputationRegistry<T extends Tuple<M>, M extends ITimeInterval> {

	public static ImputationRegistry<? extends Tuple<?>, ? extends ITimeInterval> instance = null;

	private List<IImputation<T, M>> imputationStrategies = new ArrayList<>();

	public ImputationRegistry() {
	}

	@SuppressWarnings("unchecked")
	public static synchronized <T extends Tuple<M>, M extends ITimeInterval> ImputationRegistry<T, M> getInstance() {
		if (instance == null) {
			instance = new ImputationRegistry<T, M>();
		}
		return (ImputationRegistry<T, M>) instance;

	}

	public void addImputation(IImputation<T, M> imputationStrategy) {
		this.imputationStrategies.add(imputationStrategy);
	}

	public void removeImputation(IImputation<T, M> imputationStrategy) {
		this.imputationStrategies.remove(imputationStrategy);
	}

	public List<String> getImputationStrategies() {
		List<String> list = new ArrayList<>();
		for (IImputation<T, M> impuation : this.imputationStrategies) {
			list.add(impuation.getName());
		}
		Collections.sort(list);
		return list;
	}

	public IImputation<T, M> createInstance(String imputationStrategy, Map<String, String> optionsMap) {
		for (IImputation<T, M> imputation : this.imputationStrategies) {
			if (imputation.getName().equalsIgnoreCase(imputationStrategy)) {
				return imputation.createInstance(optionsMap);
			}
		}
		return null;
	}
}
