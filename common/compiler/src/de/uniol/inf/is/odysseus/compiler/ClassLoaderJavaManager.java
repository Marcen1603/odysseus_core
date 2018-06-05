/**
 * Copyright 2012 Liferay Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoaderJavaManager extends ForwardingJavaFileManager<JavaFileManager> implements Constants {

	static Logger log = LoggerFactory.getLogger(ClassLoaderJavaManager.class);


	public ClassLoaderJavaManager(ClassLoader classLoader, JavaFileManager javaFileManager) throws IOException {

		this(classLoader, javaFileManager, null);
	}

	public ClassLoaderJavaManager(ClassLoader classLoader, JavaFileManager javaFileManager, List<String> options) throws IOException {

		super(javaFileManager);

		_javaFileManager = javaFileManager;

		setOptions(options);



		_classLoader = classLoader;

	}

	public ClassLoader getClassLoader() {
		return _classLoader;
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		if (location != StandardLocation.CLASS_PATH) {
			return _javaFileManager.getClassLoader(location);
		}

		return getClassLoader();
	}

	@Override
	public String inferBinaryName(Location location, JavaFileObject file) {
		if ((location == StandardLocation.CLASS_PATH) && (file instanceof BundleJavaFileObject)) {

			BundleJavaFileObject bundleJavaFileObject = (BundleJavaFileObject) file;

			log.trace("Infering binary name from " + bundleJavaFileObject);

			return bundleJavaFileObject.inferBinaryName();
		}

		return _javaFileManager.inferBinaryName(location, file);
	}

	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
			throws IOException {

		List<JavaFileObject> javaFileObjects = new ArrayList<JavaFileObject>();

		if (location == StandardLocation.CLASS_PATH) {
			log.trace("List available sources for {location=" + location + ", packageName=" + packageName + ", kinds="
					+ kinds + ", recurse=" + recurse + "}");
		}

		String packagePath = packageName.replace('.', '/');

		if (!packageName.startsWith(JAVA_PACKAGE) && (location == StandardLocation.CLASS_PATH)) {

			// When not in strict mode, the following ensures that if a standard
			// classpath location has been provided we include it. It allows the
			// framework to compile against libraries not deployed as OSGi
			// bundles.
			// This is also needed in cases where the system.bundle exports
			// extra
			// packages via the property
			// 'org.osgi.framework.system.packages.extra'
			// or via extension bundles (fragments) which only supplement its
			// 'Export-Package' directive.

			if (packageName.startsWith(JAVA_PACKAGE) || (location != StandardLocation.CLASS_PATH)
					|| (javaFileObjects.isEmpty() && hasPackageCapability(packageName))) {

				for (JavaFileObject javaFileObject : _javaFileManager.list(location, packagePath, kinds, recurse)) {

					if (location == StandardLocation.CLASS_PATH) {
						log.trace("\t" + javaFileObject);
					}

					javaFileObjects.add(javaFileObject);
				}
			}
		}
		return javaFileObjects;

	}

	private boolean hasPackageCapability(String packageName) {
		return true;
	}

	private void setOptions(List<String> options) {
		if (options == null) {
			return;
		}

		_options.addAll(options);


	}

	private ClassLoader _classLoader;
	private JavaFileManager _javaFileManager;
	private List<String> _options = new ArrayList<String>();

}