package de.uniol.inf.is.odysseus.transform.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.Subscription;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.metadata.base.MetadataRegistry;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TCreateMetadataRule extends AbstractTransformationRule<ISource> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(ISource source, TransformationConfiguration transformConfig) {
		Class type = MetadataRegistry.getMetadataType(transformConfig.getMetaTypes());
		MetadataCreationPO po = null;
		for(PhysicalSubscription<ISink> sub : (Collection<PhysicalSubscription<ISink>>)source.getSubscriptions()){
			if ((sub.getTarget() instanceof MetadataCreationPO) && ((MetadataCreationPO)sub.getTarget()).getType() == type) {
				po = (MetadataCreationPO)sub.getTarget();
				break;
			}
		}
		if (po == null) {
			po = new MetadataCreationPO(type);
			po.setOutputSchema(source.getOutputSchema());
			source.subscribeSink(po, 0, 0, source.getOutputSchema()); // TODO: Hier wird ignoriert, dass die Source evtl. mehrere Ausg�nge hat 
		}
		
		ArrayList<ILogicalOperator> logicalOps = new ArrayList<ILogicalOperator>();
		for(Object o : getCollection()){
			if(o instanceof ILogicalOperator){
				ILogicalOperator oplog = (ILogicalOperator)o;
				if(oplog.getPhysInputPOs().contains(source)){
					logicalOps.add(oplog);
				}
			}
		}
		
		for(ILogicalOperator op : (List<ILogicalOperator>)logicalOps) {
			for (Subscription<ISource<?>> psub : op.getPhysSubscriptionsTo()) {
					if (psub.getTarget() == source){
						op.setPhysSubscriptionTo((ISource)po ,psub.getSinkInPort(), psub.getSourceOutPort(), psub.getSchema());
						update(op);
					}
				}
		}
		
		// ABo: The new MetadataCreationPO must be inserted into the working memory, if not already done
		// this metadataCreationPO will be used by other rules
		// The check, if the metadataCreationPO is already inserted, must be done here. It cannot be
		// done in the first loop of the then-clause, since this rule could have been used by other queries
		// that created the metadataCreationPOs but could not add it to the working memory of this query.
		//boolean alreadyInserted = false;
		//for(MetadataCreationPO mPO : (List<MetadataCreationPO>) metadataCreationPOs){
		//	if(mPO == po){
		//		alreadyInserted = true;
		//	}
		//}
		
		//if(!alreadyInserted){
		//	insert(po);
		//}
		
		insert(po);
		
		retract(source);		
	}

	@Override
	public boolean isExecutable(ISource source, TransformationConfiguration transformConfig) {
		if(!source.isSink()){
			if(!transformConfig.hasOption("NO_METADATA")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Create Metadata";
	}

}
