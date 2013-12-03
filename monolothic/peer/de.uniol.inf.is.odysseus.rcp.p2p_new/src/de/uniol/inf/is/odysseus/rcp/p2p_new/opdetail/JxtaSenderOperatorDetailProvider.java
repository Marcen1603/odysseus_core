package de.uniol.inf.is.odysseus.rcp.p2p_new.opdetail;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.rcp.server.AbstractKeyValueUpdaterProvider;

@SuppressWarnings("rawtypes")
public class JxtaSenderOperatorDetailProvider extends AbstractKeyValueUpdaterProvider<JxtaSenderPO> {

	private static final String JXTA_TITLE = "JXTA";

	@Override
	public String getTitle() {
		return JXTA_TITLE;
	}

	@Override
	protected Map<String, String> getKeyValuePairs(JxtaSenderPO operator) {
		Map<String, String> pairs = Maps.newHashMap();
		pairs.put("PipeID", operator.getPipeAdvertisement().getPipeID().toString());
		pairs.put("Connections", String.valueOf(operator.getConnectionCount()));
		pairs.put("Uploaded", toByteFormat(operator.getSentByteCount()));
		pairs.put("Upload / s", toByteFormat(operator.getSentByteDataRate()));
		pairs.put("Transfertype", operator.getConnectionType().getName());
		return pairs;
	}

	@Override
	protected Class<? extends JxtaSenderPO> getOperatorType() {
		return JxtaSenderPO.class;
	}

	private static String toByteFormat(double receivedByteDataRate) {
		double rawDataRate = receivedByteDataRate;
		final String[] units = new String[] { "Bytes", "KB", "MB", "GB" };

		int unitIndex = -1;
		for (int i = 0; i < units.length; i++) {
			if (rawDataRate < 1024.0) {
				unitIndex = i;
				break;
			}
			rawDataRate /= 1024.0;
		}
		if (unitIndex == -1) {
			unitIndex = units.length - 1;
		}

		return String.format("%-10.1f", rawDataRate) + " " + units[unitIndex];
	}

}
