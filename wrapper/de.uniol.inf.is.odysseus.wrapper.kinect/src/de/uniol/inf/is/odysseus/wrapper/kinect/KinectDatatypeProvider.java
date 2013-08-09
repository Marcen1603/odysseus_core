package de.uniol.inf.is.odysseus.wrapper.kinect;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.wrapper.kinect.sdf.schema.SDFKinectDatatype;

/**
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectDatatypeProvider {
    /** Holds a list of registered data types. */
    private static IDataDictionary datadictionary = null;

    /**
     * Binds data types to the passed dictionary.
     * @param dd
     * dictionary to bind data types to.
     */
    protected void bindDataDictionary(IDataDictionary dd) {
        KinectDatatypeProvider.datadictionary = dd;
        try {
            KinectDatatypeProvider.datadictionary.addDatatype(
                    SDFKinectDatatype.KINECT_COLOR_MAP.getURI(),
                    SDFKinectDatatype.KINECT_COLOR_MAP);
            KinectDatatypeProvider.datadictionary.addDatatype(
                    SDFKinectDatatype.KINECT_DEPTH_MAP.getURI(),
                    SDFKinectDatatype.KINECT_DEPTH_MAP);
            KinectDatatypeProvider.datadictionary.addDatatype(
                    SDFKinectDatatype.KINECT_POINT_CLOUD.getURI(),
                    SDFKinectDatatype.KINECT_POINT_CLOUD);
            KinectDatatypeProvider.datadictionary.addDatatype(
                    SDFKinectDatatype.KINECT_SKELETON_MAP.getURI(),
                    SDFKinectDatatype.KINECT_SKELETON_MAP);
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
            dd.removeDatatype(SDFKinectDatatype.KINECT_COLOR_MAP.getURI());
            dd.removeDatatype(SDFKinectDatatype.KINECT_DEPTH_MAP.getURI());
            dd.removeDatatype(SDFKinectDatatype.KINECT_POINT_CLOUD.getURI());
            dd.removeDatatype(SDFKinectDatatype.KINECT_SKELETON_MAP.getURI());
        } catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
        KinectDatatypeProvider.datadictionary = null;
    }
}
