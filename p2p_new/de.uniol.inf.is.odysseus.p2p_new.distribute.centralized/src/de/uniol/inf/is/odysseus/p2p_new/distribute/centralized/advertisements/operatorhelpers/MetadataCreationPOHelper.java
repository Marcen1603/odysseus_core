package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;

@SuppressWarnings("rawtypes")
public class MetadataCreationPOHelper extends AbstractPhysicalOperatorHelper<MetadataCreationPO> {
	private static String METADATA_CREATION_TYPE = "metadata_creation_type";
	
	@Override
	public Class<MetadataCreationPO> getOperatorClass() {
		return MetadataCreationPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo) {
		MetadataCreationPO<?,?> mdcpo = (MetadataCreationPO<?,?>)o;
		toAppendTo.appendChild(rootDoc.createElement(METADATA_CREATION_TYPE,mdcpo.getType().toString()));
		return rootDoc;
	}

	@SuppressWarnings("unchecked")
	@Override
	SimpleImmutableEntry<Integer, MetadataCreationPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		Class<?> type = null;
		String typeString = "";
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(METADATA_CREATION_TYPE)) {
				typeString = elem.getTextValue();
			}
		}
		if(typeString.equals(ITimeInterval.class.toString())) {
			type = ITimeInterval.class;
		} else if (typeString.equals(ILatency.class.toString())) {
			type = ILatency.class;
		}

		MetadataCreationPO result = new MetadataCreationPO(type);
		return new SimpleImmutableEntry<Integer, MetadataCreationPO>(operatorId,result);
	}
}
