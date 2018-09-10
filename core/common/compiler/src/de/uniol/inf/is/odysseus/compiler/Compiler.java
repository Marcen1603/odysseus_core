package de.uniol.inf.is.odysseus.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.osgi.framework.Bundle;

/**
 * Compiling java class in memory.
 * @author Marco Grawunder
 * Based on work of:
 *  - Copyright 2012 Liferay Inc
 *
 *
 */

public class Compiler {

	static JavaCompiler javaCompiler = null;
	static DiagnosticCollector<JavaFileObject> diagnostics = null;
	static StandardJavaFileManager standardFileManager = null;
	static BundleJavaManager bundleFileManager = null;
	static List<String> options = new ArrayList<String>();
	
	@SuppressWarnings("rawtypes")
	public synchronized static Class compile(StringBuffer classCode, String className) throws Exception {

		SourceCode sourceCode = new SourceCode(className, classCode.toString());
		CompiledCode compiledCode = new CompiledCode(className);

		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(sourceCode);
		// Diagnostics provide details about all errors/warnings
		// observed during compilation
		diagnostics = new DiagnosticCollector<JavaFileObject>();


		Bundle bundle = Activator.getContext().getBundle();

		options.add("-proc:none");
		///options.add("-verbose");

		javaCompiler = ToolProvider.getSystemJavaCompiler();
		
		if (javaCompiler == null){
			throw new RuntimeException("No Compiler found. Must use JDK not JRE!");
		}

		StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(diagnostics, null, null);


		// the OSGi aware file manager
		bundleFileManager = new BundleJavaManager(bundle, fileManager, options, compiledCode);

		DynamicClassLoader cl = new DynamicClassLoader(bundleFileManager.getClassLoader(), bundleFileManager.getClassLoaders());
		cl.setCode(compiledCode);

		// get the compilation task
		CompilationTask compilationTask = javaCompiler.getTask(null, bundleFileManager, diagnostics, options, null,
				compilationUnits);

		if (compilationTask.call()) {
			try {
				return cl.loadClass(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		// Oh no, we got errors, list them
		for (Diagnostic<?> dm : diagnostics.getDiagnostics()) {
			System.err.println("COMPILE ERROR: " + dm);
		}

		return null;
	}

	public synchronized void shutdown() throws IOException {
		bundleFileManager.close();
	}
}
