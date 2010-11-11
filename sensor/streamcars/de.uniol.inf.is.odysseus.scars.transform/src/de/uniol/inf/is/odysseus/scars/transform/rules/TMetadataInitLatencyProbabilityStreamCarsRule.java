package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaDataInitializer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings("all")
public class TMetadataInitLatencyProbabilityStreamCarsRule extends AbstractTransformationRule<MetadataCreationPO>{

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	@SuppressWarnings("all")
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

		StreamCarsMetaDataInitializer mFac = new StreamCarsMetaDataInitializer(outputSchema);
		
		try {
			AbstractSensorAccessPO<?, ?> src = (AbstractSensorAccessPO<?, ?>) operator.getSubscribedToSource(0).getTarget();
			mFac.setObjectListPath(src.getObjectListPath());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
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
	@SuppressWarnings("all")
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
