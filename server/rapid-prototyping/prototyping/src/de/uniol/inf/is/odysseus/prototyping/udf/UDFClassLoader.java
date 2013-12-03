package de.uniol.inf.is.odysseus.prototyping.udf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    public UDFClassLoader(String classname, String filecontent) {
        this(Collections.singletonMap(classname, filecontent));
    }

    public UDFClassLoader(Map<String, String> map) {
        List<UDFSource> list = new ArrayList<UDFSource>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(new UDFSource(entry.getKey(), Kind.SOURCE, entry.getValue()));
        }
        this.compiler.getTask(null, this.manager, null, null, null, list).call();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        synchronized (this.manager) {
            UDFOutput output = this.manager.remove(name);
            if (output != null) {
                byte[] array = output.toByteArray();
                return defineClass(name, array, 0, array.length);
            }
        }
        return super.findClass(name);
    }
}
