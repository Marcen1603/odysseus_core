package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Enumeration;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;

@SuppressWarnings("rawtypes")
public class RelationalProjectPOHelper extends AbstractPhysicalOperatorHelper<RelationalProjectPO> {
	private final static String RESTRICTLIST_TAG = "restrictlist";

	@Override
	public Class<RelationalProjectPO> getOperatorClass() {
		return RelationalProjectPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		RelationalProjectPO<?> rppo = (RelationalProjectPO<?>)o;
		// get the restrictlist and append them to the result
		int[] restrictList = rppo.getRestrictList();
		String listAsString = Arrays.toString(restrictList);
		result.appendChild(result.createElement(RESTRICTLIST_TAG,listAsString));
		return result;
	}

	@Override
	SimpleImmutableEntry<Integer, RelationalProjectPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		int[] restrictList = {};
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(RESTRICTLIST_TAG)) {
				restrictList = Tools.fromStringToIntArray(elem.getTextValue());
			}
		}
		RelationalProjectPO result = new RelationalProjectPO(restrictList);
		
		return new SimpleImmutableEntry<Integer, RelationalProjectPO>(operatorId,result);
	}
}
