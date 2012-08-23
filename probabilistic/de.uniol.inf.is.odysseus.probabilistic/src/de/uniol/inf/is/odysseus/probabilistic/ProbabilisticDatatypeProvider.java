package de.uniol.inf.is.odysseus.probabilistic;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticDatatypeProvider {
    public static IDataDictionary datadictionary = null;

    protected void bindDataDictionary(final IDataDictionary dd) {
        ProbabilisticDatatypeProvider.datadictionary = dd;
        try {
            ProbabilisticDatatypeProvider.datadictionary.addDatatype(
                    SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI(),
                    SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
            ProbabilisticDatatypeProvider.datadictionary.addDatatype(
                    SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI(),
                    SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
            ProbabilisticDatatypeProvider.datadictionary.addDatatype(
                    SDFProbabilisticDatatype.PROBABILISTIC_MULTIVARIANT_CONTINUOUS_DOUBLE.getURI(),
                    SDFProbabilisticDatatype.PROBABILISTIC_MULTIVARIANT_CONTINUOUS_DOUBLE);
        }
        catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
    }

    protected void unbindDataDictionary(final IDataDictionary dd) {
        try {
            dd.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI());
            dd.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI());
        }
        catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
        ProbabilisticDatatypeProvider.datadictionary = null;
    }
}
