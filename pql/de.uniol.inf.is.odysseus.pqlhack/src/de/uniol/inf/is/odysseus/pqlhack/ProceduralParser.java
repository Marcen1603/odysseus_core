package de.uniol.inf.is.odysseus.pqlhack;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTLogicalPlan;
import de.uniol.inf.is.odysseus.pqlhack.parser.ParseException;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParser;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.CreateLogicalPlanVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.InitAttributesVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.InitBrokerVisitor;

public class ProceduralParser implements IQueryParser {

	public static IQueryParser instance;

	public static final String language = "";

	private ProceduralExpressionParser parser;

	public static synchronized IQueryParser getInstance() {
		if (instance == null) {
			instance = new ProceduralParser();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public List<IQuery> parse(String query) throws QueryParseException {
		// TODO Auto-generated method stub
		List<IQuery> listOfPlans = new ArrayList<IQuery>();

		InitAttributesVisitor initAttrs = new InitAttributesVisitor();
		InitBrokerVisitor initBroker = new InitBrokerVisitor();
		CreateLogicalPlanVisitor createPlan = new CreateLogicalPlanVisitor();

		if (this.parser == null) {
			this.parser = new ProceduralExpressionParser(
					new StringReader(query));
		} else {
			ProceduralExpressionParser.ReInit(new StringReader(query));
		}

		ASTLogicalPlan logicalPlan = null;
		try {
			logicalPlan = ProceduralExpressionParser.LogicalPlan();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// init the attribute resolver
		initAttrs.visit(logicalPlan, null);
		ArrayList data = new ArrayList();
		data.add(initAttrs.getAttributeResolver());

		// init the broker dictionary
		initBroker.visit(logicalPlan, null);

		// create the logical plan
		AbstractLogicalOperator topAO = (AbstractLogicalOperator) ((ArrayList) createPlan
				.visit(logicalPlan, data)).get(1);

		Query queryObj = new Query();
		queryObj.setParserId(getLanguage());
		queryObj.setLogicalPlan(topAO);

		listOfPlans.add(queryObj);
		return listOfPlans;
	}

	public List<IQuery> parse(Reader reader) throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLanguage() {
		// TODO Auto-generated method stub
		return "PQLHack";
	}

}
