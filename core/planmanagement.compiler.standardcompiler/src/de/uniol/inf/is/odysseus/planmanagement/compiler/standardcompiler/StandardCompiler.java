package de.uniol.inf.is.odysseus.planmanagement.compiler.standardcompiler;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.planmanagement.IRewrite;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.util.LoggerHelper;
import de.uniol.inf.is.odysseus.util.PrintGraphVisitor;

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
	 * @see de.uniol.inf.is.odysseus.planmanagement.IInfoProvider#getInfos()
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
	 * @see de.uniol.inf.is.odysseus.planmanagement.ICompiler#translateQuery(java.lang.String, java.lang.String)
	 */
	@Override
	public List<IQuery> translateQuery(String query,
			String parserID, User user) throws QueryParseException {
		if (this.parserList.containsKey(parserID)) {
			return this.parserList.get(parserID)
					.parse(query, user);
		}

		throw new QueryParseException("Parser with ID " + parserID
				+ " not registered.");
	}


//	/* (non-Javadoc)
//	 * @see de.uniol.inf.is.odysseus.planmanagement.ICompiler#transform(de.uniol.inf.is.odysseus.ILogicalOperator, de.uniol.inf.is.odysseus.TransformationConfiguration)
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public ArrayList<IPhysicalOperator> transform(ILogicalOperator logicalPlan,
//			TransformationConfiguration transformationConfiguration, User caller)
//			throws TransformationException {
//		// create working copy of plan
//		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>();
//		AbstractGraphWalker walker = new AbstractGraphWalker();
//		walker.prefixWalk(logicalPlan, copyVisitor);
//		ILogicalOperator copyPlan = copyVisitor.getResult();
//		return this.transformation.transform(copyPlan, transformationConfiguration, caller);
//	}

	@Override
	public void transform(IQuery query,
			TransformationConfiguration transformationConfiguration, User caller) throws TransformationException {
//		System.err.println("TRANSFORMING QUERY");
//		
//		System.err.println("OLD PLAN: TREE WALKER");
//		AbstractTreeWalker walker2 = new AbstractTreeWalker();
//		String result = walker2.prefixWalk(query.getLogicalPlan(), new AlgebraPlanToStringVisitor());
//		System.err.println(result);
//		
//		AbstractGraphWalker walker = new AbstractGraphWalker();
//		System.err.println("OLD PLAN");
//		PrintGraphVisitor visitor = new PrintGraphVisitor();
//		walker.prefixWalk(query.getLogicalPlan(), visitor);
//		System.err.println(visitor.getResult());

		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(query);
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalk(query.getLogicalPlan(), copyVisitor);
		ILogicalOperator copyPlan = copyVisitor.getResult();
		
//		walker = new AbstractGraphWalker();
//		System.err.println("COPIED PLAN");
//		visitor = new PrintGraphVisitor();
//		walker.prefixWalk(copyPlan, visitor);
//		System.err.println(visitor.getResult());

		query.initializePhysicalRoots(this.transformation.transform(copyPlan, transformationConfiguration, caller));
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.ICompiler#getSupportedQueryParser()
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
	

//	@Override
//	public List<ILogicalOperator> createAlternativePlans(
//			ILogicalOperator logicalPlan, OptimizationConfiguration conf) {
//		// TODO mehrere Alternativen zu dem aktuellen Plan muessen generiert
//		// werden, z.B. durch Join-Vertauschungen
//		ILogicalOperator p = rewritePlan(logicalPlan, conf.getRewriteConfiguration());
//		List<ILogicalOperator> list = new ArrayList<ILogicalOperator>(1);
//		list.add(p);
//		return list;
//	}

//	@Override
//	public List<List<IPhysicalOperator>> transformWithAlternatives(
//			ILogicalOperator logicalPlan,
//			TransformationConfiguration transformationConfiguration, User caller)
//			throws TransformationException {
//		// TODO mehrere Alternativen muessen generiert werden, z.B. durch
//		// verschiedene Join-Implementationen
//		ArrayList<IPhysicalOperator> p = transform(logicalPlan,
//				transformationConfiguration, caller);
//		List<List<IPhysicalOperator>> list = new ArrayList<List<IPhysicalOperator>>(1);
//		list.add(p);
//		return list;
//	}
	
	@Override
	public void addCompilerListener(ICompilerListener listener) {
		this.listener.add(listener);
	}
	
	@Override
	public void removeCompilerListener(ICompilerListener listener) {
		this.listener.remove(listener);
	}


	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan, RewriteConfiguration conf) {
		return rewrite.rewritePlan(plan, conf);
	}


	@Override
	public List<IQuery> translateAndTransformQuery(String query,
			String parserID, User user, TransformationConfiguration transformationConfiguration) throws QueryParseException, TransformationException {
		List<IQuery> translate = translateQuery(query, parserID, user);
		for (IQuery q:translate){
			transform(q, transformationConfiguration, user);
		}
		return translate;
	}



}
