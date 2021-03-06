package de.uniol.inf.is.odysseus.core.metadata;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class GenerateMetadataClassCode {

	public static String generateClassCode(List<Class<? extends IMetaAttribute>> classList,
			List<IMetaAttribute> metaDataTypes, StringBuffer generatedClass) throws ClassNotFoundException {
		if (classList.size() != metaDataTypes.size()) {
			throw new IllegalArgumentException("Lists must have same size!");
		}
		List<Pair<String, String>> interfacesAndClasses = new ArrayList<>();
		for (int i = 0; i < classList.size(); i++) {
			Pair<String, String> p = new Pair<>(classList.get(i).getName(), metaDataTypes.get(i).getClass().getName());
			interfacesAndClasses.add(p);
		}

		GenericClassLoader classLoader = new GenericClassLoader(metaDataTypes);

		return generateClassCode(interfacesAndClasses, classLoader, generatedClass);
	}

	static private String generateClassCode(List<Pair<String, String>> interfacesAndClasses,
			GenericClassLoader classLoader, StringBuffer generatedClassCode) throws ClassNotFoundException {

		createHeader(interfacesAndClasses, generatedClassCode);

		createClassStart(interfacesAndClasses, generatedClassCode);

		createMemberVars(interfacesAndClasses, generatedClassCode);

		createConstructorAndClone(interfacesAndClasses, generatedClassCode);

		createMergeMethods(interfacesAndClasses, generatedClassCode);

		for (Pair<String, String> pair : interfacesAndClasses) {
			createDelegateMethods(pair.getE1(), pair.getE2(), generatedClassCode, classLoader);
		}

		// HACK: Generate compareTo if ITimeInterval is contained, I do not now any other solution
		for (Pair<String, String> pair : interfacesAndClasses) {
			if (pair.getE1().endsWith("ITimeInterval")){
				createCompareMethod(pair, generatedClassCode);
			}
		}

		createClassEnd(generatedClassCode);
		return generateClassName(interfacesAndClasses);
	}


	private static void createHeader(List<Pair<String, String>> interfacesAndClasses, StringBuffer generatedClassCode) {
		generatedClassCode.append("/* This file is generated by Odysseus on " + new Date() + "*/\n");
		generatedClassCode.append("import java.util.ArrayList;\n");
		generatedClassCode.append("import java.util.List;\n");
		generatedClassCode.append("\n");
		generatedClassCode.append("import de.uniol.inf.is.odysseus.core.collection.Tuple;\n");
		generatedClassCode.append("import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;\n");
		generatedClassCode.append("import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;\n");
		generatedClassCode.append("import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;\n");
		generatedClassCode.append("import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;\n");
		generatedClassCode.append("\n");
	}

	private static void createClassStart(List<Pair<String, String>> interfacesAndClasses,
			StringBuffer generatedClassCode) {

		generatedClassCode.append("final public class ");
		generatedClassCode.append(generateClassName(interfacesAndClasses));
		generatedClassCode.append(" extends AbstractCombinedMetaAttribute implements ");
		Iterator<Pair<String, String>> iter = interfacesAndClasses.iterator();
		generatedClassCode.append(iter.next().getE1());
		while (iter.hasNext()) {
			generatedClassCode.append(", ").append(iter.next().getE1());
		}
		generatedClassCode.append(" {\n\n");
	}

	private static void createMemberVars(List<Pair<String, String>> interfacesAndClasses,
			StringBuffer generatedClassCode) {

		// TODO: generate
		generatedClassCode.append("private static final long serialVersionUID = 1l;\n");
		generatedClassCode.append("@SuppressWarnings(\"unchecked\")\n");
		generatedClassCode.append("public final static Class<? extends IMetaAttribute>[] classes = new Class[]{ \n");
		generatedClassCode.append(" ");
		for (int i = 0; i < interfacesAndClasses.size(); i++) {
			generatedClassCode.append(interfacesAndClasses.get(i).getE1() + ".class");
			if (i < interfacesAndClasses.size() - 1) {
				generatedClassCode.append(", ");
			}
		}
		generatedClassCode.append("\n};\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public Class<? extends IMetaAttribute>[] getClasses() {\n");
		generatedClassCode.append("	return classes;\n");
		generatedClassCode.append("}\n");

		generatedClassCode.append(
				"public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);\n");
		generatedClassCode.append("static{\n");
		for (Pair<String, String> pair : interfacesAndClasses) {
			generatedClassCode.append("	schema.addAll(" + pair.getE2() + ".schema);\n");
		}
		generatedClassCode.append("}\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public List<SDFMetaSchema> getSchema() {\n");
		generatedClassCode.append("	return schema;\n");
		generatedClassCode.append("}\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public String getName() {\n");
		generatedClassCode.append("	return \"" + generateMetadataName(interfacesAndClasses) + "\";\n");
		generatedClassCode.append("}\n");

		for (Pair<String, String> pair : interfacesAndClasses) {
			generatedClassCode.append("private final " + pair.getE1() + " " + generateMemberVarFrom(pair.getE2()) + ";\n");
		}
		generatedClassCode.append("\n");

	}

	private static void createConstructorAndClone(List<Pair<String, String>> interfacesAndClasses,
			StringBuffer generatedClassCode) {
		String className = generateClassName(interfacesAndClasses);
		generatedClassCode.append("public " + className + "() {\n");
		for (Pair<String, String> pair : interfacesAndClasses) {
			generatedClassCode.append("	" + generateMemberVarFrom(pair.getE2()) + " = new " + pair.getE2() + "();\n");
		}
		generatedClassCode.append("}\n");

		generatedClassCode.append("public " + className + "(" + className + " clone) {\n");
		for (Pair<String, String> pair : interfacesAndClasses) {
			generatedClassCode.append("	this." + generateMemberVarFrom(pair.getE2()) + " = (" + pair.getE2() + ") clone."
					+ generateMemberVarFrom(pair.getE2()) + ".clone();\n");
		}
		generatedClassCode.append("}\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public " + className + " clone() {\n");
		generatedClassCode.append("	return new " + className + "(this);\n");
		generatedClassCode.append("}\n");
	}

	private static void createMergeMethods(List<Pair<String, String>> interfacesAndClasses,
			StringBuffer generatedClassCode) {
		// ------------------------------------------------------------------------------\n");
		// Methods that need to merge different types\n");
		// ------------------------------------------------------------------------------\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public void retrieveValues(List<Tuple<?>> values) {\n");
		for (Pair<String, String> pair : interfacesAndClasses) {
			generatedClassCode.append("	" + generateMemberVarFrom(pair.getE2()) + ".retrieveValues(values);\n");
		}
		generatedClassCode.append("}\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public void writeValues(List<Tuple<?>> values) {\n");
		for (int i = 0; i < interfacesAndClasses.size(); i++) {
			generatedClassCode.append("	" + generateMemberVarFrom(interfacesAndClasses.get(i).getE2())
					+ ".writeValue(values.get(" + i + "));\n");
		}
		generatedClassCode.append("}\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append(
				"public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {\n");
		generatedClassCode
				.append("	List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();\n");
		for (Pair<String, String> pair : interfacesAndClasses) {
			generatedClassCode
					.append("	list.addAll(" + generateMemberVarFrom(pair.getE2()) + ".getInlineMergeFunctions());\n");
		}
		generatedClassCode.append("	return list;\n");
		generatedClassCode.append("}\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public <K> K getValue(int subtype, int index) {\n");
		generatedClassCode.append("	switch(subtype){\n");
		for (int i = 0; i < interfacesAndClasses.size(); i++) {
			generatedClassCode.append("		case " + i + ":\n");
			generatedClassCode.append(
					"			return " + generateMemberVarFrom(interfacesAndClasses.get(i).getE2()) + ".getValue(" + i + ", index);\n");
		}
		generatedClassCode.append("	}\n");
		generatedClassCode.append("	return null;\n");
		generatedClassCode.append("}\n");

		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public String toString() {\n");
		generatedClassCode.append("  StringBuffer ret = new StringBuffer();\n");

		for (int i = 0; i < interfacesAndClasses.size(); i++) {
			generatedClassCode.append(
					"  ret.append(" + generateMemberVarFrom(interfacesAndClasses.get(i).getE2()) + ".toString());\n");
			if (i < interfacesAndClasses.size() - 1) {
				generatedClassCode.append("  ret.append(\"|\");\n");
			}
		}
		generatedClassCode.append("  return ret.toString();\n");
		generatedClassCode.append("}\n");

	}

	private static void createDelegateMethods(String interfaceName, String implClassName,
			StringBuffer generatedClassCode, GenericClassLoader classLoader) throws ClassNotFoundException {
		Class<?> iclazz = classLoader.loadClass(interfaceName);
		Method[] methods = iclazz.getDeclaredMethods();
		for (Method m : methods) {
			generatedClassCode.append("  @Override\n");
			generatedClassCode.append("  public " + m.getReturnType().getName() + " " + m.getName() + "(");
			Class<?>[] paramTypes = m.getParameterTypes();
			for (int p = 0; p < paramTypes.length; p++) {
				generatedClassCode.append(paramTypes[p].getName() + " p" + p);
				if (p < paramTypes.length - 1) {
					generatedClassCode.append(", ");
				}
			}
			generatedClassCode.append("){\n");
			generatedClassCode.append("     ");
			if (!m.getReturnType().getName().equals("void")) {
				generatedClassCode.append("return ");
			}
			generatedClassCode.append(generateMemberVarFrom(implClassName) + "." + m.getName() + "(");
			for (int p = 0; p < paramTypes.length; p++) {
				generatedClassCode.append("p" + p);
				if (p < paramTypes.length - 1) {
					generatedClassCode.append(", ");
				}
			}

			generatedClassCode.append(");\n");
			generatedClassCode.append("}\n\n");

		}

	}

	private static void createCompareMethod(Pair<String, String> pair, StringBuffer generatedClassCode) {
		generatedClassCode.append("@Override\n");
		generatedClassCode.append("public int compareTo("+pair.getE1()+" o) {\n");
		generatedClassCode.append("	return "+generateMemberVarFrom(pair.getE2())+".compareTo(o);\n");
		generatedClassCode.append("}\n");
	}


	private static void createClassEnd(StringBuffer generatedClassCode) {
		generatedClassCode.append("}\n");
	}

	private static String generateClassName(List<Pair<String, String>> interfacesAndClasses) {
		StringBuffer ret = new StringBuffer();
		for (Pair<String, String> pair : interfacesAndClasses) {
			//ret.append(pair.getE2().replaceAll("\\.", ""));
			ret.append(getWithoutPath(pair.getE2()));
		}

		return ret.toString();
	}

	private static String generateMetadataName(List<Pair<String, String>> interfacesAndClasses) {
		StringBuffer ret = new StringBuffer();
		for (Pair<String, String> pair : interfacesAndClasses) {
			ret.append(getWithoutPath(pair.getE2()));
		}
		return ret.toString();
	}

	private static String generateMemberVarFrom(String e2) {
		return e2.substring(e2.lastIndexOf(".") + 1).toLowerCase();
	}

	private static String getWithoutPath(String e2) {
		return e2.substring(e2.lastIndexOf(".") + 1);
	}

//	public static void main(String[] args) {
//		List<Pair<String, String>> interfacesAndClasses = new ArrayList<>();
//		interfacesAndClasses.add(new Pair<>("de.uniol.inf.is.odysseus.core.metadata.ITimeInterval",
//				"de.uniol.inf.is.odysseus.core.metadata.TimeInterval"));
//		interfacesAndClasses.add(new Pair<>("de.uniol.inf.is.odysseus.core.server.metadata.ILatency",
//				"de.uniol.inf.is.odysseus.latency.Latency"));
//
//		try {
//			System.out.println(generateClassCode(interfacesAndClasses, null));
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		StringBuffer buffer = new StringBuffer();
//		try {
//			List l = new ArrayList<>();
//			l.add(Object.class);
//			createDelegateMethods(Object.class.getName(), Object.class.getName(), buffer, new GenericClassLoader(l));
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println(buffer);
//
//	}

}
