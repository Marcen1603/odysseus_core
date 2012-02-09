package de.uniol.inf.is.odysseus.spatial;

import de.uniol.inf.is.odysseus.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 */
public class GridDatatypeProvider {
	private static AbstractDataDictionary datadictionary = null;

	public void bindDictionary(IDataDictionary dd) throws PlanManagementException {
		if (dd instanceof AbstractDataDictionary){
			datadictionary = (AbstractDataDictionary) dd;
			datadictionary.addDatatype(SDFGridDatatype.GRID.getURI(),
					SDFGridDatatype.GRID);
		}
	}

	public void unbindDictionary(IDataDictionary dd) {
		datadictionary = null;
	}
}
