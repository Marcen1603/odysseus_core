package de.uniol.inf.is.odysseus.wrapper.dds.idl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


public class Test {

	public void parse() throws IOException{
		BufferedReader input = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/ice.idl")));

		IDLTranslator idlTranslator = new IDLTranslator();
		// read line wise and remove #pragma
		StringBuffer idlFile = new StringBuffer();
		String line;
		while((line = input.readLine())!=null){
			line = line.trim();
			if (line.contains("#pragma")){
				String[] tokens = line.split(" ");
				if (tokens.length > 2){
					if (tokens[0].equalsIgnoreCase("#pragma") && tokens[1].equalsIgnoreCase("keylist")){
						List<String> keyAttributes = new LinkedList<>();
						for (int i=3;i<tokens.length;i++){
							keyAttributes.add(tokens[i]);
						}
						idlTranslator.putKey(tokens[2], keyAttributes);
					}
				}
				
			}else{
				idlFile.append(line).append("\n");
			}
		}
		
		ANTLRInputStream in = new ANTLRInputStream(idlFile.toString());
		IDLLexer lexer = new IDLLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IDLParser parser = new IDLParser(tokens);
		
		ParseTree tree = parser.module();
		
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(idlTranslator, tree);
	}

		
	public static void main(String[] args) throws IOException {
		Test t = new Test();
		t.parse();
		
		
	}
	
}
