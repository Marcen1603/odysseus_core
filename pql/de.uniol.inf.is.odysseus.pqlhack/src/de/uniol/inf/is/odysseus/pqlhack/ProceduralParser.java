package de.uniol.inf.is.odysseus.pqlhack;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.transaction.TransactionDetector;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAlgebraOp;
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
	
	/**
	 * This list contains all broker names that
	 * have been detected during the last call
	 * of parse(String).
	 */
	private ArrayList<String> brokerNames;

	public static synchronized IQueryParser getInstance() {
		if (instance == null) {
			instance = new ProceduralParser();
		}
		return instance;
	}

	/**
	 * Returns a list of logical plans. One plan for each
	 * LOGICAL PLAN: Statement in the <query>-String.
	 * If there is the same broker used in each of the statements,
	 * the last plan in returned list contains the whole plan.
	 */
	@SuppressWarnings("unchecked")
	public List<IQuery> parse(String query) throws QueryParseException {
		List<IQuery> listOfPlans = new ArrayList<IQuery>();
		this.brokerNames = new ArrayList<String>();

		InitAttributesVisitor initAttrs = new InitAttributesVisitor();
		InitBrokerVisitor initBroker = new InitBrokerVisitor();
		CreateLogicalPlanVisitor createPlan = new CreateLogicalPlanVisitor();
		
		if(this.parser == null){
			this.parser = new ProceduralExpressionParser(new StringReader(query));
		}
		else{
			ProceduralExpressionParser.ReInit(new StringReader(query));
		}

		ASTLogicalPlan logicalPlan = null;
		try {
			logicalPlan = ProceduralExpressionParser.LogicalPlan();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// for each logical plan do the following
		

		for(int i = 0; i<logicalPlan.jjtGetNumChildren(); i++){
			ASTAlgebraOp root = (ASTAlgebraOp)logicalPlan.jjtGetChild(i);
			
			// init the attribute resolver
			initAttrs.visit(root, null);
			ArrayList data = new ArrayList();
			data.add(initAttrs.getAttributeResolver());
			
			// init the broker dictionary and
			// store all the broker names that occur in one
			// of the partial plans.
			initBroker.visit(root, null);
			ArrayList<String> newBrokerNames = (ArrayList<String>)initBroker.getBrokerNames();
			
			for(String brokerName: newBrokerNames){
				if(!this.brokerNames.contains(brokerName)){
					this.brokerNames.add(brokerName);
				}
			}
			
			// create the logical plan
			AbstractLogicalOperator topAO = (AbstractLogicalOperator)((ArrayList)createPlan.visit(root, data)).get(1);
			
			Query queryObj = new Query();
			queryObj.setParserId(getLanguage());
			queryObj.setLogicalPlan(topAO);

			listOfPlans.add(queryObj);
			
		}

		// now take all brokers and organize their transactions
		for(Entry<String, ILogicalOperator> entry: DataDictionary.getInstance().getViews()){
			if(entry.getValue() instanceof BrokerAO && this.brokerNames.contains(((BrokerAO)entry.getValue()).getIdentifier())){
				TransactionDetector.organizeTransactions((BrokerAO)entry.getValue());
			}
		}
		

		return listOfPlans;
	}
	
	/**
	 * @return A list of all broker names that have been detected in the
	 * last call of method parse(String)
	 */
	public ArrayList<String> getBrokerNames(){
		return this.brokerNames;
	}

	public List<IQuery> parse(Reader reader) throws QueryParseException {
		return null;
	}

	public String getLanguage() {
		return "PQLHack";
	}

}
