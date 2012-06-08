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
package de.uniol.inf.is.odysseus.prototyping.udf;

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
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;

/**
 * User Defined Function for rapid prototyping using scripts
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@UserDefinedFunction(name = "Script")
public class JSR223UDFunction
		implements
		IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {
	private static final String KEY_ATTRIBUTE = "attr";
	private static final String KEY_ATTRIBUTES = "attrs";
	private static final String KEY_META = "meta";
	private final Logger LOG = LoggerFactory.getLogger(JSR223UDFunction.class);

	/** The script engine */
	private ScriptEngine engine;
	/** The compiled script if supported */
	private CompiledScript compiledScript;
	/** The script reader **/
	private InputStreamReader script;
	/** The path to the script file */
	private String fileName;

	@Override
	public void init(String initString) {
		this.fileName = initString;
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

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<? extends IMetaAttribute> process(
			Tuple<? extends IMetaAttribute> in, int port) {
		Tuple<?> ret = null;
		Bindings bindings = this.engine.createBindings();

		Object[] attributes = in.getAttributes();
		bindings.put(KEY_ATTRIBUTES, attributes.length);
		for (int i = 0; i < attributes.length; ++i) {
			bindings.put(KEY_ATTRIBUTE + i, attributes[i]);
		}
		bindings.put(KEY_META, in.getMetadata());
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
			retObj[i] = bindings.get(KEY_ATTRIBUTE + i);
		}
		ret = new Tuple(retObj, false);
		return ret;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
