package de.uniol.inf.is.odysseus.prototyping.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject.Kind;
import javax.tools.ToolProvider;

/**
 * UDF for Java source code based on the work of Sergey Malenkov
 * (https://weblogs.java.net/blog/malenkov)
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class JavaPOClassLoader extends ClassLoader {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final MemoryFileManager manager = new MemoryFileManager(this.compiler);

    public JavaPOClassLoader(final String classname, final String filecontent) {
        this(Collections.singletonMap(classname, filecontent));
    }

    public JavaPOClassLoader(final Map<String, String> map) {
        Objects.requireNonNull(map);
        final List<JavaPOSource> list = new ArrayList<>();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            list.add(new JavaPOSource(entry.getKey(), Kind.SOURCE, entry.getValue()));
        }
        if (compiler == null){
        	throw new RuntimeException("No compiler found! Make sure that tools.jar is in your CLASSPATH.");
        }
        this.compiler.getTask(null, this.manager, null, null, null, list).call();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        Objects.requireNonNull(name);
        synchronized (this.manager) {
            final JavaPOOutput output = this.manager.remove(name);
            if (output != null) {
                final byte[] array = output.toByteArray();
                return this.defineClass(name, array, 0, array.length);
            }
        }
        return super.findClass(name);
    }
}
