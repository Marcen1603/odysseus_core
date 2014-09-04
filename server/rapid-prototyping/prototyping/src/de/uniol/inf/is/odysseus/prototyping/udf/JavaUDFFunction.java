package de.uniol.inf.is.odysseus.prototyping.udf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;

/**
 * UDF for Java source code based on the work of Sergey Malenkov
 * (https://weblogs.java.net/blog/malenkov)
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@UserDefinedFunction(name = "Java")
public class JavaUDFFunction implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

    private static final Logger LOG = LoggerFactory.getLogger(JavaUDFFunction.class);

    /** The process method. */
    private Method udfMethod;

    private Class<?> udfClass;

    private Object udfInstance;

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void init(final String initString) {
        Objects.requireNonNull(initString);
        final String path = initString;
        final File javaFile = new File(path);
        final String fileName = javaFile.getName();
        final String className = fileName.substring(0, fileName.indexOf("."));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(javaFile)))) {
            final StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            if (JavaUDFFunction.LOG.isDebugEnabled()) {
                JavaUDFFunction.LOG.debug(content.toString());
            }
            final UDFClassLoader udfClassLoader = new UDFClassLoader(className, content.toString());
            this.udfClass = udfClassLoader.loadClass(className);
            this.udfMethod = this.udfClass.getMethod("process", new Class[] { Object[].class, Map.class });
            this.udfInstance = this.udfClass.newInstance();
        }
        catch (IOException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            JavaUDFFunction.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Tuple<? extends IMetaAttribute> process(final Tuple<? extends IMetaAttribute> in, final int port) {
        Tuple<?> ret = null;
        Object[] retObj = new Object[] {};
        try {
            retObj = (Object[]) this.udfMethod.invoke(this.udfInstance, in.getAttributes(), in.getMetadataMap());
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            JavaUDFFunction.LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        ret = new Tuple(retObj, false);
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
