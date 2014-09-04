package de.uniol.inf.is.odysseus.prototyping.udf;

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
public class UDFClassLoader extends ClassLoader {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final MemoryFileManager manager = new MemoryFileManager(this.compiler);

    public UDFClassLoader(final String classname, final String filecontent) {
        this(Collections.singletonMap(classname, filecontent));
    }

    public UDFClassLoader(final Map<String, String> map) {
        Objects.requireNonNull(map);
        final List<UDFSource> list = new ArrayList<>();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            list.add(new UDFSource(entry.getKey(), Kind.SOURCE, entry.getValue()));
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
            final UDFOutput output = this.manager.remove(name);
            if (output != null) {
                final byte[] array = output.toByteArray();
                return this.defineClass(name, array, 0, array.length);
            }
        }
        return super.findClass(name);
    }
}
