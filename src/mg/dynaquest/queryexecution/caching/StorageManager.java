/**
 * 
 */
package mg.dynaquest.queryexecution.caching;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import mg.dynaquest.queryexecution.caching.memory.ConstraintFormula;
import mg.dynaquest.queryexecution.caching.memory.DataTuple;
import mg.dynaquest.queryexecution.caching.memory.SemanticRegion;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.description.SDFSource;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFEqualOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFGreaterOrEqualThanOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFGreaterThanOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFLowerOrEqualThanOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFLowerThanOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFNumberCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFNumberPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFStringCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFStringPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFUnequalOperator;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFExpression;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes;

import org.apache.log4j.Logger;

/**
 * 
 * Diese Klasse beinhaltet den Storage Manager. Sie implementiert ein
 * thread-safe Singleton Pattern und wird von den Caching POs durch Lazy Loading
 * instantiiert.
 * 
 * @author Tobias Hesselmann
 * 
 */
public class StorageManager implements Replacement, Invalidation {

	/* Datenbank Verbindung */
	private Connection dbConnection = null;

	/* tupleID wird hier zentral verwaltet */
	private int tupleId = -1;

	/* Logger Instanz */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	private String sessionId = "DEFAULT_SESSION";

	/**
	 * 
	 * @return Nächste freie TupelId in der Datenbank
	 */
	public synchronized int getNextTupleId() {
		this.tupleId++;
		return this.tupleId;
	}

	/**
	 * Löscht alle Inhalte aus dem Cache Memory
	 * 
	 * @throws SQLException
	 */
	public synchronized void flush() throws SQLException {

		logger.debug("Lösche Cache Memory!");

		/* Lösche Constraint Formel */
		String sql = "DELETE FROM ConstraintFormulae";
		PreparedStatement pstmt = dbConnection.prepareStatement(sql);
		pstmt.executeUpdate();
		pstmt.close();

		/* Lösche Inhalte */
		sql = "DELETE FROM DataTuples";
		pstmt = dbConnection.prepareStatement(sql);
		pstmt.executeUpdate();
		pstmt.close();

		/* Lösche semantische Region */
		sql = "DELETE FROM SemanticRegions";
		pstmt = dbConnection.prepareStatement(sql);
		pstmt.executeUpdate();
		pstmt.close();

		/* Ausführung der Transaktion */
		dbConnection.commit();
	}

