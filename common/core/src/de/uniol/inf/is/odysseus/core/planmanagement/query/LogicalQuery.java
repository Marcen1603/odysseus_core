/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.planmanagement.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.ISerializable;
import de.uniol.inf.is.odysseus.core.logicaloperator.serialize.SerializeNode;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logicalQueryInfo", propOrder = { "id", "queryText", "parserID", "containsCycles", "priority", "name",
		"notice", "parameters", "userParameters" })
public class LogicalQuery implements ILogicalQuery {

	private static final long serialVersionUID = -7357156628145329724L;

	/**
	 * Counter for ID creation.
	 */
	private static int idCounter = 0;

	/**
	 * Unique id of an ID. Used for identification of an query.
	 */
	private final int id;

	/**
	 * Name of the query.
	 */
	private Resource name;

	/**
	 * If available the text of the entered query
	 */
	private String queryText = null;

	/**
	 * The user who created this query
	 */
	@XmlTransient
	private ISession user = null;

	/**
	 * ID of the parser that should be used to translate the query string.
	 */
	private String parserID;

	/**
	 * Logical root operator of this query
	 */
	@XmlTransient
	private ILogicalPlan logicalPlan;

	@XmlTransient
	private List<ILogicalPlan> alternativeLogicalPlans;

	@XmlTransient
	private ILogicalPlan initialLogicalPlan;

	private boolean containsCycles = false;

	/**
	 * Priority of this query.
	 */
	private int priority = 0;

	// Parameter as List because of web service
	final private ArrayList<Pair<String, Object>> parameters = new ArrayList<>();

	final private HashMap<String, String> userParameters = new HashMap<>();

	final transient Map<String, Object> transParams = new HashMap<>();

	final transient private Map<String, Object> serverParameters = new HashMap<>();

	private String notice;

	// TODO: check in which constructors alternativeLogicalPlans have to be set!

	/**
	 *
	 * @param parserID
	 * @param logicalPlan
	 * @param priority
	 */
	public LogicalQuery(String parserID, ILogicalOperator logicalPlan, int priority) {
		this.id = idCounter++;
		this.parserID = parserID;
		this.logicalPlan = new LogicalPlan(logicalPlan);
		this.priority = priority;
		this.alternativeLogicalPlans = new ArrayList<ILogicalPlan>();
	}

	/**
	 *
	 * @param parserID
	 * @param logicalPlan
	 * @param priority
	 */
	public LogicalQuery(String parserID, ILogicalPlan logicalPlan, int priority) {
		this.id = idCounter++;
		this.parserID = parserID;
		this.logicalPlan = logicalPlan;
		this.priority = priority;
		this.alternativeLogicalPlans = new ArrayList<ILogicalPlan>();
	}

	/**
	 *
	 * Use this constructor only if a query is copied
	 *
	 * @param id
	 * @param parserID
	 * @param logicalPlan
	 * @param priority
	 */
	public LogicalQuery(int id, String parserID, ILogicalPlan logicalPlan, int priority) {
		this.id = id;
		this.parserID = parserID;
		this.logicalPlan = logicalPlan;
		this.priority = priority;
		this.alternativeLogicalPlans = new ArrayList<ILogicalPlan>();
	}

	public LogicalQuery(ILogicalOperator logicalPlan, int priority) {
		this("", logicalPlan, priority);
	}

	public LogicalQuery() {
		this("", (ILogicalPlan) null, 0);
	}

	public LogicalQuery(int id) {
		this(id, "", null, 0);
	}

	@Override
	public Resource getName() {
		return name;
	}

