/**
 * This package contains plan operators used for caching
 */
package mg.dynaquest.queryexecution.po.caching;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.caching.CacheManager;
import mg.dynaquest.queryexecution.caching.StorageManager;
import mg.dynaquest.queryexecution.caching.filter.ConstraintFormulaFilter;
import mg.dynaquest.queryexecution.caching.memory.ConstraintFormula;
import mg.dynaquest.queryexecution.caching.memory.DataTuple;
import mg.dynaquest.queryexecution.caching.memory.SemanticRegion;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.wrapper.WrapperPlanFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

/**
 * @author Tobias Hesselmann
 * 
 * Diese Klasse implementiert einen Caching Planoperator im DynaQuest-Format
 */
public class CachingPO extends UnaryPlanOperator {

	/*
	 * TODO: - Konfiguration in Dynaquest File auslagern
	 */

	/**
	 * Liste mit Prädikaten aus der Query
	 */

	@SuppressWarnings("unused")
	private CacheManager cacheManager = null;

	private StorageManager storageManager = null;

	private ArrayList<DataTuple> remainderQuery = null;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * verweist auf den zugehörigen AccessPO
	 */
	private SchemaTransformationAccessPO algebraPO;

	private ArrayList<Integer> probeQueryTupleIds = new ArrayList<Integer>();

	private int probeQueryTupleCounter = -1;

	private HashSet<SemanticRegion> matchingRegions = new HashSet<SemanticRegion>();

	private ConstraintFormulaFilter remainderFilter;

	private ArrayList<DataTuple> probeQuery;

	public CachingPO() {
		super();
	}

	/**
	 * Konstruktor für die Integration anstelle eines Wrappers
	 * 
	 * @param schemaTransformationAccessPO
	 */
	public CachingPO(SchemaTransformationAccessPO schemaTransformationAccessPO) {
		super(schemaTransformationAccessPO);
		this.algebraPO = schemaTransformationAccessPO;
		
		// Wrapper wird erzeugt ...
		PlanOperator wrapPlan = WrapperPlanFactory
				.getAccessPlan(schemaTransformationAccessPO);
		if (wrapPlan == null) {
			logger
					.error("Keinen passenden Wrapper gefunden. Quelle "
							+ schemaTransformationAccessPO.getSource()
							+ " verwendbar?");
		}

		/* AccessPO zu diesem PO verbinden */
		this.setInputPO(wrapPlan);

	}

