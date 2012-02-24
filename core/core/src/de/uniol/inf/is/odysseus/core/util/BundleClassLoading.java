/** Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.core.util;

import java.util.Collection;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * 
 * @author Dennis Geesen Created at: 17.01.2012
 */
public class BundleClassLoading {

	private static String DEFAULT_PACKAGE_ROOT_DIR = "/bin";

	public static Class<?> findClass(String canonicalClassName, Bundle startBundle) throws ClassNotFoundException {
		// first try to load by default-classloader
		try{
			Class<?> clazz = startBundle.loadClass(canonicalClassName);
			return clazz;
		}catch(Exception e){
			
		}
		
		// then scan wired bundles
		try {
			
			for (Bundle b : startBundle.getBundleContext().getBundles()) {
				String slashedName = canonicalClassName.replace('.', '/');
				int splitPoint = slashedName.lastIndexOf("/");

				String resourcePath = slashedName.substring(0, splitPoint);
				String className = slashedName.substring(splitPoint + 1, slashedName.length());
				resourcePath = DEFAULT_PACKAGE_ROOT_DIR + "/" + resourcePath;
				BundleWiring wiring = b.adapt(BundleWiring.class);
				if (wiring != null) {
					Collection<String> resources = wiring.listResources(resourcePath, className + ".class", BundleWiring.LISTRESOURCES_LOCAL);
					if (resources.size() > 1) {
						throw new ClassNotFoundException("There are more than one possible class for " + canonicalClassName);
					}
					if (resources.size() == 1) {
						String name = canonicalClassName;
						ClassLoader loader = wiring.getClassLoader();
						Class<?> theClass = loader.loadClass(name);
						return theClass;
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new ClassNotFoundException("Can't obtain class: " + canonicalClassName, e);
		}
	}

	public static ClassLoader getBundleClassLoader(Bundle b) {
		try {
			BundleWiring wiring = b.adapt(BundleWiring.class);
			return wiring.getClassLoader();
		} catch (Exception e) {
			throw new RuntimeException("Can't obtain Bundle Class Loader for bundle: " + b, e);
		}
	}
}
