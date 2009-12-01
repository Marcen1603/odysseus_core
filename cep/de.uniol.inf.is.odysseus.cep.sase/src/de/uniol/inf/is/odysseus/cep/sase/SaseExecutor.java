package de.uniol.inf.is.odysseus.cep.sase;

import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;



public class SaseExecutor {

	static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
		public Object create(Token payload) {
			return new CommonTree(payload);
		}
	};
	
	public void parse(String text) throws RecognitionException{
		SaseLexer lex = new SaseLexer(new ANTLRStringStream(text));
		TokenRewriteStream tokens = new TokenRewriteStream(lex);
		SaseParser grammar = new SaseParser(tokens);
		grammar.setTreeAdaptor(adaptor);
		SaseParser.query_return ret = grammar.query();
		CommonTree tree = (CommonTree) ret.getTree();
		printTree(tree, 2);
		Map<String, String> v = grammar.getVariables();
		System.out.println(v);
		List<String> k = grammar.getKleeneAttributes();
		System.out.println(k);
		List<String> s = grammar.getStates();
		System.out.println(s+" F ");
		Map<String, String> m = grammar.getStateVariables();
		System.out.println(m);
	}
	
	public void printTree(CommonTree t, int indent) {
		if ( t != null ) {
			StringBuffer sb = new StringBuffer(indent);
			for ( int i = 0; i < indent; i++ )
				sb = sb.append("   ");
			for ( int i = 0; i < t.getChildCount(); i++ ) {
				System.out.println(sb.toString() + t.getChild(i).toString());
				printTree((CommonTree)t.getChild(i), indent+1);
			}
		}
	}

	
	public static void main(String[] args) {
		SaseExecutor exec = new SaseExecutor();
		try {
			exec.parse("PATTERN SEQ(Stock+ a[], Stock b) WHERE EGAL");
			exec.parse("PATTERN SEQ(Shelf a, ~(Register b), Exit c");
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
