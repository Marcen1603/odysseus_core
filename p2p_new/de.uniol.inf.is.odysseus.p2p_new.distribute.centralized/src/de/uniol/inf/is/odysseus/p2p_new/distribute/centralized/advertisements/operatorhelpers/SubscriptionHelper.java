package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;

public class SubscriptionHelper {
	private static String SOURCESUBSCRIPTIONS_TAG = "source_subscriptions";
	private static String SINKSUBSCRIPTIONS_TAG = "sink_subscriptions";
	private static String SUBSCRIPTION_TARGET = "subscription_target";
	private static String SUBSCRIPTION_SOURCEOUTPORT = "subscription_sourceoutport";
	private static String SUBSCRIPTION_SINKINPORT = "subscription_sinkinport";
	private static String SUBSCRIPTION_SCHEMA = "subscription_schema";
	private static String SUBSCRIPTION_ORIGIN = "subscription_origin";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument generateSubscriptionStatement(MimeMediaType mimeType, Collection<IPhysicalOperator> ops) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		Map<Integer, Collection<ISubscription>> sourceSubs = new TreeMap<Integer, Collection<ISubscription>>();
		Map<Integer, Collection<ISubscription>> sinkSubs = new TreeMap<Integer,Collection<ISubscription>>();
		for(IPhysicalOperator o : ops) {
			if(o.isSource()) {
				sinkSubs.put(o.hashCode(),((ISource)o).getSubscriptions());
			}
			if(o.isSink()) {
				sourceSubs.put(o.hashCode(),((ISink)o).getSubscribedToSource());
			}
			if(!sourceSubs.isEmpty()) {
				result.appendChild(result.createElement(SOURCESUBSCRIPTIONS_TAG,createSubscriptionStatements(sourceSubs,mimeType)));
			}
			if(!sinkSubs.isEmpty()) {
				result.appendChild(result.createElement(SINKSUBSCRIPTIONS_TAG,createSubscriptionStatements(sourceSubs,mimeType)));
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static StructuredDocument createSubscriptionStatements(Map<Integer,Collection<ISubscription>> subs, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		for(Entry<Integer,Collection<ISubscription>> entry : subs.entrySet()) {
			int originatingOperatorId = entry.getKey();
			for(ISubscription sub : entry.getValue()) {
				// use the hash of the current operator and of the target as IDs. If the operators are transferred,
				// this hash will be used for its id as well and associated with the new operator during reconstruction
				result.appendChild(result.createElement(SUBSCRIPTION_ORIGIN, originatingOperatorId));
				result.appendChild(result.createElement(SUBSCRIPTION_TARGET, sub.getTarget().hashCode()));
				result.appendChild(result.createElement(SUBSCRIPTION_SINKINPORT, sub.getSinkInPort()));
				result.appendChild(result.createElement(SUBSCRIPTION_SOURCEOUTPORT, sub.getSourceOutPort()));
				result.appendChild(result.createElement(SUBSCRIPTION_SCHEMA, SchemaHelper.createOutputSchemaStatement(sub.getSchema(), mimeType)));
			}
		}
		return null;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static void reconnectOperators(Map<Integer, IPhysicalOperator> newOperators, TextElement<?> statement) {
		Enumeration<? extends TextElement<?>> elems = statement.getChildren();
		
		while(elems.hasMoreElements()) {
			TextElement<?> elem = elems.nextElement();
			Enumeration<? extends TextElement<?>> subscriptionDetails = elem.getChildren();
			int subOrigin =-1;
			int subTarget =-1;
			int sinkInPort = -1;
			int sourceOutPort = -1;
			SDFSchema schema = null;
			
			while(subscriptionDetails.hasMoreElements()) {
				TextElement<?> subDetailElem = subscriptionDetails.nextElement();
				if(subDetailElem.getName().equals(SUBSCRIPTION_ORIGIN)) {
					subOrigin = Integer.parseInt(subDetailElem.getTextValue());
				} else if(subDetailElem.getName().equals(SUBSCRIPTION_TARGET)) {
					subTarget = Integer.parseInt(subDetailElem.getTextValue());
				} else if(subDetailElem.getName().equals(SUBSCRIPTION_SINKINPORT)) {
					sinkInPort = Integer.parseInt(subDetailElem.getTextValue());
				} else if(subDetailElem.getName().equals(SUBSCRIPTION_SOURCEOUTPORT)) {
					sourceOutPort = Integer.parseInt(subDetailElem.getTextValue());
				} else if(subDetailElem.getName().equals(SUBSCRIPTION_SCHEMA)) {
					schema = SchemaHelper.createSchemaFromStatement(subDetailElem);
				}
			}
			// It's a sink subscription, meaning that the source of the subscription is the source and the target the sink
			if(elem.getName().equals(SINKSUBSCRIPTIONS_TAG)) {
				ISource<?> source = (ISource<?>)newOperators.get(subOrigin);
				ISink sink = (ISink)newOperators.get(subTarget);
				source.subscribeSink(sink, sinkInPort, sourceOutPort, schema);
			// It's a source subscription, meaning that the source of the subscription is the sink and the target is the source
			} else if(elem.getName().equals(SOURCESUBSCRIPTIONS_TAG)) {
				ISink<?> sink = (ISink<?>)newOperators.get(subOrigin);
				ISource source = (ISource)newOperators.get(subTarget);
				sink.subscribeToSource(source, sinkInPort, sourceOutPort, schema);
			}
		}
		
	}
}
