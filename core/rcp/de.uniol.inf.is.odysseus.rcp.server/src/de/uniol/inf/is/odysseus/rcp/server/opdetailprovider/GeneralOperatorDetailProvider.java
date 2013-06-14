package de.uniol.inf.is.odysseus.rcp.server.opdetailprovider;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;

public class GeneralOperatorDetailProvider extends AbstractKeyValueGeneralProvider {

	private static final String TAB_TITLE = "General";
	
	@Override
	protected Map<String, String> getKeyValuePairs(IPhysicalOperator operator) {
		Map<String, String> map = Maps.newHashMap();
		
		map.put("Name", operator.getName());
		map.put("Class", operator.getClass().getSimpleName());
		map.put("Hash", String.valueOf(operator.hashCode()));
		map.put("Owners", determineOwners(operator));
		map.put("Open", operator.isOpen() ? "true" : "false");
		map.put("Type", determineType(operator));
		
		for( String dataName : operator.getProvidedMonitoringData() ) {
			IMonitoringData<?> data = operator.getMonitoringData(dataName);
			map.put(dataName, data.getValue().toString());
		}
		
		return map;
	}

	@Override
	public String getTitle() {
		return TAB_TITLE;
	}

	private static String determineType(IPhysicalOperator operator) {
		if( operator.isPipe() ) {
			return "Pipe";
		} else if( operator.isSink() ) {
			return "Sink";
		}
		return "Source";
	}

	private static String determineOwners(IPhysicalOperator operator) {
		StringBuilder sb = new StringBuilder();
		List<IOperatorOwner> owners = operator.getOwner();
		for( int i = 0; i < owners.size(); i++ ) {
			sb.append(owners.get(i).getID());
			if( i < owners.size() - 1 ) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
}