	/**
	 * Speichert semantische Region im Cache Memory
	 * 
	 * @param semanticRegion
	 * @throws SQLException
	 * @throws SQLException
	 */
	public synchronized void saveSemanticRegion(SemanticRegion semanticRegion)
			throws SQLException {

		ConstraintFormula constraintFormula = semanticRegion
				.getConstraintFormula();
		SDFSource source = semanticRegion.getSource();
		ArrayList<DataTuple> dataTuples = semanticRegion.getDataTuples();

		/**
		 * ID der semantischen Region
		 */
		int semanticRegionId = -1;

		logger.info("Speichere semantische Region");

		/*
		 * Wenn semantische Region größer als Cache Memory ist, kann diese nicht
		 * gespeichert werden
		 */

		if (!(semanticRegion.size() > CacheManager.getInstance().caching_tupleCapacity)) {
			/*
			 * Solange neue semantische Region nicht angelegt werden kann,
			 * lösche semantische Regionen aus dem Cache Memory, angefangen mit
			 * der Region mit dem ältesten Timestamp.
			 */

			while ((semanticRegion.size() + getCacheSize()) > CacheManager
					.getInstance().caching_tupleCapacity) {
				SemanticRegion sr = getSemanticRegionToReplace();
				sr.delete();
			}

			/* Erzeuge neue semantische Region */

			String sql = "INSERT INTO SemanticRegions(Source, ReplacementValue, TimeStamp, LastAccess, SessionId) VALUES (?,'0',NOW(),NOW(),?)";
			PreparedStatement pstmt = dbConnection.prepareStatement(sql);
			pstmt.setString(1, source.getURI(false));
			pstmt.setString(2, this.sessionId);
			pstmt.executeUpdate();
			ResultSet resultSet = pstmt.getGeneratedKeys();

			/* Hole ID der gerade erzeugten semantischen Region */
			if (resultSet != null && resultSet.next()) {
				semanticRegionId = resultSet.getInt(1);
			}
			pstmt.close();

			/*
			 * Lege Constraint Formel für semantische Region an, in dem alle
			 * SimplePredicates zu der Region gespeichert werden
			 */

			for (SDFSimplePredicate predicate : constraintFormula) {
				String value = null;
				if (predicate instanceof SDFStringPredicate) {
					value = predicate.getValue().getString();
				} else if (predicate instanceof SDFNumberPredicate) {
					value = String.valueOf(predicate.getValue().getDouble());
				}
				
				sql = "INSERT INTO ConstraintFormulae(ParentId, PredicateName, Operator, Value, Type) VALUES "
						+ "("
						+ semanticRegionId
						+ ",'"
						+ predicate.getAttribute().toString()
						+ "','"
						+ predicate.getCompareOp().toString()
						+ "','"
						+ value
						+ "','"
						+ predicate.getAttribute().getDatatype().getURI(false)
						+ "')";
				pstmt = dbConnection.prepareStatement(sql);
				pstmt.executeUpdate();
				pstmt.close();
			}

			/*
			 * Speichere Datentupel der semantischen Region
			 */
			for (DataTuple tuple : dataTuples) {

				sql = "INSERT INTO DataTuples(ParentId, TupleId, Attribute, Value, Datatype) VALUES "
						+ "("
						+ semanticRegionId
						+ ","
						+ tuple.getId()
						+ ",'"
						+ tuple.getKey()
						+ "','"
						+ tuple.getValue()
						+ "','"
						+ tuple.getDatatype() + "')";
				pstmt = dbConnection.prepareStatement(sql);
				pstmt.executeUpdate();
				pstmt.close();
			}

			/* Ausführung der Transaktion */
			dbConnection.commit();
		} else { // Remainder Query zu groß für Cache
			logger
					.warn("Warnung: Semantische Region konnte nicht angelegt werden, da zu groß für Cache Memory!");
		}
	}

	public SemanticRegion getSemanticRegionToReplace() {

		/* ID ältester Region herausfinden */
		String sql = "SELECT `Id` FROM `SemanticRegions` WHERE `LastAccess` = (SELECT MIN(`LastAccess`) FROM `SemanticRegions`)";
		PreparedStatement pstmt;
		SemanticRegion semanticRegion = null;

		try {
			pstmt = dbConnection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				semanticRegion = new SemanticRegion(rs.getInt("Id"));
			}

			/* Ausführung der Transaktion */
			dbConnection.commit();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return semanticRegion;
	}

	/**
	 * 
	 * @return Anzahl von Datentupeln im Cache Memory
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private int getCacheSize() throws SQLException {

		/* Zähle TupelIds */
		String sql = "SELECT COUNT(1) FROM `DataTuples` GROUP BY `TupleId`";

		PreparedStatement pstmt = dbConnection.prepareStatement(sql);
		pstmt.executeQuery();
		pstmt.close();

		/* Ausführung der Transaktion */
		dbConnection.commit();

