package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaDataInitializer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMetadataInitLatencyProbabilityStreamCarsRule extends AbstractTransformationRule<MetadataCreationPO>{

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public void execute(MetadataCreationPO operator,
			TransformationConfiguration config) {
		
		// DRL-Code
//		logicalOps : ArrayList() from collect(ILogicalOperator(physInputPOs contains mPO))
		ArrayList logicalOps = new ArrayList();
		for(Object o: super.getCollection()){
			if(o instanceof ILogicalOperator){
				ILogicalOperator logOp = (ILogicalOperator)o;
				Collection physIn = logOp.getPhysInputPOs();
				for(Object inputOp : physIn){
					if(inputOp == operator){
						logicalOps.add(logOp);
						break;
					}
				}
				// is this the same as above?
//				if(logOp.getPhysInputPOs().contains(operator)){
//					logicalOps.add(logOp);
//				}
			}
		}
		
		System.out.println("CREATE MetadataUpdater Streamcars ObjectTracking.");
		
		SDFAttributeList outputSchema = operator.getOutputSchema();

		IMetadataUpdater mFac = new StreamCarsMetaDataInitializer(outputSchema);
		MetadataUpdatePO muPO = new MetadataUpdatePO( mFac );
		muPO.setOutputSchema(outputSchema);
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().insertNewFather(operator, logicalOps, muPO);
		for (ILogicalOperator o: toUpdate){
			update(o);
		}
		
		insert(muPO);
		retract(operator);
		System.out.println("CREATE MetadataUpdater Streamcars ObjectTracking finished.");
		
	}

	@Override
	public boolean isExecutable(MetadataCreationPO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName())){
			return true;
			// MetadataCreationPO does not have to checked, because there is no condition for it
			// from collect will be used in the execute method.
		}
		
		return false;
		
		
		// DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" )
//		mPO : MetadataCreationPO()
//		logicalOps : ArrayList() from collect(ILogicalOperator(physInputPOs contains mPO))
	}

	@Override
	public String getName() {
		return "Metadata(StreamCars) initialization: latency, probability";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.CREATE_METADATA;
	}

}
