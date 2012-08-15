package de.uniol.inf.is.odysseus.probabilistic;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class ProbabilisticDatatypeProvider {
	public static IDataDictionary datadictionary = null;

	protected void bindDataDictionary(IDataDictionary dd) {
		datadictionary = dd;
		try {
			datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
			datadictionary.addDatatype(
					SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE
							.getURI(),
					SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
		} catch (DataDictionaryException e) {
			e.printStackTrace();
		}
	}

	protected void unbindDataDictionary(IDataDictionary dd) {
		try {
			dd.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE
					.getURI());
			dd.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE
					.getURI());
		} catch (DataDictionaryException e) {
			e.printStackTrace();
		}
		datadictionary = null;
	}
}
