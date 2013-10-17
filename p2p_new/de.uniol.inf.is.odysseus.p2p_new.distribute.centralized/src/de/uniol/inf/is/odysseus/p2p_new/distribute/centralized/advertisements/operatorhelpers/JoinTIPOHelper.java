package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;
import de.uniol.inf.is.odysseus.persistentqueries.PersistentTransferArea;

@SuppressWarnings("rawtypes")
public class JoinTIPOHelper extends AbstractPhysicalOperatorHelper<JoinTIPO> {
	private final static String PREDICATE_TAG = "predicate_tag";
	private final static String JOINTIPONAME_TAG = "jointipo_name";
	private final static String TRANSFER_FUNCTION_TAG = "transferfunction";
	private final static String PERSISTENTTRANSFERAREA_TAG = "persistenttransferarea";
	private final static String TITRANSFERAREA_TAG = "titransferarea";

	@Override
	public Class<JoinTIPO> getOperatorClass() {
		return JoinTIPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		JoinTIPO<?,?> jpo = (JoinTIPO<?,?>)o;
		// get the predicate and append the info for it under the correspondent tag
		IPredicate pred = jpo.getPredicate();
		result.appendChild(result.createElement(PREDICATE_TAG,PredicateHelper.generatePredicateStatement(pred, mimeType).toString()));
		// append the name of the join
		result.appendChild(result.createElement(JOINTIPONAME_TAG,jpo.getName()));
		// Consider the type of the sweep-areas and append this information under the transferfunction-tag
		String transferfunction = "";
		if(jpo.getTransferFunction() instanceof PersistentTransferArea) {
			transferfunction = PERSISTENTTRANSFERAREA_TAG;
		} else {
			transferfunction = TITRANSFERAREA_TAG;
		}
		result.appendChild(result.createElement(TRANSFER_FUNCTION_TAG,transferfunction));
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	SimpleImmutableEntry<Integer, JoinTIPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		IPredicate pred = null;
		JoinTIPO result = new JoinTIPO();
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(PREDICATE_TAG)) {
				pred = PredicateHelper.createPredicateFromStatement(elem);
				result.setJoinPredicate(pred);
			} else if(elem.getName().equals(JOINTIPONAME_TAG)) {
				result.setName(elem.getTextValue());
			} else if(elem.getName().equals(TRANSFER_FUNCTION_TAG)) {
				if(elem.getTextValue().equals(PERSISTENTTRANSFERAREA_TAG)) {
					result.setTransferFunction(new PersistentTransferArea());
				} else if(elem.getTextValue().equals(TITRANSFERAREA_TAG)) {
					result.setTransferFunction(new TITransferArea());
				}
			}
		}
		//Merge- and creation-functions seem to be fixed according to {@link de.uniol.inf.is.odysseus.interval.transform.join.TJoinAORule}
		result.setMetadataMerge(new CombinedMergeFunction());
		result.setCreationFunction(new DefaultTIDummyDataCreation());
		
		return new SimpleImmutableEntry<Integer, JoinTIPO>(operatorId,result);
	}

}
