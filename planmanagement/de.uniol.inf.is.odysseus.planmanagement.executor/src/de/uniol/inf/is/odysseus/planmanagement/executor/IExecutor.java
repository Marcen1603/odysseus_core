package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.Collection;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

/**
 * IExecutor stellt die Hauptschnittstelle für externe Anwendungen zu Odysseus
 * da. Es werden Funktionen zur Bearbeitung von Anfragen, Steuerung der
 * Ausführung und Konfigurationen zur Verfügung gestellt. Weiterhin bietet diese
 * Schnittstelle diverse Möglichkeit, um Nachrichten über Änderungen innerhalb
 * von Odysseus zu erhalten.
 * 
 * @author wolf
 * 
 */
public interface IExecutor extends IPlanManager, IPlanScheduling,
		IInfoProvider, IErrorEventHandler, IErrorEventListener {
	/**
	 * initialize initialisiert die AUsführungsumgebung. ggf. gehen Anfragen,
	 * Pläne und Einstellungen verloren.
	 * 
	 * @throws ExecutorInitializeException
	 */
	public void initialize() throws ExecutorInitializeException;

	/**
	 * getConfiguration liefert die aktuelle Konfiguration der
	 * AUsführungsumgebung.
	 * 
	 * @return die aktuelle Konfiguration der AUsführungsumgebung
	 */
	public ExecutionConfiguration getConfiguration();

	/**
	 * getSupportedQueryParser liefert alle IDs der zur Verfügung stehenden
	 * Parser zur Übersetzung von Anfragen, die als Zeichenkette vorliegen.
	 * 
	 * @return Liste aller IDs der zur Verfügung stehenden Parser zur
	 *         Übersetzung von Anfragen, die als Zeichenkette vorliegen
	 * @throws PlanManagementException
	 */
	public Set<String> getSupportedQueryParser()
			throws PlanManagementException;

	/**
	 * addQuery fügt Odysseus eine Anfrage hinzu, die als logischer Plan
	 * vorliegt. Es kann sein, dass die Anfrage nicht direkt der Auführung
	 * hinzugefügt wird (bspw. bei interner asychronen Optimierung). Die
	 * zurückgebegen ID ist daher nur vorläufig. Erst beim Empfangen des
	 * Hinzufügen-Events kann davon ausgegangen werden, dass die Anfrage
	 * hinzugefügt wurde.
	 * 
	 * @param logicalPlan
	 *            logischer Plan der Anfrage
	 * @param parameters
	 *            Parameter zum Bearbeiten, Erstellen und Verändern der Anfrage
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public int addQuery(ILogicalOperator logicalPlan,
			AbstractQueryBuildParameter<?>... parameters)
			throws PlanManagementException;

	/**
	 * addQuery fügt Odysseus eine Anfrage hinzu, die als Zeichenkette vorliegt.
	 * Es kann sein, dass die Anfrage nicht direkt der Auführung hinzugefügt
	 * wird (bspw. bei interner asychronen Optimierung). Die zurückgebegen ID
	 * ist daher nur vorläufig. Erst beim Empfangen des Hinzufügen-Events kann
	 * davon ausgegangen werden, dass die Anfrage hinzugefügt wurde.
	 * 
	 * @param query
	 *            Anfrage in Form einer Zeichenkette
	 * @param compilerID
	 *            ID des zu verwendenden Parsers
	 * @param parameters
	 *            Parameter zum Bearbeiten, Erstellen und Verändern der Anfrage
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public Collection<Integer> addQuery(String query, String compilerID,
			AbstractQueryBuildParameter<?>... parameters)
			throws PlanManagementException;

	/**
	 * addQuery fügt Odysseus eine Anfrage hinzu, die als physischer Plan
	 * vorliegt. Es kann sein, dass die Anfrage nicht direkt der Auführung
	 * hinzugefügt wird (bspw. bei interner asychronen Optimierung). Die
	 * zurückgebegen ID ist daher nur vorläufig. Erst beim Empfangen des
	 * Hinzufügen-Events kann davon ausgegangen werden, dass die Anfrage
	 * hinzugefügt wurde.
	 * 
	 * @param physicalPlan
	 *            physischer Plan der neuen Anfrage
	 * @param parameters
	 *            Parameter zum Bearbeiten, Erstellen und Verändern der Anfrage
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public int addQuery(IPhysicalOperator physicalPlan,
			AbstractQueryBuildParameter<?>... parameters)
			throws PlanManagementException;
}
