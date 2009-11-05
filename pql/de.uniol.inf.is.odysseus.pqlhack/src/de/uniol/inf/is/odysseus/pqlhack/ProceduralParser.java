package de.uniol.inf.is.odysseus.pqlhack;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTLogicalPlan;
import de.uniol.inf.is.odysseus.pqlhack.parser.ParseException;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParser;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.CreateLogicalPlanVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.InitAttributesVisitor;

public class ProceduralParser implements IQueryParser{

	public static IQueryParser instance;
	
	public static final String language = "";
	
	public static synchronized IQueryParser getInstance(){
		if (instance == null){
			instance = new ProceduralParser();
		}
		return instance;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ILogicalOperator> parse(String query)
			throws QueryParseException {
		// TODO Auto-generated method stub
		List<ILogicalOperator> listOfPlans = new ArrayList<ILogicalOperator>();
		
		InitAttributesVisitor initAttrs = new InitAttributesVisitor();
		CreateLogicalPlanVisitor createPlan = new CreateLogicalPlanVisitor();
		
		new ProceduralExpressionParser(new StringReader(query));
		ASTLogicalPlan logicalPlan = null;
		try {
			logicalPlan = ProceduralExpressionParser.LogicalPlan();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initAttrs.visit(logicalPlan, null);
		
		ArrayList data = new ArrayList();
		data.add(initAttrs.getAttributeResolver());
		
		AbstractLogicalOperator topAO = (AbstractLogicalOperator)((ArrayList)createPlan.visit(logicalPlan, data)).get(1);
		
		listOfPlans.add(topAO);
		return listOfPlans;
	}

	@Override
	public List<ILogicalOperator> parse(Reader reader)
			throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLanguage() {
		// TODO Auto-generated method stub
		return "PQLHack";
	}

}
