/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser;

import java.io.StringReader;
import java.util.Map;

import de.uniol.inf.is.odysseus.objecttracking.predicate.range.ISolution;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ASTMaple;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.MapleResultParser;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.ParseException;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes.visitor.CreateExpressionMapVisitor;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

@SuppressWarnings({"unchecked","rawtypes"})
public class MapleResultParserFacade {

	public MapleResultParser parser;
	private static MapleResultParserFacade instance = new MapleResultParserFacade();
	
	private MapleResultParserFacade(){
	}
	
	public static MapleResultParserFacade getInstance(){
		if(instance == null){
			instance = new MapleResultParserFacade();
		}
		return instance;
	}
	
	public Map<IPredicate, ISolution> parse(String mapleResult, IAttributeResolver attributeResolver){
		// TODO Auto-generated method stub
		
		if(this.parser == null){
			this.parser = new MapleResultParser(new StringReader(mapleResult));
		}
		else{
			MapleResultParser.ReInit(new StringReader(mapleResult));
		}
	
		ASTMaple tree = null;
		try {
			tree = MapleResultParser.Maple();
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
