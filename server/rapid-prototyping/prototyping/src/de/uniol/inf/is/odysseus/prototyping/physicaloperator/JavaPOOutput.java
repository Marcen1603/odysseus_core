package de.uniol.inf.is.odysseus.prototyping.physicaloperator;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * UDF for Java source code based on the work of Sergey Malenkov
 * (https://weblogs.java.net/blog/malenkov)
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class JavaPOOutput extends SimpleJavaFileObject {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    JavaPOOutput(final String className, final Kind kind) {
        super(URI.create("memo:///" + className.replace('.', '/') + kind.extension), kind);
    }

    byte[] toByteArray() {
        return this.baos.toByteArray();
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ByteArrayOutputStream openOutputStream() {
        return this.baos;
    }

}
