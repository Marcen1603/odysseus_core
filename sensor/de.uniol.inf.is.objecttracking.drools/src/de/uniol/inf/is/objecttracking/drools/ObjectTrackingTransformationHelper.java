package de.uniol.inf.is.objecttracking.drools;

import java.io.IOException;
import java.text.ParseException;

import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.MapAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnionAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.PredictionAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IntervalProbabilityLatencyPrediction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.factory.IntervalProbabilityLatencyPredictionMFactory;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.PredictionPO;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.RelationalProjectMVPO;
//import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.IntervalProbabilityLatencyPredictionMergeFunction;
//import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.PredictionJoinTIPO;
//import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.RelationalJoinTIMVPredictionSweepArea;
import de.uniol.inf.is.odysseus.objecttracking.predicate.ProbabilityPredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITransferFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class ObjectTrackingTransformationHelper {

//	@Override
//	public ISource<?> createAccessPO(AccessAO accessAO) throws IOException {
//		String sourceName = accessAO.getSource().getURI(false);
//		ISource<MVRelationalTuple<IntervalProbabilityLatencyPrediction>> accessPO = WrapperPlanFactory
//				.getAccessPlan(accessAO);
//
//		if (accessPO == null) {
//			System.err.println(sourceName + " NO WRAPPER??");
//		}
//		IMetadataFactory<IntervalProbabilityLatencyPrediction, MVRelationalTuple<IntervalProbabilityLatencyPrediction>> mFac;
//		mFac = new IntervalProbabilityLatencyPredictionMFactory(accessAO
//				.getOutputSchema());
//
//		MetadataPO<IntervalProbabilityLatencyPrediction, MVRelationalTuple<IntervalProbabilityLatencyPrediction>, MVRelationalTuple<IntervalProbabilityLatencyPrediction>> mPO;
//		mPO = new MetadataPO<IntervalProbabilityLatencyPrediction, MVRelationalTuple<IntervalProbabilityLatencyPrediction>, MVRelationalTuple<IntervalProbabilityLatencyPrediction>>(
//				mFac);
//
//		mPO.subscribeTo(accessPO, 0);
//
//		return mPO;
//
//		// TODO metadatenfactory setzen? bzw einen operator, der das tut
//		// davor setzen
//
//		// TODO: Kostenwerte anpassen
//		// setMetadataItems(source, 1, 1, 1);
//		// setMetadataItems(top == null ? source : top, 1, 1, 1);
//	}
//
//	@Override
//	public ISource<?> createAggregatePO(AggregateAO aggregateAO) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ISource<?> createJoinPO(JoinAO joinAO) {
//		MVRelationalPredictionMergeFunction<IntervalProbabilityLatencyPrediction> dataMerge = new MVRelationalPredictionMergeFunction<IntervalProbabilityLatencyPrediction>(
//				joinAO.getOutputSchema(), joinAO.getLeftInputSchema(), joinAO.getRightInputSchema());
//		// FIXME @Andre Kovarianz zwischen linkem und rechtem Strom auslesen und setzen
//		IntervalProbabilityLatencyPredictionMergeFunction metadataMerge = new IntervalProbabilityLatencyPredictionMergeFunction(null);
//		SweepArea<MVRelationalTuple<IntervalProbabilityLatencyPrediction>>[] sas = new SweepArea[2];
//		ITransferFunction<MVRelationalTuple<IntervalProbabilityLatencyPrediction>> transferFunction;
//		if (priorityMode == PriorityMode.NO_PRIORITY) {
//			sas[0] = new RelationalJoinTIMVPredictionSweepArea<IntervalProbabilityLatencyPrediction>(joinAO.getLeftInputSchema(), joinAO.getRightInputSchema());
//			sas[1] = new RelationalJoinTIMVPredictionSweepArea<IntervalProbabilityLatencyPrediction>(joinAO.getLeftInputSchema(), joinAO.getRightInputSchema());
//			transferFunction = new TITransferFunction<MVRelationalTuple<IntervalProbabilityLatencyPrediction>>();
//		} else {
//			throw new IllegalArgumentException(
//					"Multivariate Data cannot be processed with priorities at the moment.");
//		}
//
//		// if there exist probability predicates in a complex predicate,
//		// the input schemas of the join have to be set.
//		IPredicate predicate = joinAO.getPredicate();
//
//		initializeProbabilityPredicates(predicate, joinAO.getInputSchema(0),
//				joinAO.getInputSchema(1));
//
//		// TODO fuer kartesisches eigenen operator vorsehen
//		// atm hack mit einem praedikat das true liefert
//		if (predicate == null) {
//			try {
//				predicate = new RelationalPredicate(new SDFExpression("",
//						"1==1", null));
//				((RelationalPredicate) predicate).init(
//						joinAO.getInputSchema(0), joinAO.getInputSchema(1));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		sas[0].setQueryPredicate(predicate.clone());
//		sas[1].setQueryPredicate(predicate.clone());
//
//		PredictionJoinTIPO<IntervalProbabilityLatencyPrediction, MVRelationalTuple<IntervalProbabilityLatencyPrediction>> joinPO;
//		joinPO = new PredictionJoinTIPO<IntervalProbabilityLatencyPrediction, MVRelationalTuple<IntervalProbabilityLatencyPrediction>>(
//				dataMerge, metadataMerge, transferFunction, sas);
//		setMetadataItems(joinPO, 1, 10, 2);
//
//		return joinPO;
//	}
//
//	@Override
//	public ISource<?> createMapPO(MapAO mapAO) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ISource<?> createPriorityPO(PriorityAO<?> priorityAO) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public ISource<?> createPredictionPO(PredictionAO<?> predictionAO){
//		return new PredictionPO<MVRelationalTuple<IntervalProbabilityLatencyPrediction>, IntervalProbabilityLatencyPrediction>
//				((PredictionAO<MVRelationalTuple<IntervalProbabilityLatencyPrediction>>) predictionAO);
//	}
//
//	@Override
//	public ISource<?> createProjectPO(ProjectAO projectAO) {
//		double[][] matrix = projectAO.getProjectMatrix();
//		double[] vector = projectAO.getProjectVector();
//		ISource<?> projectPO;
//
//		// if no vector has been specified, there will
//		// be a default vector 0.
//		if (matrix != null && vector != null) {
//			projectPO = new RelationalProjectMVPO<IntervalProbabilityLatency>(
//					projectAO.getRestrictList(), new RealMatrixImpl(matrix),
//					new RealMatrixImpl(vector), projectAO.getInputSchema(0));
//		} else if (matrix != null && vector == null) {
//			projectPO = new RelationalProjectMVPO<IntervalProbabilityLatency>(
//					projectAO.getRestrictList(), new RealMatrixImpl(matrix),
//					null, projectAO.getInputSchema(0));
//		} else {
//			projectPO = new RelationalProjectMVPO<IntervalProbabilityLatency>(
//					projectAO.getRestrictList(), null, null, projectAO
//							.getInputSchema(0));
//		}
//		// TODO: Kostenwerte anpassen
//		setMetadataItems(projectPO, 1, 1, 1);
//
//		return projectPO;
//	}
//
//	@Override
//	public ISource<?> createSelectPO(SelectAO selectAO) {
//		IPredicate pred = selectAO.getPredicate();
//		initializeProbabilityPredicates(pred, selectAO.getInputSchema(0));
//
//		SelectPO<MVRelationalTuple<IntervalProbabilityLatency>> selectPO;
//		selectPO = new SelectPO<MVRelationalTuple<IntervalProbabilityLatency>>(
//				pred);
//		// TODO: Selektivit�t anpassen
//		// TODO: Kostenwerte anpassen
//		setMetadataItems(selectPO, 1, 5, 1);
//
//		return selectPO;
//	}
//
//	@Override
//	public ISource<?> createUnionPO(UnionAO unionAO) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ISource<?> createWindowPO(WindowAO windowAO) {
//		SlidingAdvanceTimeWindowTIPO<MVRelationalTuple<IntervalProbabilityLatency>> windowPO;
//
//		windowPO = new SlidingAdvanceTimeWindowTIPO<MVRelationalTuple<IntervalProbabilityLatency>>(
//				windowAO);
//		
//		// TODO: Kostenwerte anpassen
//		setMetadataItems(windowPO, 1, 1, 1);
//
//		return windowPO;
//	}
//
//	@Override
//	public ISource<?> createWindowMPO(WindowAO windowAO) {
//		IMetadataFactory<IntervalProbabilityLatency, MVRelationalTuple<IntervalProbabilityLatency>> mFac;
//		MetadataPO<IntervalProbabilityLatency, MVRelationalTuple<IntervalProbabilityLatency>, MVRelationalTuple<IntervalProbabilityLatency>> mPO;
//
//		if (windowAO.getWindowOn() == null) {
//			throw new RuntimeException("No WINDOW ON statement given!");
//		} else {
//			int attrPos = windowAO.getInputSchema().indexOf(
//					windowAO.getWindowOn());
//			mFac = new TimestampAttributeMFactory(attrPos);
//		}
//
//		mPO = new MetadataPO<IntervalProbabilityLatency, MVRelationalTuple<IntervalProbabilityLatency>, MVRelationalTuple<IntervalProbabilityLatency>>(
//				mFac);
//
//		setMetadataItems(mPO, 1, 1, 1);
//		return mPO;
//	}
//
//	private void initializeProbabilityPredicates(IPredicate pred,
//			SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
//		if (pred instanceof ComplexPredicate) {
//			initializeProbabilityPredicates(
//					((ComplexPredicate) pred).getLeft(), leftSchema,
//					rightSchema);
//			initializeProbabilityPredicates(((ComplexPredicate) pred)
//					.getRight(), leftSchema, rightSchema);
//		}
//
//		else if (pred instanceof ProbabilityPredicate) {
//			((ProbabilityPredicate) pred).setLeftSchema(leftSchema);
//			((ProbabilityPredicate) pred).setRightSchema(rightSchema);
//		}
//	}
//
//	private void initializeProbabilityPredicates(IPredicate pred,
//			SDFAttributeList leftSchema) {
//		if (pred instanceof ComplexPredicate) {
//			initializeProbabilityPredicates(
//					((ComplexPredicate) pred).getLeft(), leftSchema);
//			initializeProbabilityPredicates(((ComplexPredicate) pred)
//					.getRight(), leftSchema);
//		}
//
//		else if (pred instanceof ProbabilityPredicate) {
//			((ProbabilityPredicate) pred).setLeftSchema(leftSchema);
//		}
//	}
	
}
