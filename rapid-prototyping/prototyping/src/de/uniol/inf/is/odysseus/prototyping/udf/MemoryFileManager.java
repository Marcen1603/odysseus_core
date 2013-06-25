package de.uniol.inf.is.odysseus.prototyping.udf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

/**
 * UDF for Java source code based on the work of Sergey Malenkov
 * (https://weblogs.java.net/blog/malenkov)
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings("rawtypes")
public class MemoryFileManager extends ForwardingJavaFileManager {
    private final Map<String, UDFOutput> map = new HashMap<String, UDFOutput>();

    @SuppressWarnings("unchecked")
    MemoryFileManager(JavaCompiler compiler) {
        super(compiler.getStandardFileManager(null, null, null));
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
        UDFOutput output = new UDFOutput(className, kind);
        this.map.put(className, output);
        return output;
    }

    public UDFOutput remove(String className) {
        return this.map.remove(className);
    }
}
