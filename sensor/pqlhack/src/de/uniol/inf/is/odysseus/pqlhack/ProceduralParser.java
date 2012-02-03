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
package de.uniol.inf.is.odysseus.pqlhack;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionaryEntry;
import de.uniol.inf.is.odysseus.broker.transaction.TransactionDetector;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAlgebraOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTLogicalPlan;
import de.uniol.inf.is.odysseus.pqlhack.parser.ParseException;
import de.uniol.inf.is.odysseus.pqlhack.parser.ProceduralExpressionParser;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.CreateLogicalPlanVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.InitAttributesVisitor;
import de.uniol.inf.is.odysseus.pqlhack.parser.visitor.InitBrokerVisitor;
import de.uniol.inf.is.odysseus.usermanagement.User;

@SuppressWarnings("all")

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

	private User user;
	private IDataDictionary dd;

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
	public List<IQuery> parse(String query, User user, IDataDictionary dd) throws QueryParseException {
		this.user = user;
		this.dd = dd;
		List<IQuery> listOfPlans = new ArrayList<IQuery>();
		this.brokerNames = new ArrayList<String>();
		
		InitAttributesVisitor initAttrs = new InitAttributesVisitor(user, dd);
		InitBrokerVisitor initBroker = new InitBrokerVisitor();
		CreateLogicalPlanVisitor createPlan = new CreateLogicalPlanVisitor(user, dd);
		
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
			ArrayList<Object> brokerData = new ArrayList<Object>();
			brokerData.add(false);
			brokerData.add(null);
			initBroker.visit(root, brokerData);
			//initBroker.visit(root, null);
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
			queryObj.setLogicalPlan(topAO, true);

			listOfPlans.add(queryObj);
			
		}

		// now take all brokers and organize their transactions
		for(Entry<String, BrokerDictionaryEntry> entry: BrokerDictionary.getInstance().getLogicalPlans().entrySet()){
			if(this.brokerNames.contains(entry.getValue().getBrokerName())){
				new TransactionDetector().organizeTransactions(entry.getValue().getLogicalPlan());
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

	public List<IQuery> parse(Reader reader, User user, IDataDictionary dd) throws QueryParseException {
		this.user = user;
		this.dd = dd;
		return null;
	}

	public String getLanguage() {
		return "PQLHack";
	}

}
