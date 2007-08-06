package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A list of {@link PlanOperator} objects, which are on the fly retrieved from
 * a database.
 * @author Jonas Jacobi
 * @todo TODO implement previous methods with internal caching
 */
public class PlanList extends AbstractSequentialList<PlanOperator> {

	Connection dbConnection;

	long afterTimestamp;

	/**
	 * The iterator used to iterate over a {@link PlanList}.
	 * @author Jonas Jacobi
	 */
	private class PlanListIterator implements ListIterator<PlanOperator> {
		private int index;

		private ResultSet result;

		private PlanOperator nextPlan;

		private Statement statement;

		private PlanOperator previousPlan;

		/**
		 * Constructor
		 * @param list the list the iterator iterates over
		 * @param afterTimestamp timestamp in milliseconds after 1970. All plans
		 * in the list were executed after this timestamp.
		 * @throws SQLException
		 */
		public PlanListIterator(PlanList list, long afterTimestamp)
				throws SQLException {
			this.index = 0;
			synchronized (dbConnection) {
				this.statement = dbConnection.createStatement();
			}
			this.result = this.statement
					.executeQuery("SELECT * FROM DQ_EXECUTEDPLANS WHERE EXECUTIONTIME > "
							+ afterTimestamp);
			fetchNext();
		}

		public boolean hasNext() {
			return nextPlan != null;
		}

		public boolean hasPrevious() {
			return previousPlan != null;
		}

		public PlanOperator next() {
			PlanOperator tmp = nextPlan;
			try {
				fetchNext();
			} catch (SQLException e) {
				// TODO evtl. auch ne runtime exception schmeissen
				e.printStackTrace();
				nextPlan = null;
				return null;
			}
			return tmp;
		}

		public int nextIndex() {
			return this.index;
		}

		// TODO buggy wegen next plan noch fixen
		public PlanOperator previous() {
			PlanOperator tmp = previousPlan;
			try {
				fetchPrevious();
			} catch (SQLException e) {
				// TODO evtl. auch ne runtime exception schmeissen
				e.printStackTrace();
				nextPlan = null;
				return null;
			}
			return tmp;
		}

		public int previousIndex() {
			return this.index - 1;
		}

		public void remove() {
			throw new NotImplementedException();
		}

		public void set(PlanOperator o) {
			throw new NotImplementedException();
		}

		public void add(PlanOperator o) {
			throw new NotImplementedException();
		}

		/**
		 * fetch the next plan from the resultset
		 * @throws SQLException gets thrown if next plan couldn't be retrieved
		 */
		private void fetchNext() throws SQLException {
			if (!result.next()) {
				if (this.nextPlan == null) {
					throw new NoSuchElementException();
				}
				this.nextPlan = null;
				this.statement.close();
			} else {
				this.nextPlan = createPlan(result.getClob("Plan"));
				++index;
			}
		}

		/**
		 * fetch the previous plan from the resultset
		 * @throws SQLException gets thrown if the previous plan couldn't be retrieved
		 */
		private void fetchPrevious() throws SQLException {
			if (!result.previous()) {
				if (this.previousPlan == null) {
					throw new NoSuchElementException();
				}
				this.previousPlan = null;
				this.statement.close();
			} else {
				this.previousPlan = createPlan(result.getClob("Plan"));
				--index;
			}
		}