	/**
	 * Konstruktor für das Clonen des CachingPO
	 * 
	 * @param cachingPO
	 */
	public CachingPO(CachingPO cachingPO) {
		super(cachingPO);
		this.algebraPO = cachingPO.algebraPO;

		// Wrapper wird erzeugt ...
		PlanOperator wrapPlan = WrapperPlanFactory
				.getAccessPlan(this.algebraPO);
		if (wrapPlan == null) {
			logger.error("Keinen passenden Wrapper gefunden. Quelle "
					+ algebraPO.getSource() + " verwendbar?");
		}

		/* AccessPO zu diesem PO verbinden */
		this.setInputPO(wrapPlan);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#getInternalPOName()
	 */
	@Override
	public String getInternalPOName() {
		return "CachingPO";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#getInternalXMLRepresentation(java.lang.String,
	 *      java.lang.String, java.lang.StringBuffer)
	 */
	@Override
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#initInternalBaseValues(org.w3c.dom.NodeList)
	 */
	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_open()
	 */
	@Override
	protected boolean process_open() throws POException {

		/* Hole Instanzen des Cache- und Storage-Managers */
		this.cacheManager = CacheManager.getInstance();
		this.storageManager = StorageManager.getInstance();

		// /* Resettet Cache Memory (Zu Testzwecken) */
		// try {
		// storageManager.flush();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }

		/* Session ID setzen */
		storageManager.setSessionId(algebraPO.getSessionId());
		
		/* Semantische Regionen invalidieren */
		storageManager.invalidateSemanticRegions();

		/* ProbeQuery generieren */
		try {
			getProbeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/* Überprüft auf Containment */
		ArrayList<SDFSimplePredicate> predicates;
		try {
			predicates = this.getRemainderQuery(algebraPO.getQueryPredicates(),
					matchingRegions);

			/*
			 * Wenn predicate nicht null ist, so handelt es sich um Disjunktheit
			 * oder Schnitt
			 */
			if (predicates != null) {
				if (!matchingRegions.isEmpty()) {
						/*
						 * Gibt es passende Regionen, so handelt es sich um
						 * Schnitt und es wird der Remainder Filter gesetzt
						 */
						/*
						 * Liste mit ConstraintFormeln aller passenden
						 * semantischen Regionen erstellen
						 */
						ArrayList<ConstraintFormula> constraintFormulae = new ArrayList<ConstraintFormula>();
						for (SemanticRegion sr : matchingRegions) {
							constraintFormulae.add(sr.getConstraintFormula());
						}

						this.remainderFilter = new ConstraintFormulaFilter(
								constraintFormulae, algebraPO.getOutElements());
					}

			} else {
				/*
				 * Remainder Query ist leer. Terminiere Input Operator.
				 */
				terminateAccessPO();
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		/* Initialisiere Remainder Query Liste */
		this.remainderQuery = new ArrayList<DataTuple>();
		this.probeQuery = new ArrayList<DataTuple>();

		return true;
	}

	/**
	 * Stoppt AccessPO, schließt ihn und setzt Anzahl der Inputs auf 0
	 * 
	 * @throws POException
	 */
	private void terminateAccessPO() throws POException {
		this.stopChildren();
		this.getInputPO().close(this);
		this.setNoOfInputs(0);
	}

	/**
	 * Generiert die Probe Query zu der aktuellen Anfrage
	 * 
	 * @throws SQLException
	 */
	private void getProbeQuery() throws SQLException {

		logger.info("Extracted Query Predicates: "
				+ this.algebraPO.getQueryPredicates());
		logger.info("Extracted Query Attributes: " + this.getOutElements());

		/*
		 * Finde semantische Regionen, welche Inhalte zu einer gegebenen Quelle
		 * liefern
		 */
		HashSet<SemanticRegion> sourceMatchingRegions = StorageManager
				.getInstance().getMatchingRegionsForSource(
						algebraPO.getSource());

		HashSet<Integer> matchingTupleIds = null;

		/*
		 * Nun überprüfe die in Frage kommenden Regionen daraufhin, ob sie
		 * passende Tupel enthalten
		 */
		if (sourceMatchingRegions.size() != 0) {
			matchingTupleIds = StorageManager.getInstance()
					.getMatchingTupleIds(algebraPO.getQueryPredicates(),
							sourceMatchingRegions);
		}

		if (matchingTupleIds != null) {
			this.matchingRegions = StorageManager.getInstance()
					.getSemanticRegions(matchingTupleIds);

			logger.info("Gefundene passende Regionen: "
					+ matchingRegions.size());

			for (int i : matchingTupleIds) {
				this.probeQueryTupleIds.add(i);
			}
		}

		logger.info("ProbeQuery umfasst " + probeQueryTupleIds.size()
				+ " Einträge");

	}

	/**
	 * Erzeugt die Remainder Query zu einer Anfrage.
	 * 
	 * @param queryPredicates
	 *            Prädikate der Anfrage
	 * @param semanticRegions
	 *            Liste von semantischen Regionen, die zu der Anfrage beitragen
	 *            können (muss vorher ermittelt werden)
	 * @return Remainder Query als Liste von SimplePredicates
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 * @throws CloneNotSupportedException
	 */
	private ArrayList<SDFSimplePredicate> getRemainderQuery(
			ArrayList<SDFSimplePredicate> queryPredicates,
			HashSet<SemanticRegion> semanticRegions)
			throws IllegalArgumentException, SQLException,
			CloneNotSupportedException {

		/*
		 * Fall 1: Semantische Region ist Spezialisierung (Erweiterung) der
		 * Query. Dies bedeutet, eine semantische Region enthält alle Prädikate,
		 * die in der Query vorkommen und mehr. => Gehe jedes der Prädikate der
		 * Constraint Formel durch und überprüfe, ob es in der Query vorkommt.
		 * Falls nein, füge es negiert der Remainder Query hinzu und füge die
		 * Query der Remainder Query hinzu.
		 * 
		 * Fall 2: Eine semantische Region ist Generalisierung der Query. D.h,
		 * Query ist komplett in semantischer Region enthalten. Die Query
		 * enthält also alle Prädikate, die in semantischer Region vorkommen und
		 * mehr. => Remainder Query ist null, es sind keine weiteren Abfragen
		 * nötig, da alles im Cache enthalten ist
		 * 
		 * Fall 3: Semantische Region und Query sind semantisch unabhängig.
		 * 
		 */

		ArrayList<SDFSimplePredicate> remainder = new ArrayList<SDFSimplePredicate>();

		/*
		 * Durch alle passenden SR iterieren und für jede Constraint-Formel den
		 * Remainder extrahieren. Diesen, falls nicht null, der Remainder Query
		 * hinzufügen.
		 */
		if (semanticRegions != null) {
			if (!semanticRegions.isEmpty()) {
				for (SemanticRegion sr : semanticRegions) {
					if (sr.getSource().equals(algebraPO.getSource())) {
						ArrayList<SDFSimplePredicate> remainderPredicates = sr
								.getConstraintFormula().getRemainder(
										queryPredicates);
						if (remainderPredicates != null) {
							remainder.addAll(sr.getConstraintFormula()
									.getRemainder(queryPredicates));
						} else {
							/*
							 * Wenn der Remainder einer semantischen Region null
							 * ist, so ist auch die Remainder Query null.
							 */
							return null;
						}
					}
				}
			}
		}

		/*
		 * Falls es keine totales Containment gibt, wird die Query hier dem
		 * Remainder hinzugefügt. Dies muss im Fall passieren, dass es keine
		 * matchenden Regionen gibt (z.B. weil die Quelle sich unterscheidet)
		 * oder wenn es eine semantische Überlappung gibt
		 */
		if (remainder.size() == 0) {
			remainder.addAll(queryPredicates);
		}

		return remainder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_next()
	 */
	@Override
	protected Object process_next() throws POException, TimeoutException {

		probeQueryTupleCounter++;

		/* Wenn noch Einträge in der Probe Query sind, liefere diese zurück */
		if (probeQueryTupleIds.size() > probeQueryTupleCounter) {
			return getNextProbeQueryTuple();
		} else {
			/*
			 * Wenn kein total Containment vorliegt, Tupel aus QuellSystem
			 * extrahieren
			 */
			if (this.getNumberOfInputs() > 0) {
				return getNextRemainderQueryTuple();
			} else {
				/*
				 * Sonst ist Extraktionsprozess abgeschlossen. Dann null
				 * zurückliefern
				 */
				return null;
			}
		}

	}

	/**
	 * 
	 * @return nächstes Tuple aus der Remainder Query oder NULL, falls keines
	 *         mehr vorhanden ist
	 * @throws POException
	 * @throws TimeoutException
	 */
	private RelationalTuple getNextRemainderQueryTuple() throws POException,
			TimeoutException {
		logger.info("Cache Miss!");

		/* Tupel initialisieren */
		RelationalTuple tuple = new RelationalTuple(0);

		/*
		 * Ein Tupel aus der Quelle extrahieren
		 */
		tuple = (RelationalTuple) this.getInputNext(this, -1);

		if (this.remainderFilter != null) {
			while (!remainderFilter.doesTupelPass(tuple) && tuple != null) {
				tuple = (RelationalTuple) this.getInputNext(this, -1);
			}
		}

		/*
		 * Speichere Remainder Query temporär in Liste extrahierter Inhalte
		 */

		if (tuple != null) {

			/* Hole nächste freie Tupel ID vom Storage Manager */
			int tupleId = storageManager.getNextTupleId();

			for (int i = 0; i < this.getOutElements().size(); i++) {
				String key = this.getOutElements().get(i).getURI(false);
				String value = tuple.getAttribute(i);
				String datatype = this.getOutElements().get(i).getDatatype()
						.getURI(false);
				DataTuple dataTuple = new DataTuple(tupleId, key, value,
						datatype);
				this.remainderQuery.add(dataTuple);
				logger.debug("Added Attribute to Remainder Query: Tuple ID: "
						+ tupleId + "     Attribute: " + key + "    Value: "
						+ tuple.getAttribute(i));
			}
		}

		return tuple;
	}

	private RelationalTuple getNextProbeQueryTuple() {
		logger.info("Cache Hit!");

		RelationalTuple relTuple = storageManager.getProbeQueryTuple(
				probeQueryTupleIds.get(probeQueryTupleCounter), this
						.getOutElements());
		/* Hole nächste freie Tupel ID vom Storage Manager */
		int tupleId = storageManager.getNextTupleId();

		for (int i = 0; i < this.getOutElements().size(); i++) {
			String key = this.getOutElements().get(i).getURI(false);
			String value = relTuple.getAttribute(i);
			String datatype = this.getOutElements().get(i).getDatatype()
					.getURI(false);
			DataTuple dataTuple = new DataTuple(tupleId, key, value, datatype);
			this.probeQuery.add(dataTuple);
		}
		return relTuple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_close()
	 */
	@Override
	protected boolean process_close() throws POException {

		/*
		 * Regionen, die zu der Query beitragen konnten, werden im Folgenden
		 * gelöscht. Dies ist nötig, um semantische Regionen und Anfragen mit
		 * disjunkten Prädikaten zu vermeiden. Tupel, die in der Probe Query
		 * enthalten sind, leben aber in der neu entstehenden Region weiter.
		 * 
		 * Ausschlusskriterien: Falls Containment oder semantische Disjunktheit
		 * vorliegt, können die semantischen Regionen unverändert bleiben.
		 */
		if (!remainderQuery.isEmpty() && !matchingRegions.isEmpty()) {
			for (SemanticRegion sr : matchingRegions) {
				sr.delete();
			}
		}

		if (!remainderQuery.isEmpty()) {

			/* Speichere extrahierte Inhalte in semantischer Region */

			logger
					.info("Lege neue semantische Region an und speichere Inhalte");

			/* Remainder- und ProbeQuery konkatenieren */
			ArrayList<DataTuple> dataTuples = new ArrayList<DataTuple>();
			dataTuples.addAll(this.probeQuery);
			dataTuples.addAll(this.remainderQuery);

			/* Semantische Region anlegen */
			SemanticRegion semanticRegion = new SemanticRegion(
					new ConstraintFormula(algebraPO.getQueryPredicates()),
					this.algebraPO.getSource(), dataTuples);

			/* Semantische Region in Datenbank speichern */
			try {
				semanticRegion.save();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			/*
			 * Remainder Query ist leer und es konnten Regionen zur Anfrage
			 * beitragen. Es muss sich demnach um semantisches Containment
			 * handeln. Dann Timestamps aller beteiligten semantischen Regionen
			 * updaten.
			 */
			if (!matchingRegions.isEmpty()) {
				/* Timestamp kalkulieren */
				Date date = new Date();
				Timestamp tstamp = new Timestamp(date.getTime());
				String timeStamp = tstamp.toString();

				for (SemanticRegion sr : matchingRegions) {
					sr.updateLastAccess(timeStamp);
				}
			}
		}
		this.stop(); /* ggf. später entfernen */
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe#cloneMe()
	 */
	public SupportsCloneMe cloneMe() {
		return new CachingPO(this);
	}

}
