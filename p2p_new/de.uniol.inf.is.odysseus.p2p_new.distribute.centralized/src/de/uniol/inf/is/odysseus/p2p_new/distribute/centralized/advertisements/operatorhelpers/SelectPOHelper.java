package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;

@SuppressWarnings("rawtypes")
public class SelectPOHelper extends AbstractPhysicalOperatorHelper<SelectPO> {
	private final static String PREDICATE_TAG = "predicate_tag";

	@Override
	public Class<SelectPO> getOperatorClass() {
		return SelectPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo) {
		SelectPO<?> spo = (SelectPO<?>)o;
		IPredicate pred = spo.getPredicate();
		Element predicateElement = rootDoc.createElement(PREDICATE_TAG);
		toAppendTo.appendChild(predicateElement);
		PredicateHelper.generatePredicateStatement(pred, mimeType, rootDoc, predicateElement,spo);
		System.out.println(rootDoc.toString());
		return rootDoc;
	}

	@SuppressWarnings("unchecked")
	@Override
	SimpleImmutableEntry<Integer, SelectPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		IPredicate pred = null;
		
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(PREDICATE_TAG)) {
				pred = PredicateHelper.createPredicateFromStatement(elem);
			}
		}
		return new SimpleImmutableEntry<Integer, SelectPO>(operatorId,new SelectPO(pred));
	}

}
