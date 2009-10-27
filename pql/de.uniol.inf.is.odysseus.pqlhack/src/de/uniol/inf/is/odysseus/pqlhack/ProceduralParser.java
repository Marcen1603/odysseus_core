package de.uniol.inf.is.odysseus.pqlhack;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.pqlhack.parser.ASTLogicalPlan;
import de.uniol.inf.is.odysseus.pqlhack.parser.ParseException;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParser;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.CreateLogicalPlanVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.InitAttributesVisitor;

public class ProceduralParser implements IQueryParser{

	public static IQueryParser instance;
	
	public static synchronized IQueryParser getInstance(){
		if (instance == null){
			instance = new ProceduralParser();
		}
		return instance;
	}
	
	@Override
	public List<AbstractLogicalOperator> parse(String query)
			throws ParseException {
		// TODO Auto-generated method stub
		List<AbstractLogicalOperator> listOfPlans = new ArrayList<AbstractLogicalOperator>();
		
		InitAttributesVisitor initAttrs = new InitAttributesVisitor();
		CreateLogicalPlanVisitor createPlan = new CreateLogicalPlanVisitor();
		
		ProceduralExpressionParser exprParser = new ProceduralExpressionParser(new StringReader(query));
		ASTLogicalPlan logicalPlan = null;
		try {
			logicalPlan = exprParser.LogicalPlan();
		} catch (de.uniol.inf.is.odysseus.querytranslation.procedural.parser.ParseException e) {
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
	public List<AbstractLogicalOperator> parse(Reader reader)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
