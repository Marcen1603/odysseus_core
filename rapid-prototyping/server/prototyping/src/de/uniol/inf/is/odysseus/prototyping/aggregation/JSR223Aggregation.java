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
package de.uniol.inf.is.odysseus.prototyping.aggregation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

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
 * @author Christian Kuka <christian@kuka.cc>
 * @version 1.0
 *
 */
public class JSR223Aggregation extends AbstractAggregateFunction<Tuple<?>, Tuple<?>> {
    /**
     *
     */
    private static final long serialVersionUID = -8769334296558938027L;

    private static final String KEY_ATTRIBUTE = "attr";
    private static final String KEY_ATTRIBUTES = "attrs";
    private static final String KEY_META = "meta";
    private static final String KEY_PARTIAL = "partial";
    private static final String KEY_PARTIALS = "partials";
    private static final Logger LOG = LoggerFactory.getLogger(JSR223Aggregation.class);

    /** The script engine */
    private final ScriptEngine engine;
    /** The compiled script if supported */
    private CompiledScript compiledScript;
    /** The script reader **/
    private InputStreamReader script;
    /** The path to the script file */
    private final String fileName;
    /** Position array to support multiple attributes for aggregation */
    private final int[] positions;

    final private String datatype;

    /**
     *
     * @param pos
     * @param fileName
     */
    public JSR223Aggregation(final int pos, final String fileName, final boolean partialAggregateInput, final String datatype) {
        this(new int[] { pos }, fileName, partialAggregateInput, datatype);
    }

