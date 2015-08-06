package de.uniol.inf.is.odysseus.iql.odl.typing;

import java.util.Collection;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.odl.types.MepFunctions;
import de.uniol.inf.is.odysseus.iql.odl.types.SchemaFunctions;
import de.uniol.inf.is.odysseus.iql.odl.typing.extension.PunctuationExtensions;
import de.uniol.inf.is.odysseus.iql.odl.typing.extension.TupleExtensions;

public class ODLDefaultTypes {
	
	
	public static Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(IPunctuation.class);
		types.add(IStreamObject.class);
		types.add(Tuple.class);
		types.add(IExpression.class);
		types.add(SDFSchema.class);
		types.add(Subscription.class);		
		return types;
	}
	
	public static Collection<String> getImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		implicitImports.add("java.util.*");
		implicitImports.add("java.lang.*");
		implicitImports.add(IExpression.class.getCanonicalName());
		implicitImports.add(MepFunctions.class.getCanonicalName());
		implicitImports.add(SchemaFunctions.class.getCanonicalName());
		implicitImports.add(IPunctuation.class.getCanonicalName());
		implicitImports.add(Tuple.class.getCanonicalName());
		implicitImports.add(IStreamObject.class.getCanonicalName());
		implicitImports.add(SDFSchema.class.getCanonicalName());
		implicitImports.add(Subscription.class.getCanonicalName());

		return implicitImports;
	}
	
	public static Collection<IIQLTypeExtensions> getTypeOperators() {
		Collection<IIQLTypeExtensions> result = new HashSet<>();
		result.add(new TupleExtensions());
		result.add(new PunctuationExtensions());
		return result;
	}


}
