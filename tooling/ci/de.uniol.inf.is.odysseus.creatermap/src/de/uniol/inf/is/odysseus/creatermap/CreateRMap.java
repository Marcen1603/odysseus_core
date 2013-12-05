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
package de.uniol.inf.is.odysseus.creatermap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateRMap {

	private static void buildHeader(StringBuilder sb) {
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
		sb.append("<rm:rmap xmlns:bc=\"http://www.eclipse.org/buckminster/Common-1.0\" xmlns:rm=\"http://www.eclipse.org/buckminster/RMap-1.0\">").append("\n");
	}

	private static void buildFooter(StringBuilder sb) {

		sb.append("</rm:rmap>");
	}

	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				String rootPath = args[0];
				// String rootPath = "E:\\Odysseus\\trunk";
				String updatesiteFeature = args[1]; // normally this should be de.uniol.inf.is.odysseus.updatesite

				System.out.println("Creating feature.xml for update site on root path: " + rootPath);
				List<String> names = new ArrayList<>();
				searchFeatures(rootPath, names);
				System.out.println("Found following features: ");
				String featureList = new String();
				for (String id : names) {
					String feature = "<includes id=\"" + id + "\" version=\"0.0.0\"/>";
					featureList = featureList + System.lineSeparator()+feature;					
				}
				StringBuilder outputfile = new StringBuilder();
				File template = new File(updatesiteFeature + File.separatorChar + "feature.xml.template");
				BufferedReader br = new BufferedReader(new FileReader(template));
				String line = br.readLine();
				while (line != null) {
					if (line.trim().equalsIgnoreCase("===FEATURE-LIST-HERE===")) {
						outputfile.append(featureList);
						
					} else {
						outputfile.append(line);						
					}
					outputfile.append(System.lineSeparator());
					line = br.readLine();
				}
				br.close();
				saveFile(outputfile, updatesiteFeature+File.separatorChar+"feature.xml");
				System.out.println("------------------------------------------------");

				System.out.println("Creating RMAP for update site on root path: " + rootPath);
				StringBuilder sbUS = new StringBuilder();
				buildHeader(sbUS);
				searchRecursive(rootPath, sbUS);
				buildFooter(sbUS);
				saveFile(sbUS, updatesiteFeature + File.separatorChar + "site.rmap");

				System.out.println("Creating RMAP for product building on root path: " + rootPath);
				StringBuilder sbProd = new StringBuilder();
				buildHeader(sbProd);
				searchRecursiveProd(rootPath, sbProd);
				buildFooter(sbProd);

				saveFile(sbProd, updatesiteFeature + File.separatorChar + "product.rmap");
				// System.out.println("----------------- RMAP FILE --------------------");
				// System.out.println(sb.toString());
				// System.out.println("------------------------------------------------");

			} else {
				System.out.println("Error: no root path given!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void searchFeatures(String rootPath, List<String> names) {
		File rootDir = new File(rootPath);
		searchRecursiveFeature(rootDir, rootDir, names);

	}

	private static void searchRecursiveFeature(File rootDir, File mainRoot, List<String> names) {
		if (rootDir == null) {
			System.out.println("Error: " + rootDir + " not found (null)");
			return;
		}
		for (File f : rootDir.listFiles()) {
			if (f.isDirectory()) {
				if (!isOneOfIgnoreCase(f.getName(), ".metadata", "restricted")) {
					if(f.getName().endsWith("de.uniol.inf.is.odysseus.updatesite")){
						continue;
					}
					searchRecursiveFeature(f, mainRoot, names);
				}
			} else {
				if (f.isFile() && f.getName().equals("feature.xml")) {
					// feature.xml found:
					String name = parseFeatureDefinition(f);
					names.add(name);
				}
			}
		}

	}

	private static boolean isOneOfIgnoreCase(String needle, String... haystack) {
		for (String h : haystack) {
			if (h.equalsIgnoreCase(needle)) {
				return true;
			}
		}
		return false;
	}

	private static String parseFeatureDefinition(File f) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;

			doc = dBuilder.parse(f);
			doc.getDocumentElement().normalize();
			Element nameNode = (Element) doc.getElementsByTagName("feature").item(0);
			String id = nameNode.getAttribute("id");
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static void searchRecursiveProd(String rootPath, StringBuilder sbProd) {
		File rootDir = new File(rootPath);
		searchRecursiveProd(rootDir, rootDir, sbProd);

	}

	private static void searchRecursiveProd(File rootDir, File mainRoot, StringBuilder sb) {
		if (rootDir == null) {
			System.out.println("Error: " + rootDir + " not found (null)");
			return;
		}
		for (File f : rootDir.listFiles()) {
			if (f.isDirectory()) {
				if (!f.getName().equalsIgnoreCase(".metadata")) {
					searchRecursiveProd(f, mainRoot, sb);
				}
			} else {
				if (f.isFile() && f.getName().equals(".project")) {
					// .project found:
					parseProjectForProduct(f, mainRoot, sb);
				}
			}
		}

	}

	private static void saveFile(StringBuilder sb, String destination) {
		System.out.println("Writing a file to: " + destination);
		try {
			File file = new File(destination);
			FileWriter writer = new FileWriter(file);
			writer.write(sb.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void searchRecursive(String mainRoot, StringBuilder sb) {
		File rootDir = new File(mainRoot);
		searchRecursive(rootDir, rootDir, sb);
	}

	public static void searchRecursive(File rootDir, File mainRoot, StringBuilder sb) {
		// System.out.println("Searching in: "+rootDir.getAbsolutePath());
		if (rootDir == null) {
			System.out.println("Error: " + rootDir + " not found (null)");
			return;
		}
		for (File f : rootDir.listFiles()) {
			if (f.isDirectory()) {
				if (!f.getName().equalsIgnoreCase(".metadata")) {
					searchRecursive(f, mainRoot, sb);
				}
			} else {
				if (f.isFile() && f.getName().equals(".project")) {
					// .project found:
					parseProject(f, mainRoot, sb);
				}
			}
		}
	}

	private static void parseProject(File projectFile, File mainRoot, StringBuilder sb) {
		// System.out.println("   Found: " +
		// projectFile.getParentFile().getAbsolutePath());
		String relativPath = projectFile.getParentFile().getAbsolutePath().substring(mainRoot.getAbsolutePath().length() + 1);
		String componentName = getComponentName(projectFile);
		String componentNamePattern = componentName.replace(".", "\\.");

		sb.append("<rm:locator failOnError=\"false\" pattern=\"^" + componentNamePattern + "$\" searchPathRef=\"" + componentName + "\"/>").append("\n");
		sb.append("<rm:searchPath name=\"" + componentName + "\">").append("\n");
		sb.append("<rm:provider componentTypes=\"osgi.bundle,eclipse.feature\" readerType=\"local\">").append("\n");
		sb.append("    <rm:uri format=\"file:///{0}/" + relativPath + "/\">").append("\n");
		sb.append("        <bc:propertyRef key=\"workspace.root\"/>").append("\n");
		sb.append("    </rm:uri>").append("\n");
		sb.append("</rm:provider>").append("\n");
		sb.append("</rm:searchPath>").append("\n");
	}

	private static void parseProjectForProduct(File projectFile, File mainRoot, StringBuilder sb) {
		// System.out.println("   Found: " +
		// projectFile.getParentFile().getAbsolutePath());
		String relativPath = projectFile.getParentFile().getAbsolutePath().substring(mainRoot.getAbsolutePath().length() + 1);
		String componentName = getComponentName(projectFile);
		String componentNamePattern = componentName.replace(".", "\\.");
		if (componentName.contains("odysseus.updatesite")) {
			sb.append("<rm:locator failOnError=\"false\" pattern=\"^" + componentNamePattern + "$\" searchPathRef=\"" + componentName + "\"/>").append("\n");
			sb.append("<rm:searchPath name=\"" + componentName + "\">").append("\n");
			sb.append("<rm:provider componentTypes=\"osgi.bundle,eclipse.feature\" readerType=\"local\">").append("\n");
			sb.append("    <rm:uri format=\"file:///{0}/" + relativPath + "/\">").append("\n");
			sb.append("        <bc:propertyRef key=\"workspace.root\"/>").append("\n");
			sb.append("    </rm:uri>").append("\n");
			sb.append("</rm:provider>").append("\n");
			sb.append("</rm:searchPath>").append("\n");
		} else {
			// sb.append("<rm:locator failOnError=\"false\" pattern=\"^" +
			// componentNamePattern + "(?!\\.)\" searchPathRef=\"" +
			// componentName + "\"/>").append("\n");
			sb.append("<rm:locator failOnError=\"false\" searchPathRef=\"" + componentName + "\"/>").append("\n");
			sb.append("<rm:searchPath name=\"" + componentName + "\">").append("\n");
			sb.append("<rm:provider componentTypes=\"osgi.bundle,eclipse.feature\" readerType=\"p2\" mutable=\"false\" source=\"false\">").append("\n");
			sb.append("    <rm:uri format=\"http://odysseus.informatik.uni-oldenburg.de/update/\" />").append("\n");
			sb.append("</rm:provider>").append("\n");
			sb.append("</rm:searchPath>").append("\n");
		}
	}

	private static String getComponentName(File projectFile) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;

			doc = dBuilder.parse(projectFile);
			doc.getDocumentElement().normalize();
			Element nameNode = (Element) doc.getElementsByTagName("name").item(0);
			String projectName = nameNode.getChildNodes().item(0).getNodeValue();
			return projectName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
