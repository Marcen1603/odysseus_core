package de.uniol.inf.is.odysseus.parser.novel.cql.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestCaseExtractor 
{
	
	public static void main(String[] args)
	{
		
		Class<CQLGeneratorQueryTest> clz = CQLGeneratorQueryTest.class;
		for (Method m : clz.getDeclaredMethods()) {
		  System.err.println(m.getName());
		  int paramCount = m.getParameterTypes().length;
		  for (int i = 0;  i < paramCount;  i++) {
		    System.err.println("  arg" + i + " -> " + m.getParameters()[i]);
		  }
		}
		
//		String path = "/Users/jp/git/odysseus/server/cql/parser.novel.cql/de.uniol.inf.is.odysseus.parser.novel.cql.tests/src/de/uniol/inf/is/odysseus/parser/novel/cql/tests/CQLGeneratorQueryTest.xtend";
//		try (BufferedReader buffer = new BufferedReader(new FileReader(new File(path))))
//		{
//			String line;
//			StringBuilder builder =  new StringBuilder();
//			while((line = buffer.readLine()) != null) 
//			{
//				if(line.contains("import")
//						|| line.contains("package")
//						|| line.contains("class")
//						|| line.contains("@")
//						|| line.contains("assertCorrectGenerated")
//						|| line.contains("//TODO")
//						|| line.contains("format")
//						|| line.contains("s.replaceAll")
//						|| line.contains("println")
////						|| line.contains("new CQLDictionaryHelper()")
////						|| line.contains("null") 
//						)
//					continue;
//				else if(line.contains("\""))
//					builder.append(line + "\n");
//				else if(line.trim().equals(","))
//					builder.append("§");
//				else if(line.trim().contains("new CQLDictionaryHelper()") || line.trim().contains("null"))
//					builder.append("^");
//			}
//			
//			String[] array = builder.toString().split("\n");
//			System.out.println(array.length);
//			for(String str : array)
//				System.out.println(str.substring(1, str.length() - 2));
//		} 
//		catch (FileNotFoundException e) 
//		{
//			e.printStackTrace();
//		} 
//		catch (IOException e) 
//		{
//			e.printStackTrace();
//		}
	}

}
