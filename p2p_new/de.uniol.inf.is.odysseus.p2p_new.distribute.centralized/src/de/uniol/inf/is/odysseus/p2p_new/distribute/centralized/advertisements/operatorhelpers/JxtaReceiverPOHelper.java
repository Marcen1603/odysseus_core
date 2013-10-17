package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;

@SuppressWarnings("rawtypes")
public class JxtaReceiverPOHelper extends AbstractPhysicalOperatorHelper<JxtaReceiverPO> {
	private final static String PIPEID_TAG = "pipeid";
	private final static String JXTA_STARTUP_TAG = "startupJxtaConnections";

	@Override
	public Class<JxtaReceiverPO> getOperatorClass() {
		return JxtaReceiverPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		JxtaReceiverPO<?> jpo = (JxtaReceiverPO<?>)o;		
		result.appendChild(result.createElement(PIPEID_TAG, jpo.getPipeID().toURI().toString()));
		result.appendChild(result.createElement(JXTA_STARTUP_TAG, Boolean.toString(this.jxtaStartup)));
		return result;
	}

	/**
	 * We have to use different instantiations of the operators in this case,
	 * since we don't need the connections and PipeAdvertisements at the centralised distributor
	 * but rather at the designated peers.
	 */
	@Override
	SimpleImmutableEntry<Integer, JxtaReceiverPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		// we have to use the schema for the instantiation, it is already retrieved at this point
		SDFSchema schema = this.schemata.get(0);
		String pipeIDString = "";
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(PIPEID_TAG)) {
				pipeIDString = elem.getTextValue();
			} else if(elem.getName().equals(JXTA_STARTUP_TAG)) {
				this.jxtaStartup = Boolean.parseBoolean(elem.getTextValue());
			}
		}
		JxtaReceiverPO result = null;
		if(this.jxtaStartup) {
			// instantiate the operator the traditional way including the creation of a PipeAdvertisement etc.
			JxtaReceiverAO rao = new JxtaReceiverAO();
			rao.setOutputSchema(schema);
			rao.setPipeID(pipeIDString);
			result = new JxtaReceiverPO(rao);
		} else {
			// instantiate an operator without connecting anything on a jxta-level
			result = new JxtaReceiverPO(pipeIDString, schema);
		}

		return new SimpleImmutableEntry<Integer, JxtaReceiverPO>(operatorId,result);
	}

}
