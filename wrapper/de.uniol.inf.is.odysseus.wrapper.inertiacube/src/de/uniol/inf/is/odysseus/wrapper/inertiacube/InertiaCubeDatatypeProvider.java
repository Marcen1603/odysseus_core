package de.uniol.inf.is.odysseus.wrapper.inertiacube;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.sdf.schema.SDFInertiaCubeDatatype;

public class InertiaCubeDatatypeProvider {
	/** Holds a list of registered data types. */
    private static IDataDictionary datadictionary = null;

    /**
     * Binds data types to the passed dictionary.
     * @param dd
     * dictionary to bind data types to.
     */
    protected void bindDataDictionary(IDataDictionary dd) {
    	InertiaCubeDatatypeProvider.datadictionary = dd;
        try {
        	InertiaCubeDatatypeProvider.datadictionary.addDatatype(
                    SDFInertiaCubeDatatype.YAW_PITCH_ROLL.getURI(),
                    SDFInertiaCubeDatatype.YAW_PITCH_ROLL);
        } catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes data types from the passed dictionary.
     * @param dd
     * dictionary to remove data types from.
     */
    protected void unbindDataDictionary(IDataDictionary dd) {
        try {
            dd.removeDatatype(SDFInertiaCubeDatatype.YAW_PITCH_ROLL.getURI());
        } catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
        InertiaCubeDatatypeProvider.datadictionary = null;
    }
}
