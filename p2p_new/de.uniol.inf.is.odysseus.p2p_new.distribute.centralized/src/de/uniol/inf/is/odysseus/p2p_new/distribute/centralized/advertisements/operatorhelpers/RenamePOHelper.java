package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.RenamePO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;

@SuppressWarnings("rawtypes")
public class RenamePOHelper extends AbstractPhysicalOperatorHelper<RenamePO> {

	@Override
	public Class<RenamePO> getOperatorClass() {
		return RenamePO.class;
	}

	@Override
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		// Nothing to do really, the outputschema is all that's relevant for the operator
		// and this is already taken care of via the AbstractPhysicalOperatorHelper's generateOperatorStatement-method
		return result;
	}

	@Override
	SimpleImmutableEntry<Integer, RenamePO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		// just create a RenamePO-Instance and return it along with the operatorId
		RenamePO result = new RenamePO();
		return new SimpleImmutableEntry<Integer, RenamePO>(operatorId,result);
	}

}
