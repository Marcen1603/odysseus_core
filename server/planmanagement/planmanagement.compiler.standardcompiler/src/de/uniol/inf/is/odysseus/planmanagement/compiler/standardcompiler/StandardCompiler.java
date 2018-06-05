/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.planmanagement.compiler.standardcompiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.plangeneration.IPlanGenerator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * The StandardCompiler is a standard implementation for {@link ICompiler}. It
 * provides features to edit queries (e. g. translate, rewrite, transform). This
 * class stores a list of {@link IQueryParser} services, a
 * {@link ITransformation} and a {@link IRewrite} service.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
public class StandardCompiler implements ICompiler {

	/**
	 * {@link ITransformation} service
	 */
	protected ITransformation transformation;

	/**
	 * {@link IQueryParser} service list
	 */
	protected TreeMap<String, IQueryParser> parserList = new TreeMap<String, IQueryParser>();

	/**
	 * {@link IRewrite} service
	 */
	protected IRewrite rewrite;

	/*
	 * {@link IPlanGenerator} service
	 */
	protected IPlanGenerator planGenerator;

	/**
	 * Listener
	 */
	private List<ICompilerListener> listener = new CopyOnWriteArrayList<ICompilerListener>();

	/**
	 * Get a formated info string for object. if object not null
	 * 
	 * @param object
	 *            object to describe
	 * @param label
	 *            label for description
	 * @return String: "LINE_SEPERATOR + label + ":" + class name of object" or
	 *         "LINE_SEPERATOR + label + ":" + not set"
	 */
	public String getInfoString(Object object, String label) {
		String infos = AppEnv.LINE_SEPARATOR + label + ": ";
		if (object != null) {
			infos += object.getClass();
		} else {
			infos += "not set. ";
		}

		return infos;
	}

	/**
	 * Method to bind a {@link IQueryParser}. Used by OSGi.
	 * 
	 * @param parser
	 *            new {@link IQueryParser} service
	 */
	public void bindParser(IQueryParser parser) {
		synchronized (this.parserList) {
			this.parserList.put(parser.getLanguage(), parser);
		}
		for (ICompilerListener l : listener) {
			l.parserBound(parser.getLanguage());
		}
	}

	/**
	 * Method to unbind a {@link IQueryParser}. Used by OSGi.
	 * 
	 * @param parser
	 *            {@link IQueryParser} service to unbind
	 */
	public void unbindParser(IQueryParser parser) {
		synchronized (this.parserList) {
			if (this.parserList.containsKey(parser.getLanguage())) {
				this.parserList.remove(parser.getLanguage());
			}
		}
	}

	/**
	 * Method to bind a {@link ITransformation}. Used by OSGi.
	 * 
	 * @param transformation
	 *            new {@link ITransformation} service
	 */
	public void bindTransformation(ITransformation transformation) {
		this.transformation = transformation;
		for (ICompilerListener l : listener) {
			l.transformationBound();
		}
	}

	/**
	 * Method to unbind a {@link ITransformation}. Used by OSGi.
	 * 
	 * @param transformation
	 *            {@link ITransformation} service to unbind
	 */
	public void unbindTransformation(ITransformation transformation) {
		if (this.transformation == transformation) {
			this.transformation = null;
		}
	}

	/**
	 * Method to bind a {@link IRewrite}. Used by OSGi.
	 * 
	 * @param rewrite
	 *            new {@link IRewrite} service
	 */
	public void bindRewrite(IRewrite rewrite) {
		this.rewrite = rewrite;
		for (ICompilerListener l : listener) {
			l.rewriteBound();
		}

	}

	/**
	 * Method to unbind a {@link IRewrite}. Used by OSGi.
	 * 
	 * @param rewrite
	 *            {@link IRewrite} service to unbind
	 */
	public void unbindRewrite(IRewrite rewrite) {
		if (this.rewrite == rewrite) {
			this.rewrite = null;
		}
	}

	/**
	 * Method to bind a {@link IPlanGenerator}. Used by OSGi.
	 * 
	 * @param planGenerator
	 *            {@link IPlanGenerator} service
	 */
	public void bindPlanGenerator(IPlanGenerator planGenerator) {
		this.planGenerator = planGenerator;
		for (ICompilerListener l : listener) {
			l.planGeneratorBound();
		}
	}

