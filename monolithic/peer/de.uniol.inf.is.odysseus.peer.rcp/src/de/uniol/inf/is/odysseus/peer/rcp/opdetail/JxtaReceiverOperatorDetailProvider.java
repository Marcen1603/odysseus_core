package de.uniol.inf.is.odysseus.peer.rcp.opdetail;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.rcp.AbstractKeyValueUpdaterProvider;

@SuppressWarnings("rawtypes")
public class JxtaReceiverOperatorDetailProvider extends AbstractKeyValueUpdaterProvider<JxtaReceiverPO> {

	private static final String JXTA_TITLE = "JXTA";
	
	@Override
	protected Map<String, String> getKeyValuePairs(JxtaReceiverPO operator) {
		Map<String, String> pairs = Maps.newHashMap();
		pairs.put("PipeID", "<not implemented yet>");
		pairs.put("Peer", "<not implemented yet>");
		pairs.put("Downloaded", "<not implemented yet>");
		pairs.put("Download / s", "<not implemented yet>");
		pairs.put("Transfertype", "<not implemented yet>");
		return pairs;
	}
	
	@Override
	public String getTitle() {
		return JXTA_TITLE;
	}

	@Override
	protected Class<? extends JxtaReceiverPO> getOperatorType() {
		return JxtaReceiverPO.class;
	}
	
//	private static String toByteFormat(double receivedByteDataRate) {
//		double rawDataRate = receivedByteDataRate;
//		final String[] units = new String[] { "Bytes", "KB", "MB", "GB" };
//		
//		int unitIndex = -1;
//		for( int i = 0; i < units.length ; i++) {
//			if( rawDataRate < 1024.0 ) {
//				unitIndex = i;
//				break;
//			} 
//			rawDataRate /= 1024.0;
//		}
//		if( unitIndex == -1 ) {
//			unitIndex = units.length - 1;
//		}
//		
//		return String.format("%-10.1f", rawDataRate) + " " + units[unitIndex];
//	}

}
