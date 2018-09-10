package de.uniol.inf.is.odysseus.prototyping.physicaloperator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private final Map<String, JavaPOOutput> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    MemoryFileManager(final JavaCompiler compiler) {
        super(compiler.getStandardFileManager(null, null, null));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind, final FileObject sibling) throws IOException {
        Objects.requireNonNull(className);
        final JavaPOOutput output = new JavaPOOutput(className, kind);
        this.map.put(className, output);
        return output;
    }

    public JavaPOOutput remove(final String className) {
        Objects.requireNonNull(className);
        return this.map.remove(className);
    }
}
