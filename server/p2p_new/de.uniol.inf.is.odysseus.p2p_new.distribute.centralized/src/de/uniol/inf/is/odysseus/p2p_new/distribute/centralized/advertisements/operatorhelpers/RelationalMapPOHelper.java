package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalNoGroupProcessor;

@SuppressWarnings("rawtypes")
public class RelationalMapPOHelper extends AbstractPhysicalOperatorHelper<RelationalMapPO> {
	private final static String INPUTSCHEMA_TAG = "input_schema";
	
	@Override
	public Class<RelationalMapPO> getOperatorClass() {
		return RelationalMapPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo) {
		RelationalMapPO<?> rmpo = (RelationalMapPO<?>)o;
		
		Element schemaElement = rootDoc.createElement(INPUTSCHEMA_TAG);
		toAppendTo.appendChild(schemaElement);
		SchemaHelper.createOutputSchemaStatement(rmpo.getInputSchema(), mimeType, rootDoc, schemaElement);


		SDFExpression[] expressions = rmpo.getExpressions();
		
		if(expressions != null && expressions.length > 0) {
			for(int i = 0; i < expressions.length; i++) {
				toAppendTo.appendChild(rootDoc.createElement(Integer.toString(i),expressions[i].getExpressionString()));
			}	
		}
		return rootDoc;
	}

	@Override
	SimpleImmutableEntry<Integer, RelationalMapPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		SDFSchema inputSchema = null;
		Map<Integer,SDFExpression> expressionsMap = new TreeMap<Integer,SDFExpression>();
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(INPUTSCHEMA_TAG)) {
				SchemaHelper.createSchemaFromStatement(elem);
			// if it's not the inputschema, it's an expression with the tag specifying its index in the expressions-array
			} else {
				expressionsMap.put(Integer.parseInt(elem.getName()),new SDFExpression(elem.getTextValue(),MEP.getInstance()));
			}
		}
		SDFExpression[] expressions = new SDFExpression[expressionsMap.size()];
		for(Entry<Integer, SDFExpression> e : expressionsMap.entrySet()) {
			expressions[e.getKey()] = e.getValue();
		}
		@SuppressWarnings("unchecked")
		RelationalMapPO result = new RelationalMapPO<IMetaAttribute>(inputSchema, expressions, false, false, RelationalNoGroupProcessor.getInstance());
		return new SimpleImmutableEntry<Integer, RelationalMapPO>(operatorId,result);
	}

}