	@Override
	public void setName(Resource name) {
		this.name = name;
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
		info += "LogicalAlgebra:" + "\n" + this.logicalPlan;
		return info;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery#
	 * setLogicalPlan(de.uniol.inf.is.odysseus.core.server.ILogicalOperator)
	 */
	@Override
	public void setLogicalPlan(ILogicalPlan logicalPlan, boolean setOwner) {
		this.logicalPlan = logicalPlan;
		if (setOwner) {
			logicalPlan.setOwner(this);
		} else {
			if (!logicalPlan.getRoot().hasOwner()) {
				throw new IllegalArgumentException("LogicalPlan must have an owner " + logicalPlan);
			}
		}
	}

	@Override
	public void setLogicalPlan(ILogicalOperator logicalPlan, boolean setOwner) {
		setLogicalPlan(new LogicalPlan(logicalPlan), setOwner);
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery#getPriority
	 * ()
	 */
	@Override
	public int getPriority() {
		return this.priority;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQuery#setPriority
	 * (int)
	 */
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seede.uniol.inf.is.odysseus.planmanagement.query.IQuery# getLogicalPlan()
	 */
	@Override
	public ILogicalPlan getLogicalPlan() {
		return this.logicalPlan;
	}

	@Override
	public void setAlternativeLogicalPlans(List<ILogicalPlan> altPlans) {
		this.alternativeLogicalPlans = altPlans;
	}

	@Override
	public List<ILogicalPlan> getAlternativeLogicalPlans() {
		return this.alternativeLogicalPlans;
	}

	@Override
	public void setInitialLogicalPlan(ILogicalPlan initialPlan) {
		this.initialLogicalPlan = initialPlan;
	}

	@Override
	public ILogicalPlan getInitialLogicalPlan() {
		return this.initialLogicalPlan;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.IOperatorOwner#getID
	 * ()
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
	public ISession getSession() {
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
		LogicalQuery other = (LogicalQuery) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int compareTo(IOperatorOwner query) {
		if (this.id < query.getID()) {
			return -1;
		}
		if (this.id > query.getID()) {
			return 1;
		}
		return 0;
	}

	@Override
	public SerializeNode serialize() {
		SerializeNode node = new SerializeNode(LogicalQuery.class);
		List<ILogicalOperator> visitedOperators = new ArrayList<ILogicalOperator>();
		ILogicalOperator operator = getLogicalPlan().getRoot();
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
			subNode.addPropertyValue("to", sub.getSource().hashCode());
			list.addChild(subNode);
			serializeWalker(sub.getSource(), visitedOperators, list);
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
					ops.get(to).subscribeToSource(ops.get(from), sinkInPort, sourceOutPort,
							ops.get(from).getOutputSchema());
				}
			}

			// String hashCode =
			// rootNode.getProperty("rootOperator").toString();
			// ILogicalOperator rootOperator = ops.get(hashCode);
			// this.setLogicalPlan(rootOperator, getUser());
			System.out.println("ready");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setParameter(String key, Object value) {
		Pair<String, Object> p = new Pair<>(key, value);
		if (getP(key) != null) {
			parameters.remove(p);
		}
		parameters.add(p);
		transParams.put(key, value);
	}

	@Override
	public void setServerParameter(String key, Object value) {
		serverParameters.put(key, value);
	}

	@Override
	public void setUserParameter(String key, String value) {
		userParameters.put(key, value);
	}

	@Override
	public Object getParameter(String key) {
		Object v = transParams.get(key);
		if (v == null) {
			Pair<String, Object> p = getP(key);
			if (p != null) {
				v = p.getE2();
			}
		}
		return v;
	}

	@Override
	public Object getServerParameter(String key) {
		return serverParameters.get(key);
	}

	@Override
	public String getUserParameter(String key) {
		return userParameters.get(key);
	}

	private Pair<String, Object> getP(String key) {
		// Avoid ConcurrentModificationExceptions
		List<Pair<String, Object>> list = Lists.newArrayList(parameters);
		for (Pair<String, Object> p : list) {
			if (p.getE1().equals(key)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public void done(IOwnedOperator op) {
		// Ignore in logical query
	}

	@Override
	public String getNotice() {
		return notice;
	}

	@Override
	public void setNotice(String notice) {
		this.notice = notice;
	}

	@SuppressWarnings({ "all" })
	public void setParameters(List<Pair> parameters) {
		for (Pair<String, Object> p : parameters) {
			setParameter(p.getE1(), p.getE2());
		}
	}
}
