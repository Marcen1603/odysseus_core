package de.uniol.inf.is.odysseus.prototyping.physicaloperator;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * UDF for Java source code based on the work of Sergey Malenkov
 * (https://weblogs.java.net/blog/malenkov)
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class JavaPOSource extends SimpleJavaFileObject {
    private final String content;

    JavaPOSource(final String className, final Kind kind, final String content) {
        super(URI.create("memo:///" + className.replace('.', '/') + kind.extension), kind);
        this.content = content;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public CharSequence getCharContent(final boolean ignore) {
        return this.content;
    }

}