		/**
		 * Create a {@link PlanOperator} object out of the xml planrepresentation
		 * stored in a {@link Clob}.
		 * @param plan clob containing the xml planrepresentation
		 * @return the {@link PlanOperator} planrepresentation of plan
		 */
		private PlanOperator createPlan(Clob plan) {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			// store uids with their planoperator
			Map<String, PlanOperator> pos = new HashMap<String, PlanOperator>();
			// store planoperator and all uids of its subplanoperators
			Map<PlanOperator, List<String>> subOperators = new HashMap<PlanOperator, List<String>>();
			PlanOperator first = null;

			try {
				DocumentBuilder docBuilder = docBuilderFactory
						.newDocumentBuilder();
				// use clobfilter to filter out trailing '\0' chars, otherwise
				// the parser fails
				Document doc = docBuilder.parse(new ClobFilter(plan
						.getCharacterStream()));
				NodeList poList = doc.getDocumentElement()
						.getElementsByTagName("po");
				for (int i = 0; i < poList.getLength(); ++i) {
					Element root = (Element) poList.item(i);

					String type = root.getAttribute("class");
					int n = type.lastIndexOf('.');
					if (n == -1) {
						throw new Exception(
								"invalid or missing class attribute");
					}
					type = type.substring(n + 1);

					String guid = root.getAttribute("id");
					if (guid.length() == 0) {
						throw new Exception("missing id attribute");
					}

					String bufferSizeStr = root
							.getAttribute("outputBufferSize");
					int bufferSize = 1000; // value of old plans without
											// explicitly set bufferSize
					if (bufferSizeStr.length() != 0) {
						bufferSize = Integer.parseInt(bufferSizeStr);
					}

					String source = null;
					Element algebra = (Element) root.getElementsByTagName(
							"algebraPO").item(0);
					if (algebra != null) {
						NodeList sourceNodes = algebra
								.getElementsByTagName("source");
						if (sourceNodes.getLength() > 0) {
							source = sourceNodes.item(0).getTextContent();
						}
					}

					List<String> curSubOperators = new ArrayList<String>();
					NodeList inputs = root.getElementsByTagName("input");
					for (int j = 0; j < inputs.getLength(); ++j) {
						curSubOperators.add(((Element) inputs.item(j))
								.getAttribute("idref"));
					}

					PlanOperator op = new PlanOperator(guid, type, bufferSize,
							source);
					if (first == null) {
						first = op;
					}
					pos.put(op.getGuid(), op);
					subOperators.put(op, curSubOperators);
				}

				// assign subplanoperators and parents
				for (Map.Entry<PlanOperator, List<String>> curEntry : subOperators
						.entrySet()) {
					PlanOperator curPo = curEntry.getKey();

					for (String uid : curEntry.getValue()) {
						PlanOperator subPo = pos.get(uid);
						if (subPo == null) {
							throw new Exception("no such plan operator: " + uid);
						}
						curPo.addSubOperator(subPo);
						subPo.setParent(curPo);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			// make sure the topmost planoperator is returned
			while (first.getParent() != null) {
				first = first.getParent();
			}
			return first;
		}

	}

	/**
	 * Constructor
	 * @param dbConnection the database containing the plans
	 * @param afterTimestamp timestamp in milliseconds after 1970.
	 * the list will contain only plans after this timestamp
	 * (if you want all plans just pass 0)
	 */
	public PlanList(Connection dbConnection, long afterTimestamp) {
		this.dbConnection = dbConnection;
		this.afterTimestamp = afterTimestamp;
	}

	@Override
	public ListIterator<PlanOperator> listIterator(int arg0) {
		try {
			PlanListIterator it = new PlanListIterator(this, afterTimestamp);
			for (int i = 0; i < arg0; ++i) {
				it.next();
			}
			return it;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The size of the list.
	 * WARNING! DO NOT rely on the size. Use
	 * the iterators next/previous methods to access elements. 
	 * The size is retrieved via a database query (SELECT COUNT ...) and thus may be inaccurate,
	 * but it is never smaller than the actual size.
	 * This is unfortunately necessary to have an acceptable performance of the size call
	 * for larger databases. 
	 */
	@Override
	public int size() {
		Statement stmt;
		try {
			synchronized (dbConnection) {
				stmt = dbConnection.createStatement();
			}
			ResultSet result = stmt
					.executeQuery("SELECT COUNT(*) FROM DQ_EXECUTEDPLANS WHERE EXECUTIONTIME IS NOT NULL");
			if (!result.next()) {
				throw new RuntimeException(
						"No result for 'SELECT COUNT(*) FROM DQ_EXECUTEDPLANS'");
			}
			return result.getInt(1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
