package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;

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
public interface IExecutor extends IOptimizable, IPlanManager, IPlanScheduling,
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
	public int addQuery(ILogicalOperator logicalPlan, User user,
			AbstractQueryBuildParameter<?>... parameters)
			throws PlanManagementException;

	
	
	/**
	 * addQuery f�gt Odysseus eine Anfrage hinzu, die bereits als Query
	 * vorliegt. Es kann sein, dass die Anfrage nicht direkt der Auf�hrung
	 * hinzugef�gt wird (bspw. bei interner asychronen Optimierung). Die
	 * zur�ckgebegen ID ist daher nur vorl�ufig. Erst beim Empfangen des
	 * Hinzuf�en-Events kann davon ausgegangen werden, dass die Anfrage
	 * hinzugef�gt wurde.
	 * 
	 * @param query die query
	 * @return vorl�ufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */	
	public int addQuery(Query query) throws PlanManagementException;
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
	public Collection<Integer> addQuery(String query, String compilerID, User user,
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
	 * @param doRestruct
	 *            If true, the query plan will be restructured, if false, it will not.
	 * @param rulesToUse
	 *            Contains the names of the rules to be used for restructuring. Other
	 *            rules will not be used.
	 * @param parameters
	 *            Parameter zum Bearbeiten, Erstellen und Verändern der Anfrage
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public Collection<Integer> addQuery(String query, String parserID, User user,
			boolean doRestruct,
			Set<String> rulesToUse,
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
	public int addQuery(List<IPhysicalOperator> physicalPlan, User user,
			AbstractQueryBuildParameter<?>... parameters)
			throws PlanManagementException;
	
	public void addCompilerListener(ICompilerListener compilerListener);


}
