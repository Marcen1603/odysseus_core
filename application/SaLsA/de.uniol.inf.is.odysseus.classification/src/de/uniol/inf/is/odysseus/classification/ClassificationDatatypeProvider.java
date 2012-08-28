package de.uniol.inf.is.odysseus.classification;

import de.uniol.inf.is.odysseus.classification.sdf.schema.SDFClassificationDataype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;

/**
 * @author Alexander Funk <alexander.funk@uni-oldenburg.de>
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClassificationDatatypeProvider {
    public static IDataDictionary datadictionary = null;

    protected void bindDataDictionary(final IDataDictionary dd) {
        ClassificationDatatypeProvider.datadictionary = dd;
        try {
            ClassificationDatatypeProvider.datadictionary.addDatatype(SDFClassificationDataype.CLASSIFICATION.getURI(),
                    SDFClassificationDataype.CLASSIFICATION);
            ClassificationDatatypeProvider.datadictionary.addDatatype(
                    SDFClassificationDataype.CLASSIFY_OBJECT.getURI(), SDFClassificationDataype.CLASSIFY_OBJECT);
            ClassificationDatatypeProvider.datadictionary.addDatatype(
                    SDFClassificationDataype.CLASSIFY_OBJECT_LIST.getURI(),
                    SDFClassificationDataype.CLASSIFY_OBJECT_LIST);
        }
        catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
    }

    protected void unbindDataDictionary(final IDataDictionary dd) {
        try {
            dd.removeDatatype(SDFClassificationDataype.CLASSIFY_OBJECT_LIST.getURI());
            dd.removeDatatype(SDFClassificationDataype.CLASSIFY_OBJECT.getURI());
            dd.removeDatatype(SDFClassificationDataype.CLASSIFICATION.getURI());
        }
        catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
        ClassificationDatatypeProvider.datadictionary = null;
    }
}
