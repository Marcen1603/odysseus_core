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
package de.uniol.inf.is.odysseus.core.server.planmanagement.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.ISerializable;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.SerializeNode;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.SetOwnerGraphVisitor;
import de.uniol.inf.is.odysseus.core.sla.SLA;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Query is a standard implementation of a query in odysseus. Each query has an
 * unique ID and stores all relevant data (logical/physical plan, parser ID,
 * execution state etc.).
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public class Query implements ILogicalQuery {

	private static final long serialVersionUID = -7846033747726522915L;

	transient protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	/**
	 * Counter for ID creation.
	 */
	private static int idCounter = 0;

	/**
	 * Unique id of an ID. Used for identification of an query.
	 */
	private final int id;

	/**
	 * If available the text of the entered query
	 */
	private String queryText = null;

	/**
	 * The user who created this query
	 */
	transient private ISession user = null;

	/**
	 * ID of the parser that should be used to translate the query string.
	 */
	private String parserID;

	/**
	 * Logical root operator of this query
	 */
	private ILogicalOperator logicalPlan;

	private boolean containsCycles = false;

	/**
	 * Priority of this query.
	 */
	private int priority = 0;

	/**
	 * SLA based penalty
	 */
	private double penalty;

	/**
	 * Service level agreement of the query
	 */
	private SLA sla;

	public Query() {
		this("", null, null);
	}


	/**
	 * Creates a query based on a parser ID and {@link QueryBuildConfiguration}
	 * 
	 * @param parserID
	 *            ID of the parser to use
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public Query(String parserID, QueryBuildConfiguration parameters) {
		this(parserID, null, parameters);
	}

	/**
	 * Creates a query based on a logical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param logicalPlan
	 *            logical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	public Query(ILogicalOperator logicalPlan, QueryBuildConfiguration parameters) {
		this("", logicalPlan, parameters);
	}

	/**
	 * Creates a query based on a parserID, a logical plan, a physical plan and
	 * {@link QueryBuildConfiguration}
	 * 
	 * @param parserID
	 *            logical operator plan
	 * @param logicalPlan
	 *            logical operator plan
	 * @param physicalPlan
	 *            physical operator plan
	 * @param parameters
	 *            {@link QueryBuildConfiguration} for creating the query
	 */
	protected Query(String parserID, ILogicalOperator logicalPlan, QueryBuildConfiguration parameters) {
		this.id = idCounter++;
		this.parserID = parserID;
		this.logicalPlan = logicalPlan;
		
		if (parameters != null) {
			if (parameters.getPriority() != null) {
				this.priority = parameters.getPriority();
			}
		}
	}
	
	protected Query(Query query){
		// TODO: Sollte es die selbe ID sein? Konstruktor wird nur von PhysicalQuery verwendet
		// aber kann man das garantieren?
		this.id = query.id;
		
		
	}

	/**
	 * Provides an info string which describes the query.
	 * 
	 * @return info string which describes the query.
	 */
	public String getDebugInfo() {
		String info = "";
		info += "ID:" + this.id;
		info += "CompileLanguage:" + this.parserID;
		info += "LogicalAlgebra:" + AppEnv.LINE_SEPARATOR + this.logicalPlan;
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * setLogicalPlan(de.uniol.inf.is.odysseus.core.server.ILogicalOperator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setLogicalPlan(ILogicalOperator logicalPlan, boolean setOwner) {
		this.logicalPlan = logicalPlan;
		if (setOwner) {
			// Set Owner
			SetOwnerGraphVisitor<ILogicalOperator> visitor = new SetOwnerGraphVisitor<ILogicalOperator>(this);
			@SuppressWarnings("rawtypes")
			AbstractGraphWalker walker = new AbstractGraphWalker();
			walker.prefixWalk(logicalPlan, visitor);
		} else {
			if (!logicalPlan.hasOwner()) {
				throw new IllegalArgumentException("LogicalPlan must have an owner " + logicalPlan);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery#getPriority()
	 */
	@Override
	public int getPriority() {
		return this.priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery#setPriority
	 * (int)
	 */
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * getLogicalPlan()
	 */
	@Override
	public ILogicalOperator getLogicalPlan() {
		return this.logicalPlan;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.IOperatorOwner#getID()
	 */
	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public String getParserId() {
		return parserID;
	}

	@Override
	public String getQueryText() {
		return queryText;
	}

	@Override
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	@Override
	public void setUser(ISession user) {
		this.user = user;
	}

	@Override
	public ISession getUser() {
		return user;
	}

	@Override
	public void setParserId(String parserId) {
		this.parserID = parserId;
	}

	@Override
	public boolean containsCycles() {
		return containsCycles;
	}

	public void setContainsCycles(boolean containsCycles) {
		this.containsCycles = containsCycles;
	}

	@Override
	public String toString() {
		return "Query " + getID();
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Query other = (Query) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public double getPenalty() {
		return penalty;
	}

	@Override
	public void addPenalty(double penalty) {
		this.penalty += penalty;
	}

	@Override
	public SLA getSLA() {
		return this.sla;
	}

	@Override
	public void setSLA(SLA sla) {
		this.sla = sla;
	}


	@Override
	public SerializeNode serialize() {
		SerializeNode node = new SerializeNode(Query.class);
		List<ILogicalOperator> visitedOperators = new ArrayList<ILogicalOperator>();		
		ILogicalOperator operator = getLogicalPlan();
		serializeWalker(operator, visitedOperators, node);
		node.addPropertyValue("rootOperator", operator.hashCode());
		return node;
	}

	private void serializeWalker(ILogicalOperator op, List<ILogicalOperator> visitedOperators, SerializeNode list) {
		if (visitedOperators.contains(op)) {
			return;
		}
		visitedOperators.add(op);
		SerializeNode node = op.serialize();
		node.addPropertyValue("id", op.hashCode());
		list.addChild(node);
		for (LogicalSubscription sub : op.getSubscribedToSource()) {
			SerializeNode subNode = new SerializeNode(LogicalSubscription.class);
			subNode.addPropertyValue("sinkInPort", sub.getSinkInPort());
			subNode.addPropertyValue("sourceOutPort", sub.getSourceOutPort());
			subNode.addPropertyValue("from", op.hashCode());
			subNode.addPropertyValue("to", sub.getTarget().hashCode());
			list.addChild(subNode);
			serializeWalker(sub.getTarget(), visitedOperators, list);
		}
	}

	@Override
	public void deserialize(SerializeNode rootNode) {
		try {
			Map<String, ILogicalOperator> ops = new HashMap<String, ILogicalOperator>();
			for (SerializeNode node : rootNode.getChilds()) {
				if (ILogicalOperator.class.isAssignableFrom(node.getRepresentingClass())) {

					// create instance
					ISerializable s = (ISerializable) node.getRepresentingClass().newInstance();
					// reload properties
					s.deserialize(node);
					// memorize for subscriptions
					ops.put(node.getProperty("id").toString(), (ILogicalOperator) s);

				}
			}
			// strict serial, so that the operators are all available and
			// loaded!
			for (SerializeNode node : rootNode.getChilds()) {
				if (LogicalSubscription.class.isAssignableFrom(node.getRepresentingClass())) {
					String from = node.getProperty("from").toString();
					String to = node.getProperty("to").toString();
					int sinkInPort = Integer.parseInt(node.getProperty("sinkInPort").toString());
					int sourceOutPort = Integer.parseInt(node.getProperty("sourceOutPort").toString());
					// TODO: schema speichern
					ops.get(to).subscribeToSource(ops.get(from), sinkInPort, sourceOutPort, ops.get(from).getOutputSchema());
				}
			}
			
			//String hashCode = rootNode.getProperty("rootOperator").toString();
			//ILogicalOperator rootOperator = ops.get(hashCode); 
			//this.setLogicalPlan(rootOperator, getUser());
			System.out.println("ready");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public int compareTo(ILogicalQuery query) {
		if (this.id < query.getID()){
			return -1;
		}
		if (this.id > query.getID()){
			return 1;
		}
		return 0;
	}


	
	
}
