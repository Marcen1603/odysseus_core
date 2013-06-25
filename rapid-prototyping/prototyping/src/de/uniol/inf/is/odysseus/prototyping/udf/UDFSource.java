package de.uniol.inf.is.odysseus.prototyping.udf;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * UDF for Java source code based on the work of Sergey Malenkov
 * (https://weblogs.java.net/blog/malenkov)
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class UDFSource extends SimpleJavaFileObject {
    private final String content;

    UDFSource(String className, Kind kind, String content) {
        super(URI.create("memo:///" + className.replace('.', '/') + kind.extension), kind);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignore) {
        return this.content;
    }

}