    /**
     *
     * @param pos
     * @param fileName
     */
    public JSR223Aggregation(final int[] pos, final String fileName, final boolean partialAggregateInput, final String datatype) {
        super("JSR223Aggregation", partialAggregateInput);
        this.positions = pos;
        this.datatype = datatype;
        this.fileName = fileName;
        final String extension = this.fileName.substring(this.fileName.lastIndexOf(".") + 1);
        final ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByExtension(extension);
        if (this.engine == null) {
            JSR223Aggregation.LOG.error("No scripting engine was found");
        }
        if (JSR223Aggregation.LOG.isDebugEnabled()) {
            final List<ScriptEngineFactory> engines = manager.getEngineFactories();
            JSR223Aggregation.LOG.debug("The following " + engines.size() + " scripting engines were found");
            for (final ScriptEngineFactory engineFactory : engines) {
                JSR223Aggregation.LOG.debug("Engine name: {}", engineFactory.getEngineName());
                JSR223Aggregation.LOG.debug("\tVersion: {}", engineFactory.getEngineVersion());
                JSR223Aggregation.LOG.debug("\tLanguage: {}", engineFactory.getLanguageName());
                final List<String> extensions = engineFactory.getExtensions();
                if (extensions.size() > 0) {
                    JSR223Aggregation.LOG.debug("\tEngine supports the following extensions:");
                    for (final String e : extensions) {
                        JSR223Aggregation.LOG.debug("\t\t{}", e);
                    }
                }
                final List<String> shortNames = engineFactory.getNames();
                if (shortNames.size() > 0) {
                    JSR223Aggregation.LOG.debug("\tEngine has the following short names:");
                    for (final String n : engineFactory.getNames()) {
                        JSR223Aggregation.LOG.debug("\t\t{}", n);
                    }
                }
                JSR223Aggregation.LOG.debug("=========================");
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
            JSR223Aggregation.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple<?> in) {
        Tuple<?> ret = null;
        final Bindings bindings = this.engine.createBindings();
        final Object[] attributes = JSR223Aggregation.getAttributes(in, this.positions);
        bindings.put(JSR223Aggregation.KEY_ATTRIBUTES, new Integer(attributes.length));
        for (int i = 0; i < attributes.length; ++i) {
            bindings.put(JSR223Aggregation.KEY_ATTRIBUTE + i, attributes[i]);
            bindings.put(JSR223Aggregation.KEY_PARTIAL + i, attributes[i]);
        }
        bindings.put(JSR223Aggregation.KEY_META, in.getMetadata());
        bindings.put(JSR223Aggregation.KEY_PARTIALS, new Integer(0));
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
            JSR223Aggregation.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        final Object[] retObj = new Object[attributes.length];
        for (int i = 0; i < attributes.length; ++i) {
            retObj[i] = bindings.get(JSR223Aggregation.KEY_PARTIAL + i);
        }
        ret = new Tuple(retObj, false);
        final IPartialAggregate<Tuple<?>> pa = new ElementPartialAggregate<Tuple<?>>(ret, this.datatype);
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final Tuple<?> toMerge, final boolean createNew) {
        ElementPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            pa = new ElementPartialAggregate<Tuple<?>>(((ElementPartialAggregate<Tuple<?>>) p).getElem(), this.datatype);
        }
        else {
            pa = (ElementPartialAggregate<Tuple<?>>) p;
        }
        Tuple<?> ret = null;
        final Bindings bindings = this.engine.createBindings();
        final Object[] attributes = JSR223Aggregation.getAttributes(toMerge, this.positions);
        bindings.put(JSR223Aggregation.KEY_ATTRIBUTES, new Integer(attributes.length));
        for (int i = 0; i < attributes.length; ++i) {
            bindings.put(JSR223Aggregation.KEY_ATTRIBUTE + i, attributes[i]);
        }
        bindings.put(JSR223Aggregation.KEY_META, toMerge.getMetadata());

        final Object[] partials = pa.getElem().getAttributes();
        bindings.put(JSR223Aggregation.KEY_PARTIALS, new Integer(partials.length));
        for (int i = 0; i < partials.length; ++i) {
            bindings.put(JSR223Aggregation.KEY_PARTIAL + i, partials[i]);
        }

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
            JSR223Aggregation.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        final Object[] retObj = new Object[partials.length];
        for (int i = 0; i < partials.length; ++i) {
            retObj[i] = bindings.get(JSR223Aggregation.KEY_PARTIAL + i);
        }
        ret = new Tuple(retObj, false);

        pa.setElem(ret);
        return pa;
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Tuple<?> evaluate(final IPartialAggregate<Tuple<?>> p) {
        final ElementPartialAggregate<Tuple<?>> pa = new ElementPartialAggregate<Tuple<?>>(((ElementPartialAggregate<Tuple<?>>) p).getElem(), this.datatype);

        Tuple<?> ret = null;
        final Bindings bindings = this.engine.createBindings();
        bindings.put(JSR223Aggregation.KEY_ATTRIBUTES, new Integer(0));

        bindings.put(JSR223Aggregation.KEY_META, pa.getElem().getMetadata());

        final Object[] partials = pa.getElem().getAttributes();
        bindings.put(JSR223Aggregation.KEY_PARTIALS, new Integer(partials.length));
        for (int i = 0; i < partials.length; ++i) {
            bindings.put(JSR223Aggregation.KEY_PARTIAL + i, partials[i]);
        }

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
            JSR223Aggregation.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        final Object[] retObj = new Object[partials.length];
        for (int i = 0; i < partials.length; ++i) {
            retObj[i] = bindings.get(JSR223Aggregation.KEY_PARTIAL + i);
        }
        ret = new Tuple(retObj, false);
        return ret;
    }

    /**
     * Return the attributes from the tuple
     *
     * @param in
     * @param positions
     * @return
     */
    private static Object[] getAttributes(final Tuple<?> in, final int[] positions) {
        Objects.requireNonNull(in);
        Objects.requireNonNull(positions);
        final Object[] attributes = new Object[positions.length];
        for (int i = 0; i < positions.length; ++i) {
            attributes[i] = in.getAttribute(positions[i]);
        }
        return attributes;
    }

}
