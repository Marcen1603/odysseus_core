package de.uniol.inf.is.odysseus.iql.odl.typing;

import java.util.Collection;
import java.util.HashSet;





import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.odl.types.ODLUtils;
import de.uniol.inf.is.odysseus.iql.odl.types.extension.PunctuationExtensions;
import de.uniol.inf.is.odysseus.iql.odl.types.extension.TransferExtensions;
import de.uniol.inf.is.odysseus.iql.odl.types.extension.TupleExtensions;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;

public class ODLDefaultTypes {
	
	
	public static Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(IPunctuation.class);
		types.add(IStreamObject.class);
		types.add(Tuple.class);
		types.add(IMepExpression.class);
		types.add(SDFSchema.class);
		types.add(Subscription.class);	
		types.add(ITimeIntervalSweepArea.class);	
		types.add(ITransferArea.class);	
		types.add(Order.class);	
		types.add(IDataMergeFunction.class);	
		types.add(IMetadataMergeFunction.class);	
		types.add(JoinTISweepArea.class);	
		types.add(TITransferArea.class);	
		types.add(RelationalMergeFunction.class);	
		types.add(MetadataRegistry.class);
		types.add(RelationalExpression.class);
		types.add(SDFExpression.class);
		types.add(SDFElement.class);
		types.add(ODLUtils.class);
		types.add(SDFDatatype.class);
		types.add(AbstractPhysicalSubscription.class);
		types.add(ITimeInterval.class);
		types.add(TruePredicate.class);
		types.add(SweepAreaRegistry.class);
		types.add(PointInTime.class);
		types.add(TimeUnit.class);
		types.add(SDFDatatype.class);		
		types.add(SDFConstraint.class);
		types.add(IGroupProcessor.class);
		types.add(NoGroupProcessor.class);
		types.add(RelationalGroupProcessor.class);
		types.add(Pair.class);
		types.add(IPair.class);
		types.add(AggregateFunction.class);
		types.add(IAggregateFunctionBuilder.class);
		types.add(IAggregateFunction.class);
		types.add(IMerger.class);
		types.add(IEvaluator.class);
		types.add(IPartialAggregate.class);
		types.add(IInitializer.class);
		types.add(FESortedClonablePair.class);
		types.add(PairMap.class);
		types.add(AggregateFunctionBuilderRegistry.class);
		types.add(Cardinalities.class);
		types.add(EnumParameter.class);
		types.add(KeyValueObject.class);
		return types;
	}

	
	public static Collection<String> getImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		implicitImports.add("java.util.*");
		implicitImports.add("java.lang.*");
		implicitImports.add(IMepExpression.class.getCanonicalName());
		implicitImports.add(IPunctuation.class.getCanonicalName());
		implicitImports.add(Tuple.class.getCanonicalName());
		implicitImports.add(IStreamObject.class.getCanonicalName());
		implicitImports.add(SDFSchema.class.getCanonicalName());
		implicitImports.add(Subscription.class.getCanonicalName());
		implicitImports.add(ITimeIntervalSweepArea.class.getCanonicalName());
		implicitImports.add(ITransferArea.class.getCanonicalName());
		implicitImports.add(Order.class.getCanonicalName());
		implicitImports.add(IDataMergeFunction.class.getCanonicalName());
		implicitImports.add(IMetadataMergeFunction.class.getCanonicalName());
		implicitImports.add(JoinTISweepArea.class.getCanonicalName());
		implicitImports.add(TITransferArea.class.getCanonicalName());
		implicitImports.add(RelationalMergeFunction.class.getCanonicalName());
		implicitImports.add(MetadataRegistry.class.getCanonicalName());
		implicitImports.add(SDFSchemaFactory.class.getCanonicalName());
		implicitImports.add(RelationalExpression.class.getCanonicalName());
		implicitImports.add(SDFExpression.class.getCanonicalName());
		implicitImports.add(SDFElement.class.getCanonicalName());
		implicitImports.add(ODLUtils.class.getCanonicalName());
		implicitImports.add(SDFDatatype.class.getCanonicalName());
		implicitImports.add(AbstractPhysicalSubscription.class.getCanonicalName());
		implicitImports.add(TruePredicate.class.getCanonicalName());
		implicitImports.add(SweepAreaRegistry.class.getCanonicalName());
		implicitImports.add(ITimeInterval.class.getCanonicalName());
		implicitImports.add(PointInTime.class.getCanonicalName());
		implicitImports.add(TimeUnit.class.getCanonicalName());
		implicitImports.add(SDFDatatype.class.getCanonicalName());
		implicitImports.add(SDFConstraint.class.getCanonicalName());
		implicitImports.add(IGroupProcessor.class.getCanonicalName());
		implicitImports.add(NoGroupProcessor.class.getCanonicalName());
		implicitImports.add(RelationalGroupProcessor.class.getCanonicalName());
		implicitImports.add(Pair.class.getCanonicalName());
		implicitImports.add(IPair.class.getCanonicalName());
		implicitImports.add(AggregateFunction.class.getCanonicalName());
		implicitImports.add(IAggregateFunctionBuilder.class.getCanonicalName());
		implicitImports.add(IAggregateFunction.class.getCanonicalName());
		implicitImports.add(IMerger.class.getCanonicalName());
		implicitImports.add(IEvaluator.class.getCanonicalName());
		implicitImports.add(IPartialAggregate.class.getCanonicalName());
		implicitImports.add(IInitializer.class.getCanonicalName());
		implicitImports.add(FESortedClonablePair.class.getCanonicalName());
		implicitImports.add(PairMap.class.getCanonicalName());
		implicitImports.add(AggregateFunctionBuilderRegistry.class.getCanonicalName());
		implicitImports.add(PairMap.class.getCanonicalName());
		implicitImports.add(Cardinalities.class.getCanonicalName());
		implicitImports.add(EnumParameter.class.getCanonicalName());
		implicitImports.add(KeyValueObject.class.getCanonicalName());
		return implicitImports;
	}
	

	public static Collection<String> getImplicitStaticImports() {
		Collection<String> types = new HashSet<>();
		types.add(Tuple.class.getCanonicalName());
		types.add(SDFSchemaFactory.class.getCanonicalName());
		types.add(SDFSchema.class.getCanonicalName());
		types.add(Order.class.getCanonicalName());
		types.add(MetadataRegistry.class.getCanonicalName());
		types.add(SDFDatatype.class.getCanonicalName());
		types.add(ODLUtils.class.getCanonicalName());
		types.add(TruePredicate.class.getCanonicalName());
		types.add(SweepAreaRegistry.class.getCanonicalName());
		types.add(TimeUnit.class.getCanonicalName());
		types.add(SDFDatatype.class.getCanonicalName());
		types.add(SDFConstraint.class.getCanonicalName());
		types.add(AggregateFunctionBuilderRegistry.class.getCanonicalName());
		types.add(Cardinalities.class.getCanonicalName());
		return types;
	}
	
	
	public static Collection<IIQLTypeExtensions> getTypeOperators() {
		Collection<IIQLTypeExtensions> result = new HashSet<>();
		result.add(new TupleExtensions());
		result.add(new PunctuationExtensions());
		result.add(new TransferExtensions());
		return result;
	}
	
	public static Collection<String> getDependencies() {
		Collection<String> bundles = new HashSet<>();
		bundles.add("de.uniol.inf.is.odysseus.transform");
		bundles.add("de.uniol.inf.is.odysseus.ruleengine");
		bundles.add("de.uniol.inf.is.odysseus.rewrite");
		bundles.add("de.uniol.inf.is.odysseus.mep");
		bundles.add("de.uniol.inf.is.odysseus.iql.odl");
		bundles.add("de.uniol.inf.is.odysseus.sweeparea");
		bundles.add("de.uniol.inf.is.odysseus.server.intervalapproach");
		bundles.add("de.uniol.inf.is.odysseus.relational.base");
		bundles.add("de.uniol.inf.is.odysseus.intervalapproach");
		return bundles;
	}
	
	public static Collection<String> getVisibleTypesFromBundle() {
		Collection<String> bundles = new HashSet<>();
		bundles.add("de.uniol.inf.is.odysseus.mep");
		bundles.add("de.uniol.inf.is.odysseus.sweeparea");
		bundles.add("de.uniol.inf.is.odysseus.server.intervalapproach");
		bundles.add("de.uniol.inf.is.odysseus.relational.base");
		bundles.add("de.uniol.inf.is.odysseus.intervalapproach");	
		return bundles;
	}

	public static Collection<String> getImportedPackages() {
		Collection<String> packages = new HashSet<>();
		return packages;
	}


}
