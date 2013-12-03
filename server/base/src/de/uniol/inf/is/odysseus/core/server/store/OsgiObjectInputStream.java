/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.store;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.Activator;
import de.uniol.inf.is.odysseus.core.util.BundleClassLoading;

public class OsgiObjectInputStream extends ObjectInputStream {

    public OsgiObjectInputStream(FileInputStream fileInputStream) throws IOException {
        super(fileInputStream);
    }

    public OsgiObjectInputStream(InputStream inputStream) throws IOException {
    	super(inputStream);
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
