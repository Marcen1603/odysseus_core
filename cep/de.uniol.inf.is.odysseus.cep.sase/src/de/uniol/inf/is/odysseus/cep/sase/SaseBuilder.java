package de.uniol.inf.is.odysseus.cep.sase;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.cep.CepAO;
import de.uniol.inf.is.odysseus.cep.epa.symboltable.relational.RelationalSymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

public class SaseBuilder implements IQueryParser, BundleActivator  {

	@Override
	public String getLanguage() {
		return "SASE Relational";
	}

	@Override
	public void start(BundleContext arg0) throws Exception {
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
	}
	
	public void printTree(CommonTree t, int indent) {
		if (t != null) {
			StringBuffer sb = new StringBuffer(indent);
			for (int i = 0; i < indent; i++)
				sb = sb.append("   ");
			for (int i = 0; i < t.getChildCount(); i++) {
				System.out.println(sb.toString() + t.getChild(i).toString());
				printTree((CommonTree) t.getChild(i), indent + 1);
			}
		}
	}
	
	public List<ILogicalOperator> parse(Reader reader)
			throws QueryParseException {
		SaseLexer lex = null;
		try {
			lex = new SaseLexer(new ANTLRReaderStream(reader));
		} catch (IOException e) {
			throw new QueryParseException(e);
		}
		return processParse(lex);
	}

	public List<ILogicalOperator> parse(String text) throws QueryParseException {
		SaseLexer lex = new SaseLexer(new ANTLRStringStream(text));
		return processParse(lex);
	}

	private List<ILogicalOperator> processParse(SaseLexer lexer)
			throws QueryParseException {
		List<ILogicalOperator> retList = new ArrayList<ILogicalOperator>();
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SaseParser parser = new SaseParser(tokens);
		SaseParser.start_return ret;
		try {
			ret = parser.start();
		} catch (RecognitionException e) {
			e.printStackTrace();
			throw new QueryParseException(e);
		}
		CommonTree tree = (CommonTree) ret.getTree();
		printTree(tree, 2);
		CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
		SaseAST walker = new SaseAST(nodes);

		// Relational ... ggf. auslagern ?
		walker.symTableOpFac = new RelationalSymbolTableOperationFactory();
		CepVariable.setSymbolTableOperationFactory(walker.symTableOpFac);

		try {
			retList.add(walker.start());
		} catch (RecognitionException e) {
			throw new QueryParseException(e);
		}
		return retList;
	}

	// -----------------------------------------------------------------------------------------
	// FOR TESTING ONLY
	// -----------------------------------------------------------------------------------------
	
	private static void createDummySource(String sourcename) {
		AccessAO source;
		source = new AccessAO(new SDFSource(sourcename, "RelationalStreaming"));
		source.setOutputSchema(new SDFAttributeList());
		DataDictionary.getInstance().setView(sourcename, source);
	}
	
	public static void main(String[] args) {
		SaseBuilder exec = new SaseBuilder();
		// Zum Testen
		createDummySource("Stock");
		createDummySource("Shelf");
		createDummySource("Register");
		createDummySource("Exit");
		createDummySource("Alert");
		createDummySource("Shipment");
		createDummySource("nexmark:bid2");
		createDummySource("nexmark:person2");
		createDummySource("nexmark:auction2");
		createDummySource("nexmark:category2");
		createDummySource("nexmark:bid");
		createDummySource("nexmark:person");
		createDummySource("nexmark:auction");
		createDummySource("nexmark:category");

		String[] toParse = new String[] {
				"PATTERN SEQ(nexmark:person2 person, nexmark:auction2 auction, nexmark:bid2+ bid[]) WHERE skip_till_any_match(person, auction, bid){person.id = auction.seller and auction.id = bid[1].auction and bid[i].auction = bid[i-1].auction and Count(bid[..i-1].bidder)>2} WITHIN 1 minute RETURN person.id, person.name, auction.id, Count(bid[..i-1].bidder)"
//				"CREATE STREAM Marco PATTERN SEQ(nexmark:person2 person, nexmark:auction2 auction, nexmark:bid2+ bid[])"
//				,
//				"PATTERN SEQ(~nexmark:person2 person, nexmark:auction2 auction, nexmark:bid2+ bid[])"
//				,
//				"PATTERN SEQ(nexmark:person2 person, nexmark:auction2 auction, nexmark:bid2+ bid[])"
//				,
//		,"PATTERN SEQ(nexmark:person2 person, nexmark:auction2 auction, nexmark:bid2+ bid[]) WHERE skip_till_any_match(person, auction, bid){person.id = auction.seller and auction.id = bid[1].auction and bid[i].auction = bid[i-1].auction+bid[i].auction and 5*21+(7+7)*2/COunt(bid[..i-1].bidder)>2} RETURN person.id, count(person[]), person.name, auction.id" };
//						"PATTERN SEQ(nexmark:person2 person, nexmark:auction2 auction, nexmark:bid2+ bid[]) WHERE skip_till_any_match(person, auction, bid){person.id = auction.seller and auction.id = bid[1].auction and bid[i].auction = bid[i-1].auction+bid[i].auction and 5*21+(7+7)*2/COunt(bid[..i-1].bidder)>2} WITHIN 1" };
	// "PATTERN SEQ(nexmark:person2 person, nexmark:auction2 auction, nexmark:bid2+ bid[]) WHERE skip_till_any_match(person, auction, bid){person.id = auction.seller and auction.id = bid[1].auction} RETURN person.id, count(person[]), person.name, auction.id" 
				};

		for (String q : toParse) {
			System.out.println(q);
			try {
				List<ILogicalOperator> top = exec.parse(q);
				CepAO cepAo = (CepAO) top
						.get(0);
				System.out.println("Final SM "
						+ cepAo.getStateMachine().prettyPrint());
				System.out.println(AbstractTreeWalker.prefixWalk(top.get(0),
						new AlgebraPlanToStringVisitor()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}
