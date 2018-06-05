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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.prototyping.logicaloperator.JavaAO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class JavaPO<M extends IMetaAttribute, T extends Tuple<M>> extends AbstractPipe<T, T> {
    private static final Logger LOG = LoggerFactory.getLogger(JavaPO.class);

    /** The process method. */
    private Method udfMethod;

    private Class<?> udfClass;

    private Object udfInstance;
    /** The path to the Java file */
    private final String path;

    /**
     * Class constructor.
     *
     */
    public JavaPO(final JavaAO operator) {
        this.path = operator.getPath();
        final File javaFile = new File(this.path);
        final String fileName = javaFile.getName();
        final String className = fileName.substring(0, fileName.indexOf("."));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(javaFile)))) {
            final StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            if (JavaPO.LOG.isDebugEnabled()) {
                JavaPO.LOG.debug(content.toString());
            }
            final JavaPOClassLoader udfClassLoader = new JavaPOClassLoader(className, content.toString());
            this.udfClass = udfClassLoader.loadClass(className);
            this.udfMethod = this.udfClass.getMethod("process", new Class[] { Object[].class, Map.class });
            this.udfInstance = this.udfClass.newInstance();
        }
        catch (IOException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            JavaPO.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Class constructor.
     *
     */
    public JavaPO(final JavaPO<M, T> operator) {
        this.path = operator.path;
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
        Object[] attributes = new Object[] {};
        try {
            attributes = (Object[]) this.udfMethod.invoke(this.udfInstance, object.getAttributes());
        }
        catch (final Throwable e) {
            JavaPO.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        final T ret = (T) new Tuple<>(attributes, false);
        ret.setMetadata((M) object.getMetadata().clone());
        this.transfer(ret);
    }

}