		return pstmt.getFetchSize();
	}

	/**
	 * Löscht eine semantische Region aus dem Cache Memory
	 * 
	 * @param semanticRegion
	 *            Zu löschende semantische Region
	 * @throws SQLException
	 */
	public void deleteSemanticRegion(SemanticRegion semanticRegion)
			throws SQLException {

		logger.debug("Lösche semantische Region " + semanticRegion.getId());

		/* Lösche Constraint Formel */
		String sql = "DELETE FROM ConstraintFormulae WHERE ParentId=?";
		PreparedStatement pstmt = dbConnection.prepareStatement(sql);
		pstmt.setInt(1, +semanticRegion.getId());
		pstmt.executeUpdate();
		pstmt.close();

		/* Lösche Inhalte */
		sql = "DELETE FROM DataTuples WHERE ParentId=?";
		pstmt = dbConnection.prepareStatement(sql);
		pstmt.setInt(1, +semanticRegion.getId());
		pstmt.executeUpdate();
		pstmt.close();

		/* Lösche semantische Region */
		sql = "DELETE FROM SemanticRegions WHERE Id=?";
		pstmt = dbConnection.prepareStatement(sql);
		pstmt.setInt(1, semanticRegion.getId());
		pstmt.executeUpdate();
		pstmt.close();

		/* Ausführung der Transaktion */
		dbConnection.commit();
	}

	/**
	 * Stellt Verbindung zur Datenbank her
	 * 
	 * @return Database connection
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws SQLException
	 */
	private Connection getDataBaseConnection() throws ClassNotFoundException,
			SQLException {
		if (this.dbConnection == null) {
			Class.forName(CacheManager.getInstance().caching_driverClass);
			this.dbConnection = DriverManager.getConnection(CacheManager
					.getInstance().caching_jdbcstring, CacheManager
					.getInstance().caching_db_user,
					CacheManager.getInstance().caching_db_password);
			this.dbConnection.setAutoCommit(false);
		}
		return this.dbConnection;
	}

	/*
	 * Private constructor suppresses generation of a (public) default
	 * constructor
	 */
	private StorageManager() {

		/* Datenbankverbindung bei Initialisierung herstellen */
		try {
			getDataBaseConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static class StorageManagerHolder {
		private final static StorageManager INSTANCE = new StorageManager();
	}

	public static StorageManager getInstance() {
		return StorageManagerHolder.INSTANCE;
	}

	/**
	 * 
	 * @param tupleId
	 * @param queryAttributeList
	 * @return Ein RelationalTuple, das Inhalte des angeforderten Tupels enthält
	 */
	public RelationalTuple getProbeQueryTuple(int tupleId,
			SDFAttributeList queryAttributeList) {
		/*
		 * Überprüfen, ob das übergebene Tupel noch weitere Attribute enthält,
		 * die angefragt wurden. Falls ja, werden diese zur Probe Query
		 * hinzugefügt
		 */

		HashMap tuple = null;

		/*
		 * Alle Attribute des Tupels aus dem Cache Memory einlesen
		 */
		try {
			tuple = getTupleAttributes(tupleId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		RelationalTuple relationalTuple = new RelationalTuple(
				queryAttributeList.size());

		/* Durch Liste der Attribute gehen und RelationalTuple erzeugen */
		for (SDFSchemaElement attribute : queryAttributeList) {
			if (tuple.containsKey(attribute.getURI(false))) {
				relationalTuple.setAttribute(queryAttributeList
						.indexOf(attribute), (String) tuple.get(attribute
						.getURI(false)));
			} else {
				relationalTuple.setAttribute(queryAttributeList
						.indexOf(attribute), null);
			}
		}
		return relationalTuple;
	}

	/**
	 * 
	 * @param dataSource
	 *            URI einer DatenQuelle
	 * @return Menge von semantischen Regionen, welche Inhalte zu einer Quelle
	 *         beinhalten
	 * @throws SQLException
	 */
	public HashSet<SemanticRegion> getMatchingRegionsForSource(
			SDFSource dataSource) throws SQLException {
		HashSet<SemanticRegion> matchingTuples = new HashSet<SemanticRegion>();
		String sql = "SELECT `Id` FROM `SemanticRegions` WHERE `Source` = ?";
		PreparedStatement pstmt = dbConnection.prepareStatement(sql);
		pstmt.setString(1, dataSource.getURI(false));
		ResultSet rs = pstmt.executeQuery();

		/* Ausführen der Anfrage */
		dbConnection.commit();

		/* Map mit Werten und Attributen pro Tupel erzeugen */
		while (rs.next()) {
			matchingTuples.add(new SemanticRegion(rs.getInt("Id")));
		}

		pstmt.close();

		return matchingTuples;
	}

	/**
	 * Holt alle Attribute eines Tupels aus dem Cache Memory
	 * 
	 * @param id
	 *            Id des Tupels, für das Werte extrahiert werden sollen
	 *            (Achtung: Nicht "TupleId" sondern "Id" muss hier übergeben
	 *            werden)
	 * @return Ein Datenbank-Tupel als Key/Value-Map (Attribute/Werte)
	 * @throws SQLException
	 */
	private HashMap getTupleAttributes(int id) throws SQLException {
		ResultSet tuples;
		String sql = "SELECT `Attribute`, `Value`, `DataType` FROM `DataTuples` WHERE `TupleId` = (SELECT `TupleId` FROM `DataTuples` WHERE `Id` = ?)";
		PreparedStatement pstmt = dbConnection.prepareStatement(sql);
		pstmt.setInt(1, id);
		tuples = pstmt.executeQuery();

		/* Ausführen der Anfrage */
		dbConnection.commit();

		HashMap<String, String> tupleMap = new HashMap<String, String>();

		/* Map mit Werten und Attributen pro Tupel erzeugen */
		while (tuples.next()) {
			tupleMap.put(tuples.getString(1), tuples.getString(2));
		}

		pstmt.close();

		return tupleMap;
	}

	/**
	 * @param matchingRegions
	 * @param queryPredicateList
	 *            Anfrageprädiakte
	 * @return IDs von Tupeln, welche zu Anfrageprädikat und Quelle passende
	 *         Inhalte enthalten
	 * @throws SQLException
	 */
	public HashSet<Integer> getMatchingTupleIds(
			ArrayList<SDFSimplePredicate> queryPredicateList,
			HashSet<SemanticRegion> matchingRegions) throws SQLException {

		/*
		 * Liste mit Mengen, die jeweils TupelIds beinhalten, welche zu dem
		 * jeweiligen Prädikat passen
		 */
		ArrayList<HashSet<Integer>> tupleSets = new ArrayList<HashSet<Integer>>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String matchingRegionsSQLString = getMatchingRegionsSqlString(matchingRegions);

		/*
		 * Für jedes Prädikat in der Anfrage: Hole TupelIDs, die dieses
		 * enthalten. 
		 */
		for (SDFSimplePredicate predicate : queryPredicateList) {
			String predicateOperator = predicate.getCompareOp().toString();
			String sql = "SELECT `Id` FROM `DataTuples` WHERE ("
					+ matchingRegionsSQLString
					+ ") AND `Attribute` = ? AND `Value` " + predicateOperator
					+ " ?";

			pstmt = dbConnection.prepareStatement(sql);
			pstmt.setString(1, predicate.getAttribute().getURI(false));
			if (predicate instanceof SDFStringPredicate) {
				pstmt.setString(2, predicate.getValue().getString());
			} else if (predicate instanceof SDFNumberPredicate) {
				pstmt.setString(2, String.valueOf(predicate.getValue().getDouble()));
			}

			rs = pstmt.executeQuery();

			/* Ausführung der Transaktion */
			dbConnection.commit();

			HashSet<Integer> tupleSet = new HashSet<Integer>();

			while (rs.next()) {
				tupleSet.add(rs.getInt("Id"));
			}

			pstmt.close();

			/* Hinzufügen der Menge matchender TupelIds */
			tupleSets.add(tupleSet);
		}

		/*
		 * Durchschnitt von allen Mengen bilden, um Tupel zu finden, die alle
		 * Prädikate beantworten
		 */

		if (tupleSets.size() > 1) {
			for (int i = 0; i < tupleSets.size() - 1; i++) {
				tupleSets.get(i).retainAll(tupleSets.get(i + 1));
			}
		}

		return tupleSets.get(0);
	}

	private String getMatchingRegionsSqlString(
			HashSet<SemanticRegion> matchingRegions) {
		int cnt = 0;
		String matchingRegionsSQLString = "";

		/*
		 * Liste mit semantischen Regionen wird in einen SQL-String konvertiert,
		 * so dass die entsprechenden Regionen selektiert werden können
		 */
		for (SemanticRegion region : matchingRegions) {
			if (cnt == 0) {
				matchingRegionsSQLString = " `ParentId` = "
						+ String.valueOf(region.getId());
			} else {
				matchingRegionsSQLString += " OR `ParentId` = "
						+ String.valueOf(region.getId());
			}
			cnt++;
		}
		return matchingRegionsSQLString;
	}

	/**
	 * 
	 * 
	 * @param dataSource
	 *            URI der Quelle
	 * @param queryPredicateList
	 *            Liste mit Prädikaten
	 * @return Liste semantischer Regionen, die zu einer Quelle und einem
	 *         gegebenen Prädikat Inhalte enthalten
	 * @throws SQLException
	 */
	public HashSet<SemanticRegion> getMatchingRegions(SDFSource dataSource,
			ArrayList<SDFSimplePredicate> queryPredicateList)
			throws SQLException {

		/*
		 * Finde semantische Regionen, welche Inhalte zu einer gegebenen Quelle
		 * liefern
		 */
		HashSet<SemanticRegion> sourceMatchingRegions = this
				.getMatchingRegionsForSource(dataSource);

		/*
		 * Nun finde semantische Regionen, welche Inhalte zu dem Prädikat aus
		 * der Anfrage enthalten TODO optimieren!
		 */
		HashSet<Integer> matchingTuples = this.getMatchingTupleIds(
				queryPredicateList, sourceMatchingRegions);

		HashSet<SemanticRegion> tupleMatchingRegions = getSemanticRegions(matchingTuples);

		return tupleMatchingRegions;
	}

	/**
	 * 
	 * @param matchingTuples
	 * @return Menge semantischer Regionen zu gegebener Menge von Tupeln
	 * @throws SQLException
	 */
	public HashSet<SemanticRegion> getSemanticRegions(
			HashSet<Integer> matchingTuples) throws SQLException {
		HashSet<SemanticRegion> matchingRegions = new HashSet<SemanticRegion>();

		if (matchingTuples.size() > 0) {

			/*
			 * Finde heraus, welchen semantischen Regionen die Tupel zugeordnet
			 * sind
			 */
			String tupleString = "";
			for (int entry : matchingTuples) {
				if (tupleString.equals("")) {
					tupleString += entry;
				} else {
					tupleString += " OR `Id` = " + entry;
				}
			}

			String sql = "SELECT `ParentId` FROM `DataTuples` WHERE `Id` = "
					+ tupleString;
			PreparedStatement pstmt = dbConnection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			/* Ausführung der Transaktion */
			dbConnection.commit();

			while (rs.next()) {
				SemanticRegion sr = new SemanticRegion(rs.getInt("ParentId"));
				matchingRegions.add(sr);
			}

			pstmt.close();
		}
		return matchingRegions;
	}

	/**
	 * 
	 * @param semanticRegion
	 * @return ConstraintFormel zu gegebener semantischer Region
	 * @throws SQLException
	 */
	public ConstraintFormula getConstraintFormula(SemanticRegion semanticRegion)
			throws IllegalArgumentException, SQLException {
		ConstraintFormula constraintFormula = new ConstraintFormula();

		/* Hole Prädikate zu gegebener semantischer Region */
		String sql = "SELECT `PredicateName`, `Operator`, `Value`, `Type` FROM `ConstraintFormulae` WHERE `ParentId` = ?";
		PreparedStatement pstmt = dbConnection.prepareStatement(sql);
		pstmt.setInt(1, semanticRegion.getId());
		ResultSet rs = pstmt.executeQuery();

		/* Ausführung der Transaktion */
		dbConnection.commit();

		while (rs.next()) {
			SDFSimplePredicate predicate;
			String predicateType = rs.getString("Type");

			/* Für String Prädikate */
			if (predicateType.equals(SDFDatatypes.String)) {
				SDFCompareOperator op = null;
				SDFAttribute attribute = new SDFAttribute(rs
						.getString("PredicateName"));
				if (rs.getString("Operator").equals(
						new SDFEqualOperator().toString())) {
					op = new SDFEqualOperator();
				} else {
					op = new SDFUnequalOperator();
				}
				SDFConstant sdfConstant = new SDFStringConstant("", rs
						.getString("Value"));
				predicate = new SDFStringPredicate("cachingTempPredicate",
						attribute, (SDFStringCompareOperator) op, sdfConstant);

				/* Für Number Prädikate */
			} else if (predicateType.equals(SDFDatatypes.Number)) {
				SDFCompareOperator op = null;
				SDFAttribute attribute = new SDFAttribute(rs
						.getString("PredicateName"));
				String operator = rs.getString("Operator");
				if (operator.equals(new SDFEqualOperator().toString())) {
					op = new SDFEqualOperator();
				} else if (operator.equals(new SDFUnequalOperator().toString())) {
					op = new SDFUnequalOperator();
				} else if (operator.equals(new SDFGreaterOrEqualThanOperator()
						.toString())) {
					op = new SDFGreaterOrEqualThanOperator();
				} else if (operator.equals(new SDFGreaterThanOperator()
						.toString())) {
					op = new SDFGreaterThanOperator();
				} else if (operator.equals(new SDFLowerOrEqualThanOperator()
						.toString())) {
					op = new SDFLowerOrEqualThanOperator();
				} else if (operator.equals(new SDFLowerThanOperator()
						.toString())) {
					op = new SDFLowerThanOperator();
				}

				SDFExpression sdfConstant = new SDFExpression("", rs
						.getString("Value"));
				predicate = new SDFNumberPredicate("cachingTempPredicate",
						attribute, (SDFNumberCompareOperator) op, sdfConstant);

				/* Andere Operatoren werden derzeit nicht unterstützt */
			} else {
				logger.error("Prädikat-Operator nicht unterstützt!");
				throw new IllegalArgumentException(
						"Prädikat-Operator nicht unterstützt!");
			}

			constraintFormula.add(predicate);
		}

		pstmt.close();

		return constraintFormula;
	}

	/**
	 * Invalidiert semantische Regionen, d.h. identifiziert und löscht diese
	 * 
	 * @param sessionId
	 * 
	 * @param sessionId
	 */
	public synchronized void invalidateSemanticRegions() {
		/* Hole IDs semantischer Regionen */
		String sql = "SELECT `Id` FROM `SemanticRegions`";

		PreparedStatement pstmt;
		try {
			pstmt = dbConnection.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();

			/* Ausführung der Transaktion */
			dbConnection.commit();

			while (rs.next()) {
				SemanticRegion sr = new SemanticRegion(rs.getInt("Id"));

				if (!sr.isValid()) {
					logger.info("Invalidiere semantische Region " + sr.getId()
							+ "\n");
					sr.delete();
				} else {
					logger.info("Semantische Region" + sr.getId() + " ist gültig");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param semanticRegion
	 * @return true, falls semantische Region konsistent ist, false sonst
	 */
	public boolean isSemanticRegionValid(SemanticRegion semanticRegion) {

		/*
		 * Falls semantische Region während aktueller Session angelegt wurde,
		 * immer gültig
		 */
		if (isSessionValid(semanticRegion)) {
			return true;
		} else {
			/* Sonst überprüfen, ob Zeit abgelaufen ist */
			Date date = new Date();
			Timestamp currentTimeStamp = new Timestamp(date.getTime());
			Timestamp regionTimeStamp = semanticRegion.getTimeStamp();

			if ((currentTimeStamp.getTime() - regionTimeStamp.getTime()) < CacheManager
					.getInstance().caching_invalidationTimeoutInSeconds * 1000) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param semanticRegion
	 * @return true, falls semantische Region während der aktuellen Session
	 *         angelegt wurde, false sonst
	 */
	private boolean isSessionValid(SemanticRegion semanticRegion) {
		/* Hole IDs semantischer Regionen */
		String sql = "SELECT `SessionId` FROM `SemanticRegions` WHERE `Id`=?";

		PreparedStatement pstmt;

		try {
			pstmt = dbConnection.prepareStatement(sql);

			pstmt.setInt(1, semanticRegion.getId());

			ResultSet rs = pstmt.executeQuery();

			/* Ausführung der Transaktion */
			dbConnection.commit();

			String regionSessionId = null;

			while (rs.next()) {
				regionSessionId = rs.getString("SessionId");
			}

			/* Wenn SessionIds identisch, true zurückliefern */
			if (regionSessionId.equals(this.sessionId)) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public synchronized void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Liest Datenquelle zu gegebener sem. Region aus dem Cache Memory
	 * 
	 * @param region
	 * @return Quelle einer semantischen Region
	 */
	public SDFSource getSourceForRegion(SemanticRegion region) {

		String sql = "SELECT `Source` FROM `SemanticRegions` WHERE `Id`=?";
		SDFSource source = null;
		PreparedStatement pstmt;

		try {
			pstmt = dbConnection.prepareStatement(sql);
			pstmt.setInt(1, region.getId());
			ResultSet rs = pstmt.executeQuery();

			/* Ausführung der Transaktion */
			dbConnection.commit();

			while (rs.next()) {
				source = new SDFSource(rs.getString("Source"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return source;
	}

	/**
	 * Schreibt die aktuelle Constraint Formel der übergebenen semantischen
	 * Region ins Cache Memory
	 * 
	 * @param region
	 */
	public void updateConstraintFormula(SemanticRegion region) {
		try {
			for (SDFSimplePredicate predicate : region.getConstraintFormula()) {
				String sql = "INSERT INTO ConstraintFormulae(ParentId, PredicateName, Operator, Value, Type) VALUES (?,?,?,?,?)";
				PreparedStatement pstmt = dbConnection.prepareStatement(sql);
				pstmt.setInt(1, region.getId());
				pstmt.setString(2, predicate.getAttribute().toString());
				pstmt.setString(3, predicate.getCompareOp().toString());
				pstmt.setString(4, predicate.getValue().getString());
				pstmt.setString(5, predicate.getAttribute().getDatatype()
						.getURI(false));
				pstmt.executeUpdate();
				pstmt.close();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setzt TimeStamp der semantischen Region auf aktuelle Systemzeit.
	 * 
	 * @param region
	 * @param timeStamp
	 */
	public void updateRegionLastAccess(SemanticRegion region, String timeStamp) {
		String sql = "UPDATE `SemanticRegions` SET `LastAccess` = ? WHERE `Id` = ?";
		PreparedStatement pstmt;

		try {
			pstmt = dbConnection.prepareStatement(sql);
			pstmt.setString(1, timeStamp);
			pstmt.setInt(2, region.getId());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Timestamp getRegionTimeStamp(SemanticRegion semanticRegion) {
		String sql = "SELECT `Timestamp` FROM `SemanticRegions` WHERE `Id` = ?";
		PreparedStatement pstmt;
		ResultSet rs;
		Timestamp timeStamp = null;

		try {
			pstmt = dbConnection.prepareStatement(sql);
			pstmt.setInt(1, semanticRegion.getId());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				timeStamp = rs.getTimestamp("Timestamp");
			}
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return timeStamp;
	}
}