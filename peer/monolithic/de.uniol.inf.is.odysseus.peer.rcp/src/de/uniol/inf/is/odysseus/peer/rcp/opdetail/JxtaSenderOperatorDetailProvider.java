package de.uniol.inf.is.odysseus.peer.rcp.opdetail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.rcp.AbstractKeyValueUpdaterProvider;

@SuppressWarnings("rawtypes")
public class JxtaSenderOperatorDetailProvider extends AbstractKeyValueUpdaterProvider<JxtaSenderPO> {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaSenderOperatorDetailProvider.class);
	
	private static final String JXTA_TITLE = "JXTA";

	@Override
	public String getTitle() {
		return JXTA_TITLE;
	}

	@Override
	protected Map<String, String> getKeyValuePairs(JxtaSenderPO operator) {
		Map<String, String> pairs = Maps.newHashMap();
		pairs.put("PipeID", operator.getPipeIDString());
		pairs.put("To Peer", determineDestinationPeerName(operator));
		pairs.put("Uploaded", toByteFormat(operator.getTotalSendByteCount()));
		pairs.put("Upload / s", toByteFormat(operator.getUploadRateBytesPerSecond()));
		pairs.put("Transfertype", operator.getTransmission().getClass().getSimpleName());
		return pairs;
	}
	
	private static String determineDestinationPeerName(JxtaSenderPO operator) {
		if( Strings.isNullOrEmpty(operator.getPeerIDString())) {
			return "<none>";
		}
		
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
