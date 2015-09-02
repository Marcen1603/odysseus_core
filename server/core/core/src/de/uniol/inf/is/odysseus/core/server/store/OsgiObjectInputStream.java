package de.uniol.inf.is.odysseus.core.server.store;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Use {@code de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream} instead.
 */
@Deprecated
public class OsgiObjectInputStream extends de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream {

	public OsgiObjectInputStream(FileInputStream fileInputStream) throws IOException {
        super(fileInputStream);
    }

    public OsgiObjectInputStream(InputStream inputStream) throws IOException {
    	super(inputStream);
    }
	
}