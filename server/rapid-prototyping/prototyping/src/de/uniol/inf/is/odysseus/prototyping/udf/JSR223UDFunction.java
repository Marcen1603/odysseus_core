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
public class JSR223UDFunction implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {
    private static final String KEY_ATTRIBUTE = "attr";
    private static final String KEY_ATTRIBUTES = "attrs";
    private static final String KEY_META = "meta";
    private static final Logger LOG = LoggerFactory.getLogger(JSR223UDFunction.class);

    /** The script engine */
    private ScriptEngine engine;
    /** The compiled script if supported */
    private CompiledScript compiledScript;
    /** The script reader **/
    private InputStreamReader script;
    /** The path to the script file */
    private String fileName;

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void init(final String initString) {
        this.fileName = initString;
        final String extension = this.fileName.substring(this.fileName.lastIndexOf(".") + 1);
        final ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByExtension(extension);
        if (this.engine == null) {
            JSR223UDFunction.LOG.error("No scripting engine was found");
        }
        if (JSR223UDFunction.LOG.isDebugEnabled()) {
            final List<ScriptEngineFactory> engines = manager.getEngineFactories();
            JSR223UDFunction.LOG.debug("The following " + engines.size() + " scripting engines were found");
            for (final ScriptEngineFactory engineFactory : engines) {
                JSR223UDFunction.LOG.debug("Engine name: {}", engineFactory.getEngineName());
                JSR223UDFunction.LOG.debug("\tVersion: {}", engineFactory.getEngineVersion());
                JSR223UDFunction.LOG.debug("\tLanguage: {}", engineFactory.getLanguageName());
                final List<String> extensions = engineFactory.getExtensions();
                if (extensions.size() > 0) {
                    JSR223UDFunction.LOG.debug("\tEngine supports the following extensions:");
                    for (final String e : extensions) {
                        JSR223UDFunction.LOG.debug("\t\t{}", e);
                    }
                }
                final List<String> shortNames = engineFactory.getNames();
                if (shortNames.size() > 0) {
                    JSR223UDFunction.LOG.debug("\tEngine has the following short names:");
                    for (final String n : engineFactory.getNames()) {
                        JSR223UDFunction.LOG.debug("\t\t{}", n);
                    }
                }
                JSR223UDFunction.LOG.debug("=========================");
            }
        }
        final File scriptFile = new File(this.fileName);
        try {
            this.script = new InputStreamReader(new FileInputStream(scriptFile));
            if ((this.compiledScript == null) && (this.engine instanceof Compilable)) {
                this.compiledScript = ((Compilable) this.engine).compile(this.script);
            }
        }
        catch (FileNotFoundException | ScriptException e) {
            JSR223UDFunction.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Tuple<? extends IMetaAttribute> process(final Tuple<? extends IMetaAttribute> in, final int port) {
        Tuple<IMetaAttribute> ret = null;
        final Bindings bindings = this.engine.createBindings();

        final Object[] attributes = in.getAttributes();
        bindings.put(JSR223UDFunction.KEY_ATTRIBUTES, new Integer(attributes.length));
        for (int i = 0; i < attributes.length; ++i) {
            bindings.put(JSR223UDFunction.KEY_ATTRIBUTE + i, attributes[i]);
        }
        bindings.put(JSR223UDFunction.KEY_META, in.getMetadata());
        try {
            if (this.compiledScript != null) {
                this.compiledScript.eval(bindings);
            }
            else {
                if (this.script != null) {
                    this.engine.eval(this.script, bindings);
                }
            }
        }
        catch (final ScriptException e) {
            JSR223UDFunction.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        final Object[] retObj = new Object[attributes.length];
        for (int i = 0; i < attributes.length; ++i) {
            retObj[i] = bindings.get(JSR223UDFunction.KEY_ATTRIBUTE + i);
        }
        ret = new Tuple<>(retObj, false);
        if (bindings.get(JSR223UDFunction.KEY_META) != null) {
            ret.setMetadata((IMetaAttribute) bindings.get(JSR223UDFunction.KEY_META));
        }
        else {
            ret.setMetadata(in.getMetadata().clone());
        }
        return ret;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

}
