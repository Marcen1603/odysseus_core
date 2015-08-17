package de.uniol.inf.is.odysseus.iql.odl.typing;

import java.util.Collection;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.odl.types.MepFunctions;
import de.uniol.inf.is.odysseus.iql.odl.types.ODLUtils;
import de.uniol.inf.is.odysseus.iql.odl.types.extension.PunctuationExtensions;
import de.uniol.inf.is.odysseus.iql.odl.types.extension.TransferExtensions;
import de.uniol.inf.is.odysseus.iql.odl.types.extension.TupleExtensions;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class ODLDefaultTypes {
	
	
	public static Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(IPunctuation.class);
		types.add(IStreamObject.class);
		types.add(Tuple.class);
		types.add(IExpression.class);
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
		return types;
	}
	
	public static Collection<String> getImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		implicitImports.add("java.util.*");
		implicitImports.add("java.lang.*");
		implicitImports.add(IExpression.class.getCanonicalName());
		implicitImports.add(MepFunctions.class.getCanonicalName());
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

		return implicitImports;
	}
	
	public static Collection<IIQLTypeExtensions> getTypeOperators() {
		Collection<IIQLTypeExtensions> result = new HashSet<>();
		result.add(new TupleExtensions());
		result.add(new PunctuationExtensions());
		result.add(new TransferExtensions());
		return result;
	}

	public static Collection<Class<?>> getImplicitStaticImports() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(System.class);
		types.add(Tuple.class);
		types.add(SDFSchemaFactory.class);
		types.add(SDFSchema.class);
		types.add(MepFunctions.class);
		types.add(Order.class);
		types.add(MetadataRegistry.class);
		types.add(SDFDatatype.class);
		types.add(ODLUtils.class);
		return types;
	}


}
