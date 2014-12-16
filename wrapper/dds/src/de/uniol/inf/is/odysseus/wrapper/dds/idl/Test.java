package de.uniol.inf.is.odysseus.wrapper.dds.idl;


import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


public class Test {

	public void parse() throws IOException{
		InputStream input = getClass().getResourceAsStream("/ice.idl");

		ANTLRInputStream in = new ANTLRInputStream(input);
		IDLLexer lexer = new IDLLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IDLParser parser = new IDLParser(tokens);
		
		ParseTree tree = parser.module();
		System.out.println("----------------");
		System.out.println(tree.toStringTree(parser));
		System.out.println("----------------");

		
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new IDLTranslator(), tree);
	}

		
	public static void main(String[] args) throws IOException {
		Test t = new Test();
		t.parse();
		
		
	}
	
}
