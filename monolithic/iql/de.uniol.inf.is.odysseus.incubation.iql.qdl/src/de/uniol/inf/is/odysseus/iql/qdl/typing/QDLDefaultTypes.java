package de.uniol.inf.is.odysseus.iql.qdl.typing;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.Operator;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.PipeOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.SinkOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.SourceOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.source.Source;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscribable;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscriber;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscription;

public class QDLDefaultTypes {
	
	
	public static Collection<Class<?>> getVisibleTypes() {
		Collection<Class<?>> types = new HashSet<>();
		types.add(Option.class);
		types.add(Source.class);
		types.add(Operator.class);
		types.add(SourceOperator.class);
		types.add(PipeOperator.class);
		types.add(SinkOperator.class);
		types.add(Subscribable.class);
		types.add(Subscriber.class);
		types.add(Subscription.class);
		types.add(TimeUnit.class);
		return types;
	}
	
	public static Collection<String> getImplicitImports() {
		Collection<String> implicitImports = new HashSet<>();
		implicitImports.add(Option.class.getCanonicalName());
		implicitImports.add(Source.class.getCanonicalName());
		implicitImports.add(Operator.class.getCanonicalName());
		implicitImports.add(SourceOperator.class.getCanonicalName());
		implicitImports.add(PipeOperator.class.getCanonicalName());
		implicitImports.add(SinkOperator.class.getCanonicalName());
		implicitImports.add(Subscribable.class.getCanonicalName());
		implicitImports.add(Subscriber.class.getCanonicalName());
		implicitImports.add(Subscription.class.getCanonicalName());
		implicitImports.add(TimeUnit.class.getCanonicalName());
		return implicitImports;
	}

	public static Collection<String> getImplicitStaticImports() {
		Collection<String> types = new HashSet<>();
		types.add(TimeUnit.class.getCanonicalName());
		types.add(Runtime.class.getCanonicalName());
		return types;
	}

	public static Collection<IIQLTypeExtensions> getTypeOperators() {
		Collection<IIQLTypeExtensions> result = new HashSet<>();
		return result;		
	}
	
	public static Collection<String> getDependencies() {
		Collection<String> bundles = new HashSet<>();
		bundles.add("de.uniol.inf.is.odysseus.iql.qdl");
		return bundles;
	}
	
	public static Collection<String> getVisibleTypesFromBundle() {
		Collection<String> bundles = new HashSet<>();
		return bundles;
	}

	public static Collection<String>  getImportedPackages() {
		Collection<String> packages = new HashSet<>();
		return packages;
	}

}
