package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ACQueryParameter;

public class IsACQueryFunction extends
		AbstractQueryInformationFunction<Boolean> {

	private static final long serialVersionUID = -948427057065250120L;
	
	private final Map<Integer, Boolean> cacheMap = Maps.newHashMap();

	public IsACQueryFunction() {
		super("IsACQuery", SDFDatatype.BOOLEAN);
	}

	@Override
	public Boolean getValue() {
		int queryId = getNumericalInputValue(0).intValue();
		
		if( cacheMap.containsKey(queryId)) {
			return cacheMap.get(queryId);
		}
		
		IPhysicalQuery physicalQuery = getQuery(queryId);
		Object parameter = physicalQuery.getLogicalQuery().getParameter(ACQueryParameter.class.getSimpleName());
		if( parameter != null && parameter instanceof ACQueryParameter ) {
			if( ((ACQueryParameter)parameter).getValue() ) {
				cacheMap.put(queryId, true);
				return true;
			}
		}
		
		cacheMap.put(queryId, false);
		return false;
	}
}
