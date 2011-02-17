/** Copyright [2011] [The Odysseus Team]
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.base.CopyLogicalPlanVisitor;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.IRewrite;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.UpdateLogicalPlanVisitor;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
/**
 * The StandardCompiler is a standard implementation for {@link ICompiler}. It
 * provides features to edit queries (e. g. translate, rewrite, transform). This
 * class stores a list of {@link IQueryParser} services, a
 * {@link ITransformation} and a {@link IRewrite} service.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
@Component(immediate = true)
@Service(value = ICompiler.class)
@References(value = {
	@Reference(name = "ITransformation", referenceInterface = ITransformation.class, bind = "bindTransformation", unbind = "unbindTransformation", cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC),
	@Reference(name = "IQueryParser", referenceInterface = IQueryParser.class, bind = "bindParser", unbind = "unbindParser", cardinality = ReferenceCardinality.MANDATORY_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
	@Reference(name = "IRewrite", referenceInterface = IRewrite.class, bind = "bindRewrite", unbind = "unbindRewrite", cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.DYNAMIC) })
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
	 * @param parser new {@link IQueryParser} service
	 */
	public void bindParser(IQueryParser parser) {
		synchronized (this.parserList) {
			this.parserList.put(parser.getLanguage(), parser);
		}
	}

	/**
	 * Method to unbind a {@link IQueryParser}. Used by OSGi.
	 * 
	 * @param parser {@link IQueryParser} service to unbind
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
	 * @param transformation new {@link ITransformation} service
	 */
	public void bindTransformation(ITransformation transformation) {
		this.transformation = transformation;
	}

	/**
	 * Method to unbind a {@link ITransformation}. Used by OSGi.
	 * 
	 * @param transformation {@link ITransformation} service to unbind
	 */
	public void unbindTransformation(ITransformation transformation) {
		if (this.transformation == transformation) {
			this.transformation = null;
		}
	}

	/**
	 * Method to bind a {@link IRewrite}. Used by OSGi.
	 * 
	 * @param rewrite new {@link IRewrite} service
	 */
	public void bindRewrite(IRewrite rewrite) {
		this.rewrite = rewrite;
	}

	/**
	 * Method to unbind a {@link IRewrite}. Used by OSGi.
	 * 
	 * @param rewrite {@link IRewrite} service to unbind
	 */
	public void unbindRewrite(IRewrite rewrite) {
		if (this.rewrite == rewrite) {
			this.rewrite = null;
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider#getInfos()
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.ICompiler#translateQuery(java.lang.String, java.lang.String)
	 */
	@Override
	public ArrayList<ILogicalOperator> translateQuery(String query,
			String parserID) throws QueryParseException {
		if (this.parserList.containsKey(parserID)) {
			return (ArrayList<ILogicalOperator>) this.parserList.get(parserID)
					.parse(query);
		}

		throw new QueryParseException("Parser with ID " + parserID
				+ " not registered.");
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.ICompiler#restructPlan(de.uniol.inf.is.odysseus.base.ILogicalOperator)
	 */
	@Override
	public ILogicalOperator restructPlan(ILogicalOperator logicalPlan) {
		return this.rewrite.rewritePlan(logicalPlan);
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.ICompiler#restructPlan(de.uniol.inf.is.odysseus.base.ILogicalOperator)
	 */
	@Override
	public ILogicalOperator restructPlan(ILogicalOperator logicalPlan, Set<String> rulesToUse) {
		return this.rewrite.rewritePlan(logicalPlan, rulesToUse);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.ICompiler#transform(de.uniol.inf.is.odysseus.base.ILogicalOperator, de.uniol.inf.is.odysseus.base.TransformationConfiguration)
	 */
	@Override
	public IPhysicalOperator transform(ILogicalOperator logicalPlan,
			TransformationConfiguration transformationConfiguration)
			throws TransformationException {
		// create working copy of plan
		CopyLogicalPlanVisitor v = new CopyLogicalPlanVisitor();
		ILogicalOperator cPlan = AbstractTreeWalker.prefixWalk(logicalPlan,v);
		AbstractTreeWalker.prefixWalk(cPlan, new UpdateLogicalPlanVisitor(v.getReplaced()));
		return this.transformation.transform(cPlan, transformationConfiguration);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.ICompiler#getSupportedQueryParser()
	 */
	@Override
	public Set<String> getSupportedQueryParser() {
		return Collections.unmodifiableSet(this.parserList.keySet());
	}

	@Override
	public List<ILogicalOperator> createAlternativePlans(
			ILogicalOperator logicalPlan, Set<String> rulesToUse) {
		// TODO mehrere Alternativen zu dem aktuellen Plan muessen generiert
		// werden, z.B. durch Join-Vertauschungen
		ILogicalOperator p = restructPlan(logicalPlan);
		List<ILogicalOperator> list = new ArrayList<ILogicalOperator>(1);
		list.add(p);
		return list;
	}

	@Override
	public List<IPhysicalOperator> transformWithAlternatives(
			ILogicalOperator logicalPlan,
			TransformationConfiguration transformationConfiguration)
			throws TransformationException {
		// TODO mehrere Alternativen muessen generiert werden, z.B. durch
		// verschiedene Join-Implementationen
		IPhysicalOperator p = transform(logicalPlan,
				transformationConfiguration);
		List<IPhysicalOperator> list = new ArrayList<IPhysicalOperator>(1);
		list.add(p);
		return list;
	}
}
