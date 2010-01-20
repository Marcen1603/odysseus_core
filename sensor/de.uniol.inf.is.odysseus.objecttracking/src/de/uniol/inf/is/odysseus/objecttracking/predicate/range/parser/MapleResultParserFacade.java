package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser;

import java.io.StringReader;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.ISolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTMaplePiecewise;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.MapleResultParser;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ParseException;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.visitor.CreateExpressionMapVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

public class MapleResultParserFacade {

	public MapleResultParser parser;
	
	public MapleResultParserFacade(){
	}
	
	public Map<IPredicate, ISolution> parse(String mapleResult, IAttributeResolver attributeResolver){
		// TODO Auto-generated method stub
		
		if(this.parser == null){
			this.parser = new MapleResultParser(new StringReader(mapleResult));
		}
		else{
			MapleResultParser.ReInit(new StringReader(mapleResult));
		}
	
		ASTMaplePiecewise tree = null;
		try {
			tree = MapleResultParser.MaplePiecewise();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (Map<IPredicate, ISolution>)new CreateExpressionMapVisitor().visit(tree, attributeResolver);
	}
	
//	public static void main(String[] args){
//		MapleResultParserFacade parser = new MapleResultParserFacade();
//		
//		parser.parse("piecewise(0 < -b+a,{[{x < c/(-b+a)}]},-b+a < 0,{[{c/(-b+a) < x}]},And(0 < c,a = b),{[{x}]},And(c <= 0,a = b),{})");
//		parser.parse("piecewise(0 < a,{[{x < 30/a}]},a < 0,{[{30/a < x}]},a = 0,{[{x}]})");
//
//	}
}
