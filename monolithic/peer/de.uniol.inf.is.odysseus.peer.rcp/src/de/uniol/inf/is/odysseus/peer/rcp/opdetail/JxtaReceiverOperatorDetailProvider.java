package de.uniol.inf.is.odysseus.peer.rcp.opdetail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.rcp.AbstractKeyValueUpdaterProvider;

@SuppressWarnings("rawtypes")
public class JxtaReceiverOperatorDetailProvider extends AbstractKeyValueUpdaterProvider<JxtaReceiverPO> {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaReceiverOperatorDetailProvider.class);
	
	private static final String JXTA_TITLE = "JXTA";

	@Override
	protected Map<String, String> getKeyValuePairs(JxtaReceiverPO operator) {
		Map<String, String> pairs = Maps.newHashMap();
		pairs.put("PipeID", operator.getPipeIDString());
		pairs.put("From Peer", determineDestinationPeerName(operator));
		pairs.put("Downloaded", toByteFormat(operator.getTotalReceivedByteCount()));
		pairs.put("Download / s", toByteFormat(operator.getDownloadRateBytesPerSecond()));
		pairs.put("Transfertype", operator.getTransmission().getClass().getSimpleName());
		return pairs;
	}

	private static String determineDestinationPeerName(JxtaReceiverPO operator) {
		return RCPP2PNewPlugIn.getP2PDictionary().getRemotePeerName(toPeerID(operator.getPeerIDString()));
	}

	protected static PeerID toPeerID(String peerIDString) {
		try {
			final URI id = new URI(peerIDString);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not get id from peerIDString {}", peerIDString, ex);
			return null;
		}
	}

	@Override
	public String getTitle() {
		return JXTA_TITLE;
	}

	@Override
	protected Class<? extends JxtaReceiverPO> getOperatorType() {
		return JxtaReceiverPO.class;
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
