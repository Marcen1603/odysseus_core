package de.uniol.inf.is.odysseus.core.server.store;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.Activator;
import de.uniol.inf.is.odysseus.core.util.BundleClassLoading;

public class OsgiObjectInputStream extends ObjectInputStream {

    public OsgiObjectInputStream(FileInputStream fileInputStream) throws IOException {
        super(fileInputStream);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(desc, "Desc must not be null!");

        try {
            return BundleClassLoading.findClass(desc.getName(), Activator.getBundleContext().getBundle());
        } catch (ClassNotFoundException e) {
            
        } catch( NullPointerException e ) {
            
        }
        return super.resolveClass(desc);
    }

}
