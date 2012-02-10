package de.uniol.inf.is.odysseus.spatial.grid;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 */
public class GridDatatypeProvider {
	private static IDataDictionary datadictionary = null;

	public void bindDictionary(IDataDictionary dataDictionary)
			throws PlanManagementException {

		datadictionary = dataDictionary;
		if (datadictionary.getDatatype(SDFGridDatatype.GRID.getURI()) == null) {
			datadictionary.addDatatype(SDFGridDatatype.GRID.getURI(),
					SDFGridDatatype.GRID);
		}
	}

	public void unbindDictionary(IDataDictionary dd) {
		if (datadictionary.getDatatype(SDFGridDatatype.GRID.getURI()) != null) {
			datadictionary.removeDatatype(SDFGridDatatype.GRID.getURI());
		}
		datadictionary = null;
	}
}
