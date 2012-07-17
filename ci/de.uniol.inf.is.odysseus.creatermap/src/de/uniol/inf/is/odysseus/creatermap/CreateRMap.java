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

import java.io.File;
import java.io.FileWriter;

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
		if (args.length > 0) {
			String rootPath = args[0];
			//String rootPath = "E:\\Odysseus\\trunk";
			String destinationA = args[1];
			String destinationB = args[2];
			
			System.out.println("Creating RMAP for update site on root path: " + rootPath);
			StringBuilder sbUS = new StringBuilder();
			buildHeader(sbUS);
			searchRecursive(rootPath, sbUS);
			buildFooter(sbUS);
			saveFile(sbUS, destinationA);

			System.out.println("Creating RMAP for product building on root path: " + rootPath);
			StringBuilder sbProd = new StringBuilder();
			buildHeader(sbProd);
			searchRecursiveProd(rootPath, sbProd);
			buildFooter(sbProd);

			saveFile(sbProd, destinationB);
//			 System.out.println("----------------- RMAP FILE --------------------");
//			 System.out.println(sb.toString());
//			 System.out.println("------------------------------------------------");
			 

		} else {
			System.out.println("Error: no root path given!");
		}

	}

	private static void searchRecursiveProd(String rootPath, StringBuilder sbProd) {
		File rootDir = new File(rootPath);
		searchRecursiveProd(rootDir, rootDir, sbProd);
		
	}

	private static void searchRecursiveProd(File rootDir, File mainRoot, StringBuilder sb) {
		if(rootDir==null){
			System.out.println("Error: "+rootDir+" not found (null)");
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
		System.out.println("Writing RMAP to: "+destination);
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
		if(rootDir==null){
			System.out.println("Error: "+rootDir+" not found (null)");
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

		sb.append("<rm:locator failOnError=\"false\" pattern=\"^" + componentNamePattern + "(?!\\.)\" searchPathRef=\"" + componentName + "\"/>").append("\n");
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
	//	String relativPath = projectFile.getParentFile().getAbsolutePath().substring(mainRoot.getAbsolutePath().length() + 1);
		String componentName = getComponentName(projectFile);
		String componentNamePattern = componentName.replace(".", "\\.");

		sb.append("<rm:locator failOnError=\"false\" pattern=\"^" + componentNamePattern + "(?!\\.)\" searchPathRef=\"" + componentName + "\"/>").append("\n");
		sb.append("<rm:searchPath name=\"" + componentName + "\">").append("\n");
		sb.append("<rm:provider componentTypes=\"osgi.bundle,eclipse.feature\" readerType=\"p2\">").append("\n");
		sb.append("    <rm:uri format=\"http://odysseus.informatik.uni-oldenburg.de/update\" />").append("\n");
		sb.append("</rm:provider>").append("\n");
		sb.append("</rm:searchPath>").append("\n");
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
