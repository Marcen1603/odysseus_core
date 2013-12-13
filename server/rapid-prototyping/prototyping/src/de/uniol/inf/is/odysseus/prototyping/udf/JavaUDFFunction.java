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
public class JavaUDFFunction
		implements
		IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	private final Logger LOG = LoggerFactory.getLogger(JavaUDFFunction.class);

	/** The process method. */
	private Method udfMethod;

	private Class<?> udfClass;

	private Object udfInstance;

	@Override
	public void init(String initString) {
	    Objects.requireNonNull(initString);
		String path = initString;
		File javaFile = new File(path);
		String fileName = javaFile.getName();
		String className = fileName.substring(0, fileName.indexOf("."));

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(javaFile)));
			StringBuilder content = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
			reader.close();
			if (LOG.isDebugEnabled()) {
				LOG.debug(content.toString());
			}
			UDFClassLoader udfClassLoader = new UDFClassLoader(className,
					content.toString());
			this.udfClass = udfClassLoader.loadClass(className);
			this.udfMethod = this.udfClass.getMethod("process", new Class[] {
					Object[].class, Map.class });
			this.udfInstance = this.udfClass.newInstance();
		} catch (IOException | NoSuchMethodException | SecurityException
				| ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<? extends IMetaAttribute> process(
			Tuple<? extends IMetaAttribute> in, int port) {
		Tuple<?> ret = null;
		Object[] retObj = new Object[] {};
		try {
			retObj = (Object[]) this.udfMethod.invoke(this.udfInstance,
					in.getAttributes(), in.getMetadataMap());
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		ret = new Tuple(retObj, false);
		return ret;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
