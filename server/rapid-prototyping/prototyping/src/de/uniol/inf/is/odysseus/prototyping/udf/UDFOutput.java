package de.uniol.inf.is.odysseus.prototyping.udf;

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
public class UDFOutput extends SimpleJavaFileObject {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    UDFOutput(String className, Kind kind) {
        super(URI.create("memo:///" + className.replace('.', '/') + kind.extension), kind);
    }

    byte[] toByteArray() {
        return this.baos.toByteArray();
    }

    @Override
    public ByteArrayOutputStream openOutputStream() {
        return this.baos;
    }

}
