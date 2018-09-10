/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.prototyping.physicaloperator;

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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.prototyping.logicaloperator.AbstractScriptAO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ScriptPO<M extends IMetaAttribute, T extends Tuple<M>> extends AbstractPipe<T, T> {
    private static final Logger LOG = LoggerFactory.getLogger(ScriptPO.class);

    private static final String KEY_ATTRIBUTE = "attr";
    private static final String KEY_ATTRIBUTES = "attrs";
    private static final String KEY_META = "meta";

    /** The script engine */
    private ScriptEngine engine;
    /** The compiled script if supported */
    private CompiledScript compiledScript;
    /** The script reader **/
    private InputStreamReader script;
    /** The path to the script file */
    private String path;

    /**
     * Class constructor.
     *
     */
    public ScriptPO(final AbstractScriptAO operator) {
        this.path = operator.getPath();
        final String extension = this.path.substring(this.path.lastIndexOf(".") + 1);
        final ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByExtension(extension);
        if (this.engine == null) {
            ScriptPO.LOG.error("No scripting engine was found");
        }
        if (ScriptPO.LOG.isDebugEnabled()) {
            final List<ScriptEngineFactory> engines = manager.getEngineFactories();
            ScriptPO.LOG.debug("The following " + engines.size() + " scripting engines were found");
            for (final ScriptEngineFactory engineFactory : engines) {
                ScriptPO.LOG.debug("Engine name: {}", engineFactory.getEngineName());
                ScriptPO.LOG.debug("\tVersion: {}", engineFactory.getEngineVersion());
                ScriptPO.LOG.debug("\tLanguage: {}", engineFactory.getLanguageName());
                final List<String> extensions = engineFactory.getExtensions();
                if (extensions.size() > 0) {
                    ScriptPO.LOG.debug("\tEngine supports the following extensions:");
                    for (final String e : extensions) {
                        ScriptPO.LOG.debug("\t\t{}", e);
                    }
                }
                final List<String> shortNames = engineFactory.getNames();
                if (shortNames.size() > 0) {
                    ScriptPO.LOG.debug("\tEngine has the following short names:");
                    for (final String n : engineFactory.getNames()) {
                        ScriptPO.LOG.debug("\t\t{}", n);
                    }
                }
                ScriptPO.LOG.debug("=========================");
            }
        }
        final File scriptFile = new File(this.path);
        try {
            this.script = new InputStreamReader(new FileInputStream(scriptFile));
            if ((this.compiledScript == null) && (this.engine instanceof Compilable)) {
                this.compiledScript = ((Compilable) this.engine).compile(this.script);
            }
        }
        catch (FileNotFoundException | ScriptException e) {
            ScriptPO.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public ScriptPO(final ScriptPO<M, T> operator) {
        super(operator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPunctuation(final IPunctuation punctuation, final int port) {
        this.sendPunctuation(punctuation);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final T object, final int port) {
        final Bindings bindings = this.engine.createBindings();

        final Object[] attributes = object.getAttributes();
        bindings.put(ScriptPO.KEY_ATTRIBUTES, new Integer(attributes.length));
        for (int i = 0; i < attributes.length; ++i) {
            bindings.put(ScriptPO.KEY_ATTRIBUTE + i, attributes[i].toString());
        }
        bindings.put(ScriptPO.KEY_META, object.getMetadata());
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
            ScriptPO.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        final Object[] retObj = new Object[attributes.length];
        for (int i = 0; i < attributes.length; ++i) {
            retObj[i] = bindings.get(ScriptPO.KEY_ATTRIBUTE + i);
        }
        final T ret = (T) new Tuple<>(retObj, false);
        if (bindings.get(ScriptPO.KEY_META) != null) {
            ret.setMetadata((M) bindings.get(ScriptPO.KEY_META));
        }
        else {
            ret.setMetadata((M) object.getMetadata().clone());
        }
        this.transfer(ret);
    }

}
