package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.UnionPO;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;

@SuppressWarnings("rawtypes")
public class UnionPOHelper extends AbstractPhysicalOperatorHelper<UnionPO> {

	@Override
	public Class<UnionPO> getOperatorClass() {
		return UnionPO.class;
	}

	@Override
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo) {
		// Since every UnionPO is created the same way via TUnionTIPORule
		// and doesn't have any remarkable features to speak of, return an empty document
		return rootDoc;
	}

	@Override
	SimpleImmutableEntry<Integer, UnionPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		// only return a new UnionPO-instance and its ID
		UnionPO<IStreamObject<ITimeInterval>> result = new UnionPO<IStreamObject<ITimeInterval>>(new TITransferArea<IStreamObject<ITimeInterval>,IStreamObject<ITimeInterval>>());
		return new SimpleImmutableEntry<Integer, UnionPO>(operatorId,result);
	}
}
