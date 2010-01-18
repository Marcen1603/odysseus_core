package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser;

import java.io.StringReader;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTMaplePiecewise;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.MapleResultParser;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ParseException;

public class MapleResultParserFacade {

	public MapleResultParser parser;
	
	public MapleResultParserFacade(){
	}
	
	public void parse(String mapleResult){
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
	}
	
	public static void main(String[] args){
		MapleResultParserFacade parser = new MapleResultParserFacade();
		
		parser.parse("piecewise(0 < -b+a,{[{x < c/(-b+a)}]},-b+a < 0,{[{c/(-b+a) < x}]},And(0 < c,a = b),{[{x}]},And(c <= 0,a = b),{})");
	}
}
