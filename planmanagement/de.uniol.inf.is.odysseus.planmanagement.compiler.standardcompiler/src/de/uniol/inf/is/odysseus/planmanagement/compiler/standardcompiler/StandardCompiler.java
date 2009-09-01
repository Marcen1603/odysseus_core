package de.uniol.inf.is.odysseus.planmanagement.compiler.standardcompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.IRewrite;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;

/**
 * 
 * @author Wolf Bauer
 *
 */
public class StandardCompiler implements ICompiler {
	protected ITransformation transformation;
	
	protected HashMap<String, IQueryParser> parserList = new HashMap<String, IQueryParser>();
	
	protected IRewrite rewrite;
	
	public String getInfoString(Object object, String label) {
		String infos = AppEnv.LINE_SEPERATOR + label + ": ";
		if (object != null) {
			infos += object.getClass();
		} else {
			infos += "not set. ";
		}
		return infos;
	}

	public void bindParser(IQueryParser parser) {
		synchronized (this.parserList) {
			this.parserList.put(parser.getLanguage(), parser);
		}
	}

	public void unbindParser(IQueryParser parser) {
		synchronized (this.parserList) {
			if (this.parserList.containsKey(parser.getLanguage())) {
				this.parserList.remove(parser.getLanguage());
			}
		}
	}

	public void bindTransformation(ITransformation transformation) {
		this.transformation = transformation;
	}

	public void unbindTransformation(ITransformation transformation) {
		if (this.transformation == transformation) {
			this.transformation = null;
		}
	}

	public void bindRewrite(IRewrite rewrite) {
		this.rewrite = rewrite;
	}

	public void unbindRewrite(IRewrite rewrite) {
		if (this.rewrite == rewrite) {
			this.rewrite = null;
		}
	}

	@Override
	public String getInfos() {
		String infos = "<Compiler class=\"" + this + "\"> ";

		infos += getInfoString(this.transformation, "Transformation comp.:");
		infos += getInfoString(this.rewrite, "Restructure comp.:");
		
		infos += AppEnv.LINE_SEPERATOR + " <Parser> ";
		for (IQueryParser parser : this.parserList.values()) {
			infos += getInfoString(parser, "Parser:");	
		}
		infos += AppEnv.LINE_SEPERATOR + " </Parser>";

		infos += AppEnv.LINE_SEPERATOR + "</Compiler> ";

		return infos;
	}

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

	@Override
	public ILogicalOperator restructPlan(ILogicalOperator logicalPlan) {
		return this.rewrite.rewritePlan(logicalPlan);
	}

	@Override
	public IPhysicalOperator transform(ILogicalOperator logicalPlan, TransformationConfiguration transformationConfiguration) throws TransformationException {
		return this.transformation.transform(logicalPlan, transformationConfiguration);
	}

	@Override
	public Set<String> getSupportedQueryParser() {
		return Collections.unmodifiableSet( this.parserList.keySet());
	}
}
