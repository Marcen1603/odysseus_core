package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class SelectPOHelper extends AbstractPhysicalOperatorHelper<SelectPO> {

	@Override
	public Class<SelectPO> getOperatorClass() {
		return SelectPO.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o,
			MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		SelectPO<?> spo = (SelectPO<?>)o;
		
		RelationalPredicate pred = (RelationalPredicate)spo.getPredicate();
		List<SDFAttribute> sdfAttributes = pred.getAttributes();
		String predicateExpression = pred.getExpression().getExpressionString();
		RelationalPredicate predicate = new RelationalPredicate(new SDFExpression(predicateExpression, MEP.getInstance()));
		
		return null;
	}

	@Override
	Entry<Integer, SelectPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		return null;
	}

}