	/**
	 * Method to unbind {@link IPlanGenerator}. Used by OSGi.
	 * 
	 * @param planGenerator
	 *            {@link IPlanGenerator} service to unbind
	 */
	public void unbindPlanGenerator(IPlanGenerator planGenerator) {
		if (this.planGenerator == planGenerator) {
			this.planGenerator = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IInfoProvider#getInfos
	 * ()
	 */
	@Override
	public String getInfos() {
		String infos = "<Compiler class=\"" + this + "\"> ";

		infos += getInfoString(this.transformation, "Transformation comp.:");
		infos += getInfoString(this.rewrite, "Restructure comp.:");

		infos += AppEnv.LINE_SEPARATOR + " <Parser> ";
		for (IQueryParser parser : this.parserList.values()) {
			infos += getInfoString(parser, "Parser:");
		}
		infos += AppEnv.LINE_SEPARATOR + " </Parser>";

		infos += AppEnv.LINE_SEPARATOR + "</Compiler> ";

		return infos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler#translateQuery
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public List<IExecutorCommand> translateQuery(String query, String parserID,
			ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException {
		if (this.parserList.containsKey(parserID)) {
			return this.parserList.get(parserID).parse(query, user, dd,
					context, metaAttribute, executor);
		}

		throw new QueryParseException("Parser with ID " + parserID
				+ " not registered.");
	}

	@Override
	public IPhysicalQuery transform(ILogicalQuery query,
			TransformationConfiguration transformationConfiguration,
			ISession caller, IDataDictionary dd) throws TransformationException {

		ILogicalPlan copyPlan = query.getLogicalPlan().copyPlan();
		
		ArrayList<IPhysicalOperator> physicalPlan = this.transformation
				.transform(copyPlan, transformationConfiguration, caller, dd);

		IPhysicalQuery transformedQuery = new PhysicalQuery(query, physicalPlan);
		return transformedQuery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler#
	 * getSupportedQueryParser()
	 */
	@Override
	public Set<String> getSupportedQueryParser() {
		return Collections.unmodifiableSet(this.parserList.keySet());
	}

	@Override
	public boolean isTransformationBound() {
		return transformation != null;
	}

	@Override
	public boolean isRewriteBound() {
		return rewrite != null;
	}

	// @Override
	// public List<ILogicalOperator> createAlternativePlans(
	// ILogicalOperator logicalPlan, OptimizationConfiguration conf) {
	// // TODO mehrere Alternativen zu dem aktuellen Plan muessen generiert
	// // werden, z.B. durch Join-Vertauschungen
	// ILogicalOperator p = rewritePlan(logicalPlan,
	// conf.getRewriteConfiguration());
	// List<ILogicalOperator> list = new ArrayList<ILogicalOperator>(1);
	// list.add(p);
	// return list;
	// }

	// @Override
	// public List<List<IPhysicalOperator>> transformWithAlternatives(
	// ILogicalOperator logicalPlan,
	// TransformationConfiguration transformationConfiguration, User caller)
	// throws TransformationException {
	// // TODO mehrere Alternativen muessen generiert werden, z.B. durch
	// // verschiedene Join-Implementationen
	// ArrayList<IPhysicalOperator> p = transform(logicalPlan,
	// transformationConfiguration, caller);
	// List<List<IPhysicalOperator>> list = new
	// ArrayList<List<IPhysicalOperator>>(1);
	// list.add(p);
	// return list;
	// }

	@Override
	public void addCompilerListener(ICompilerListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void removeCompilerListener(ICompilerListener listener) {
		this.listener.remove(listener);
	}

	@Override
	public ILogicalPlan rewritePlan(ILogicalPlan plan,
			RewriteConfiguration conf, ISession caller, IDataDictionary dd) {
		return rewrite.rewritePlan(plan, conf, caller, dd);
	}

	@Override
	public Collection<String> getRewriteRules() {
		return rewrite.getRewriteRules();
	}

	@Override
	public List<IPhysicalQuery> translateAndTransformQuery(String query,
			String parserID, ISession user, IDataDictionary dd,
			TransformationConfiguration transformationConfiguration,
			Context context, IServerExecutor executor) throws QueryParseException,
			TransformationException {
		IMetaAttribute metaAttribute = MetadataRegistry
				.getMetadataType(transformationConfiguration.getDefaultMetaTypeSet());
		List<IExecutorCommand> translate = translateQuery(query, parserID,
				user, dd, context, metaAttribute, executor);
		List<IPhysicalQuery> translated = new ArrayList<IPhysicalQuery>();
		for (IExecutorCommand q : translate) {
			if (q instanceof CreateQueryCommand) {
				translated.add(transform(((CreateQueryCommand) q).getQuery(),
						transformationConfiguration, user, dd));
			}
		}
		return translated;
	}

	@Override
	public List<ILogicalPlan> generatePlans(ILogicalPlan plan,
			PlanGenerationConfiguration conf, IOperatorOwner owner) {
		return planGenerator.generatePlans(plan, conf, owner);
	}

	@Override
	public Map<String, List<String>> getQueryParserTokens(String parserID,
			ISession user) {
		if (this.parserList.containsKey(parserID)) {
			return this.parserList.get(parserID).getTokens(user);
		}
		return new HashMap<>();

	}

	@Override
	public List<String> getQueryParserSuggestions(String parserID, String hint,
			ISession user) {
		if (this.parserList.containsKey(parserID)) {
			return this.parserList.get(parserID).getSuggestions(hint, user);
		}
		return new ArrayList<>();
	}

}
