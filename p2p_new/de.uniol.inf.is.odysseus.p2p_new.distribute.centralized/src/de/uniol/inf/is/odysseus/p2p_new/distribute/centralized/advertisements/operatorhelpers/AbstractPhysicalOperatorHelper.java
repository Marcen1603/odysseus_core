package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;

public abstract class AbstractPhysicalOperatorHelper<T extends IPhysicalOperator> implements
		IPhysicalOperatorHelper<T> {
	
	private String OPERATOR_ID_TAG = "operator_id";
	private String OPERATOR_CONTENT_TAG = "operator_content";
	private String OUTPUTSCHEMATA_TAG = "outputSchemata";
	
	private Map<Integer,SDFSchema> schemata = new TreeMap<Integer,SDFSchema>();
	private int operatorId = -1;

	@Override@SuppressWarnings({ "rawtypes", "unchecked" })
	public StructuredDocument<?> generateStatement(IPhysicalOperator o,
			MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		// We need some way of distinguishing the operators, just for the purpose of reconnecting them later. The hashcode will do.
		result.appendChild(result.createElement(OPERATOR_ID_TAG, o.hashCode()));
		// The schema of the operator has to be determined
		if(o.getOutputSchema() != null) {
			result.appendChild(result.createElement(OUTPUTSCHEMATA_TAG,SchemaHelper.createOutputSchemataStatement(o,mimeType)));
		}
		result.appendChild(result.createElement(OPERATOR_CONTENT_TAG,createOperatorSpecificStatement(o,mimeType)));
		
		return result;
	}


	@SuppressWarnings("rawtypes")
	abstract StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType);
	
	@Override
	public SimpleImmutableEntry<Integer,T> createOperatorFromStatement(StructuredDocument<? extends TextElement<?>> doc) {
		Enumeration<? extends TextElement<?>> elems = doc.getChildren();
		TextElement<?> contentElement = null;
		while(elems.hasMoreElements()) {
			TextElement<?> elem = elems.nextElement();
			String tag = elem.getName();
			if (tag.equals(OPERATOR_ID_TAG)) {
				this.operatorId = Integer.parseInt(elem.getTextValue());
			} else if(tag.equals(OUTPUTSCHEMATA_TAG)) {
				this.schemata = SchemaHelper.createSchemataFromStatement(elem);
			} else if(tag.equals(OPERATOR_CONTENT_TAG)) {
				// The creation of the actual operator has to happen last, saving the content for now
				contentElement = elem;
			}
		}
		SimpleImmutableEntry<Integer,T>  result= createSpecificOperatorFromStatement(contentElement,operatorId);
		setSchemataForOperator(result.getValue());
		return result;
	}

	private void setSchemataForOperator(IPhysicalOperator o) {
		for(Entry<Integer, SDFSchema> e : this.schemata.entrySet()) {
			o.setOutputSchema(e.getValue(), e.getKey());
		}
	}
	abstract SimpleImmutableEntry<Integer,T> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId);
}
