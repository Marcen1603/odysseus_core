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
package de.uniol.inf.is.odysseus.prototyping.aggregation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

/**
 * Aggregation function to use arbitrary script languages for aggregation
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @version 1.0
 * 
 */
public class JSR223Aggregation extends
		AbstractAggregateFunction<Tuple<?>, Tuple<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8769334296558938027L;

	private static final String KEY_ATTRIBUTE = "attr";
	private static final String KEY_ATTRIBUTES = "attrs";
	private static final String KEY_META = "meta";
	private static final String KEY_PARTIAL = "partial";
	private static final String KEY_PARTIALS = "partials";
	private final Logger LOG = LoggerFactory.getLogger(JSR223Aggregation.class);

	/** The script engine */
	private ScriptEngine engine;
	/** The compiled script if supported */
	private CompiledScript compiledScript;
	/** The script reader **/
	private InputStreamReader script;
	/** The path to the script file */
	private String fileName;
	/** Position array to support multiple attributes for aggregation */
	private int[] positions;

	/**
	 * 
	 * @param pos
	 * @param fileName
	 */
	public JSR223Aggregation(int pos, String fileName) {
		this(new int[] { pos }, fileName);
	}

	/**
	 * 
	 * @param pos
	 * @param fileName
	 */
	public JSR223Aggregation(int[] pos, String fileName) {
		super("JSR223Aggregation");
		this.positions = pos;
		this.fileName = fileName;
		String extension = this.fileName.substring(this.fileName
				.lastIndexOf(".") + 1);
		ScriptEngineManager manager = new ScriptEngineManager();
		this.engine = manager.getEngineByExtension(extension);
		if (this.engine == null) {
			LOG.error("No scripting engine was found");
		}
		if (LOG.isDebugEnabled()) {
			List<ScriptEngineFactory> engines = manager.getEngineFactories();
			LOG.debug("The following " + engines.size()
					+ " scripting engines were found");
			for (ScriptEngineFactory engine : engines) {
				LOG.debug("Engine name: {}", engine.getEngineName());
				LOG.debug("\tVersion: {}", engine.getEngineVersion());
				LOG.debug("\tLanguage: {}", engine.getLanguageName());
				List<String> extensions = engine.getExtensions();
				if (extensions.size() > 0) {
					LOG.debug("\tEngine supports the following extensions:");
					for (String e : extensions) {
						LOG.debug("\t\t{}", e);
					}
				}
				List<String> shortNames = engine.getNames();
				if (shortNames.size() > 0) {
					LOG.debug("\tEngine has the following short names:");
					for (String n : engine.getNames()) {
						LOG.debug("\t\t{}", n);
					}
				}
				LOG.debug("=========================");
			}
		}
		File scriptFile = new File(this.fileName);
		try {
			this.script = new InputStreamReader(new FileInputStream(scriptFile));
			if ((this.compiledScript == null) && (engine instanceof Compilable)) {
				try {
					this.compiledScript = ((Compilable) engine)
							.compile(this.script);
				} catch (ScriptException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .IInitializer#init(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		Tuple<?> ret = null;
		Bindings bindings = this.engine.createBindings();
		Object[] attributes = getAttributes(in, positions);
		bindings.put(KEY_ATTRIBUTES, attributes.length);
		for (int i = 0; i < attributes.length; ++i) {
			bindings.put(KEY_ATTRIBUTE + i, attributes[i]);
			bindings.put(KEY_PARTIAL + i, attributes[i]);
		}
		bindings.put(KEY_META, in.getMetadata());
		bindings.put(KEY_PARTIALS, 0);
		try {
			if (this.compiledScript != null) {
				this.compiledScript.eval(bindings);
			} else {
				if (script != null) {
					engine.eval(this.script, bindings);
				}
			}
		} catch (ScriptException e) {
			LOG.error(e.getMessage(), e);
		}

		Object[] retObj = new Object[attributes.length];
		for (int i = 0; i < attributes.length; ++i) {
			retObj[i] = bindings.get(KEY_PARTIAL + i);
		}
		ret = new Tuple(retObj);
		IPartialAggregate<Tuple<?>> pa = new ElementPartialAggregate<Tuple<?>>(
				ret);
		return pa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .IMerger
	 * #merge(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate
	 * .basefunctions .IPartialAggregate, java.lang.Object, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p,
			Tuple<?> toMerge, boolean createNew) {
		ElementPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			pa = new ElementPartialAggregate<Tuple<?>>(
					((ElementPartialAggregate<Tuple<?>>) p).getElem());
		} else {
			pa = (ElementPartialAggregate<Tuple<?>>) p;
		}
		Tuple<?> ret = null;
		Bindings bindings = this.engine.createBindings();
		Object[] attributes = getAttributes(toMerge, positions);
		bindings.put(KEY_ATTRIBUTES, attributes.length);
		for (int i = 0; i < attributes.length; ++i) {
			bindings.put(KEY_ATTRIBUTE + i, attributes[i]);
		}
		bindings.put(KEY_META, toMerge.getMetadata());

		Object[] partials = pa.getElem().getAttributes();
		bindings.put(KEY_PARTIALS, partials.length);
		for (int i = 0; i < partials.length; ++i) {
			bindings.put(KEY_PARTIAL + i, partials[i]);
		}

		try {
			if (this.compiledScript != null) {
				this.compiledScript.eval(bindings);
			} else {
				if (script != null) {
					engine.eval(this.script, bindings);
				}
			}
		} catch (ScriptException e) {
			LOG.error(e.getMessage(), e);
		}
		Object[] retObj = new Object[partials.length];
		for (int i = 0; i < partials.length; ++i) {
			retObj[i] = bindings.get(KEY_PARTIAL + i);
		}
		ret = new Tuple(retObj);

		pa.setElem(ret);
		return pa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
	 * .IEvaluator
	 * #evaluate(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate
	 * .basefunctions.IPartialAggregate)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		ElementPartialAggregate<Tuple<?>> pa = new ElementPartialAggregate<Tuple<?>>(
				((ElementPartialAggregate<Tuple<?>>) p).getElem());

		Tuple<?> ret = null;
		Bindings bindings = this.engine.createBindings();
		bindings.put(KEY_ATTRIBUTES, 0);

		bindings.put(KEY_META, pa.getElem().getMetadata());

		Object[] partials = pa.getElem().getAttributes();
		bindings.put(KEY_PARTIALS, partials.length);
		for (int i = 0; i < partials.length; ++i) {
			bindings.put(KEY_PARTIAL + i, partials[i]);
		}

		try {
			if (this.compiledScript != null) {
				this.compiledScript.eval(bindings);
			} else {
				if (script != null) {
					engine.eval(this.script, bindings);
				}
			}
		} catch (ScriptException e) {
			LOG.error(e.getMessage(), e);
		}
		Object[] retObj = new Object[partials.length];
		for (int i = 0; i < partials.length; ++i) {
			retObj[i] = bindings.get(KEY_PARTIAL + i);
		}
		ret = new Tuple(retObj);
		return ret;
	}

	/**
	 * Return the attributes from the tuple
	 * 
	 * @param in
	 * @param positions
	 * @return
	 */
	private static Object[] getAttributes(Tuple<?> in, int[] positions) {
		Object[] attributes = new Object[positions.length];
		for (int i = 0; i < positions.length; ++i) {
			attributes[i] = in.getAttribute(positions[i]);
		}
		return attributes;
	}

}
