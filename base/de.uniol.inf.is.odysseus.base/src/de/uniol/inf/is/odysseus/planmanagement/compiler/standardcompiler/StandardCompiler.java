package de.uniol.inf.is.odysseus.planmanagement.compiler.standardcompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
import de.uniol.inf.is.odysseus.base.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

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
	 * @param parser new {@link IQueryParser} service
	 */
	public void bindParser(IQueryParser parser) {
		synchronized (this.parserList) {
			this.parserList.put(parser.getLanguage(), parser);
		}
		for (ICompilerListener l: listener){
			l.parserBound(parser.getLanguage());
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
		for (ICompilerListener l: listener){
			l.transformationBound();
		}
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
		for (ICompilerListener l: listener){
			l.rewriteBound();
		}

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
	public boolean transformationBound() {
		return transformation != null;
	}


	@Override
	public boolean rewriteBound() {
		return rewrite != null;
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
	
	@Override
	public void addCompilerListener(ICompilerListener listener) {
		this.listener.add(listener);
	}
	
	@Override
	public void removeCompilerListener(ICompilerListener listener) {
		this.listener.remove(listener);
	}


}
