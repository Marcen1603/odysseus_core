package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;

@SuppressWarnings("rawtypes")
public class JxtaSenderPOHelper extends AbstractPhysicalOperatorHelper<JxtaSenderPO> {
	private final static String PIPEID_TAG = "pipeid";
	private final static String JXTA_STARTUP_TAG = "startupJxtaConnections";
	private final static String JXTA_USEUDP_TAG = "useUdp";

	@Override
	public Class<JxtaSenderPO> getOperatorClass() {
		return JxtaSenderPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		JxtaSenderPO<?> jpo = (JxtaSenderPO<?>)o;		
		result.appendChild(result.createElement(PIPEID_TAG, jpo.getPipeID().toURI().toString()));
		result.appendChild(result.createElement(JXTA_STARTUP_TAG, this.jxtaStartup));
		result.appendChild(result.createElement(JXTA_USEUDP_TAG, jpo.isUseUDP()));
		return result;
	}

	/**
	 * We have to use different instantiations of the operators in this case,
	 * since we don't need the connections and PipeAdvertisements at the centralised distributor
	 * but rather at the designated peers.
	 */
	@Override
	SimpleImmutableEntry<Integer, JxtaSenderPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		
		String pipeIDString = "";
		boolean useUDP = false;
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(PIPEID_TAG)) {
				pipeIDString = elem.getTextValue();
			} else if(elem.getName().equals(JXTA_STARTUP_TAG)) {
				this.jxtaStartup = Boolean.parseBoolean(elem.getTextValue());
			} else if(elem.getName().equals(JXTA_USEUDP_TAG)) {
				useUDP = Boolean.parseBoolean(elem.getTextValue());
			}
		}
		JxtaSenderPO result = null;
		if(this.jxtaStartup) {
			// instantiate the operator the traditional way including the creation of a PipeAdvertisement etc.
			JxtaSenderAO sao = new JxtaSenderAO();
			sao.setPipeID(pipeIDString);
			sao.setUseUDP(useUDP);
			result = new JxtaSenderPO(sao);
		} else {
			// instantiate an operator without connecting anything on a jxta-level
			result = new JxtaSenderPO(pipeIDString, useUDP);
		}

		return new SimpleImmutableEntry<Integer, JxtaSenderPO>(operatorId,result);
	}

}
