/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @version 1.0
 * 
 */
public class AggregationJSR223 extends
		AbstractAggregateFunction<RelationalTuple<?>> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String KEY_ATTRIBUTE = "attr";
	public static final String KEY_META = "meta";
	public static final String KEY_PARTIAL = "partial";

	private ScriptEngine engine;
	private CompiledScript script;
	private String fileName;
	private int pos;

	public AggregationJSR223(int pos, String name) {
		super(name);
		this.pos = pos;
		this.fileName = name;
		String extension = name.substring(name.lastIndexOf(".") + 1);
		ScriptEngineManager manager = new ScriptEngineManager();
		this.engine = manager.getEngineByExtension(extension);
		if (this.engine == null) {
			this.engine = manager.getEngineByName("js");
		}
		List<ScriptEngineFactory> engines = manager.getEngineFactories();
		if (engines.isEmpty()) {
			logger.debug("No scripting engines were found");
		} else {
			logger.debug("The following " + engines.size()
					+ " scripting engines were found");
			for (ScriptEngineFactory engine : engines) {
				logger.debug("Engine name: {}", engine.getEngineName());
				logger.debug(" \tVersion: {}", engine.getEngineVersion());
				logger.debug("\tLanguage: {}", engine.getLanguageName());
				List<String> extensions = engine.getExtensions();
				if (extensions.size() > 0) {
					logger.debug("\tEngine supports the following extensions:");
					for (String e : extensions) {
						logger.debug("\t\t{}", e);
					}
				}
				List<String> shortNames = engine.getNames();
				if (shortNames.size() > 0) {
					logger.debug("\tEngine has the following short names:");
					for (String n : engine.getNames()) {
						logger.debug("\t\t{}", n);
					}
				}
				logger.debug("=========================");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions
	 * .IInitializer#init(java.lang.Object)
	 */
	@Override
	public IPartialAggregate<RelationalTuple<?>> init(RelationalTuple<?> in) {
		Bindings bindings = this.engine.createBindings();
		for (int i = 0; i < in.getAttributeCount(); ++i) {
			bindings.put(KEY_ATTRIBUTE + i, in.getAttribute(i));
		}
		bindings.put(KEY_META, in.getMetadata());
		Double partial = new Double(0);
		PartialAggregateData result = new PartialAggregateData();
		result.setPartial(partial);
		bindings.put(KEY_PARTIAL, partial);
		try {
			InputStream is = this.getClass().getResourceAsStream(this.fileName);
			if ((this.script == null) && (engine instanceof Compilable)) {
				this.script = ((Compilable) engine)
						.compile(new InputStreamReader(is));
			}
			if (this.script != null) {
				this.script.eval(bindings);
			} else {
				engine.eval(new InputStreamReader(is), bindings);
			}
		} catch (ScriptException e) {
			logger.error(e.getMessage(), e);
		}
		result.setPartial(bindings.get(KEY_PARTIAL));
		return result;
	}

	@Override
	public IPartialAggregate<RelationalTuple<?>> merge(
			IPartialAggregate<RelationalTuple<?>> partial,
			RelationalTuple<?> in, boolean create) {
		Bindings bindings = this.engine.createBindings();
		for (int i = 0; i < in.getAttributeCount(); ++i) {
			bindings.put(KEY_ATTRIBUTE + i, in.getAttribute(i));
		}
		bindings.put(KEY_META, in.getMetadata());
		// TODO What is "create"? And when it is called?
		// if (create) {
		// ((PartialAggregateData) partial).setPartial(new Double(0));
		// }
		bindings
				.put(KEY_PARTIAL, ((PartialAggregateData) partial).getPartial());

		try {
			InputStream is = this.getClass().getResourceAsStream(this.fileName);
			if ((this.script == null) && (engine instanceof Compilable)) {
				this.script = ((Compilable) engine)
						.compile(new InputStreamReader(is));
			}
			if (this.script != null) {
				this.script.eval(bindings);
			} else {
				this.engine.eval(new InputStreamReader(is), bindings);
			}
		} catch (ScriptException e) {
			logger.error(e.getMessage(), e);
		}
		((PartialAggregateData) partial).setPartial(bindings.get(KEY_PARTIAL));
		return partial;
	}

	@Override
	public RelationalTuple<?> evaluate(
			IPartialAggregate<RelationalTuple<?>> partial) {
		Bindings bindings = this.engine.createBindings();
		bindings.put(KEY_META, null);
		bindings
				.put(KEY_PARTIAL, ((PartialAggregateData) partial).getPartial());
		RelationalTuple<?> result = new RelationalTuple(1);
		try {
			InputStream is = this.getClass().getResourceAsStream(this.fileName);
			if ((this.script == null) && (engine instanceof Compilable)) {
				this.script = ((Compilable) engine)
						.compile(new InputStreamReader(is));
			}
			if (this.script != null) {
				this.script.eval(bindings);
			} else {
				engine.eval(new InputStreamReader(is), bindings);
			}
		} catch (ScriptException e) {
			logger.error(e.getMessage(), e);
		}
		result.setAttribute(0, bindings.get(KEY_PARTIAL));
		return result;
	}

	private class PartialAggregateData implements
			IPartialAggregate<RelationalTuple<?>> {
		private Object partial = null;

		public PartialAggregateData() {

		}

		public PartialAggregateData(PartialAggregateData instance) {
			this.partial = instance.getPartial();
		}

		public Object getPartial() {
			return partial;
		}

		public void setPartial(Object partial) {
			this.partial = partial;
		}

		public PartialAggregateData clone() {
			return new PartialAggregateData(this);
		}

	}
}
