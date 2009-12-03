package de.uniol.inf.is.odysseus.cep.sase;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;



public class SaseBuilder {

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
		SaseBuilder exec = new SaseBuilder();
		String[] toParse = new String[]{
				"PATTERN SEQ(Shelf a, ~(Register+ b[]), Exit c) "+
				"WHERE skip_till_next_match(a,b,c){ " +
				"[tag_id]" +
				"}" +
				"WITHIN 12 hours",
		"PATTERN SEQ(Shelf a, ~(Register+ b[]), Exit c) "+
		"WHERE skip_till_next_match(a,b,c){ " +
		"a.tag_id = b.tag_id AND " +
		"a.tag_id = c.tag_id }" +
		"WITHIN 12 hours",
		"PATTERN SEQ(Alert a, Shipment+ b[]) " +
		"WHERE skip_till_any_match(a,b[]){" +
		"a.type = \"contaminated\" AND " +
		"b[1].from = a.site AND " +
		"b[i].from = b[i-1].to} " +
		"WITHIN 3 hours",
		"PATTERN SEQ(Stock+ a[], Stock b) " +
		"WHERE skip_till_next_match(a[],b){ " +
		"[symbol] " +
		"and a[1].volume > 1000 " +
		"and a[i].price > avg(a[..i-1].price) " +
		"and b.volume < 80% * a[a.LEN].volume} " +
		"WITHN 1 hour"};

		try {
			for(String q: toParse){
				System.out.println(q);
				exec.parse(q);
			}
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
