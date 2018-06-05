package de.uniol.inf.is.odysseus.iql.basic.typing;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AccessAOSourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BitVectorParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ByteParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateAndRenameSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileNameParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.HTTPStringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MetaAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PhysicalOperatorParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ValidatedFileNameParameter;

public class ParameterFactory {
	
	private static List<Class<? extends IParameter<?>>> parameters = new ArrayList<>();

	
	private static Map<Class<? extends IParameter<?>>, Class<?>> parametersByType = new HashMap<>();

	private static Map<Class<?>, List<Class<? extends IParameter<?>>>> parametersByValue = new HashMap<>();

	
	static {
		addParameter(StringParameter.class, String.class);
		addParameter(BooleanParameter.class, Boolean.class);
		addParameter(ByteParameter.class, Byte.class);
		addParameter(IntegerParameter.class, Integer.class);
		addParameter(DoubleParameter.class, Double.class);
		addParameter(LongParameter.class, Long.class);

		
		addParameter(AccessAOSourceParameter.class, Resource.class);
		addParameter(AggregateItemParameter.class, AggregateItem.class);
		//addParameter(BatchParameter.class, BatchItem.class);
		addParameter(BitVectorParameter.class, BitVector.class);
		addParameter(CreateAndRenameSDFAttributeParameter.class, RenameAttribute.class);
		addParameter(CreateSDFAttributeParameter.class, SDFAttribute.class);
		//addParameter(DirectParameter.class, T);
		//addParameter(EnumParameter.class, Enum.class);
		addParameter(FileNameParameter.class, String.class);
		addParameter(ValidatedFileNameParameter.class, File.class);
		addParameter(FileParameter.class, File.class);
		addParameter(HTTPStringParameter.class, String.class);
		//addParameter(ListParameter.class, List<T>);
		//addParameter(MapParameter.class, Map<K, V>);
		//addParameter(MatrixParameter.class, Double[][].class);
		addParameter(MetaAttributeParameter.class, IMetaAttribute.class);
		//addParameter(NestAggregateItemParameter.class, NestAggregateItem.class);
		//addParameter(OptionParameter.class, Option.class);
		addParameter(PhysicalOperatorParameter.class, IPhysicalOperator.class);
		addParameter(PredicateParameter.class, IPredicate.class);
		addParameter(ResolvedSDFAttributeParameter.class, SDFAttribute.class);
		addParameter(ResourceParameter.class, Resource.class);
		addParameter(NamedExpressionParameter.class, NamedExpression.class);
		addParameter(SourceParameter.class, AccessAO.class);
		addParameter(TimeParameter.class, TimeValueItem.class);
		//addParameter(UncheckedExpressionParamter.class, String[].class);
		//addParameter(VectorParameter.class, Double[].class);
	}
		
	private static void addParameter(Class<? extends IParameter<?>> parameterType, Class<?> valueType) {
		parameters.add(parameterType);
		parametersByType.put(parameterType, valueType);
		
		List<Class<? extends IParameter<?>>> types = parametersByValue.get(valueType);
		if (types == null) {
			types = new ArrayList<>();
			parametersByValue.put(valueType, types);
		}
		types.add(parameterType);
	}
	
	public static List<Class<? extends IParameter<?>>> getParameters() {
		return parameters;
	}
	
	public static Class<?> getParameterValue(Class<? extends IParameter<?>> parameterType) {
		return parametersByType.get(parameterType);
	}
	
}
