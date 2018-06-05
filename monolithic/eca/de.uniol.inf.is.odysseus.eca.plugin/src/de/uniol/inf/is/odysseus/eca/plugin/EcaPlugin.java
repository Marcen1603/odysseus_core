package de.uniol.inf.is.odysseus.eca.plugin;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.OSGI;

/**
 * Admission Control Main Wird von EcaPluginEventBinder.class initialisiert
 * 
 */
public class EcaPlugin {

	private static ISession currentSession;
	private static IServerExecutor executor;
	private static final Logger LOG = LoggerFactory.getLogger(EcaPlugin.class);
	// private static String finalName;
	private static Context context;

	/**
	 * Konstruktor
	 */
	public EcaPlugin() {
		UserManagementProvider userManagementProvider = OSGI.get(UserManagementProvider.class);
		if (currentSession == null || !currentSession.isValid()) {
			currentSession = SessionManagement.instance.loginSuperUser(null, userManagementProvider.getDefaultTenant().getName());
		}
		context = new Context();
		executor = EcaPluginEventBinder.getExecutor();
	}

	/**
	 * Empfaengt die Regeln von xText
	 */
	public void addQuerySet(ArrayList<EcaRuleObj> ruleSet) {

		String tmp = EcaPluginImpl.buildQueries(ruleSet);
		if (context == null && EcaParser.getContext() != null) {
			context = EcaParser.getContext();
			LOG.info("Context needed..");
		} else if (context != null) {
			regSources(ruleSet.get(0));
			executor.addQuery("#PARSER PQL\n #QNAME ac_" + ruleSet.get(0).getRuleName() + "\n" + "#ACQUERY \n" + tmp,
					"OdysseusScript", currentSession, Context.empty());
		}
	}

	/**
	 * Sources zu timer, planmodification und systemload werden angelegt, um
	 * dies nicht immer wieder manuell machen zu m�ssen.
	 */
	private void regSources(EcaRuleObj rule) {
		int timer = 0;
		if ((!executor.containsViewOrStream("eca_planmodification", currentSession))) {

			LOG.info("Input source eca_planmodification registered.");
			executor.addQuery(
					"eca_planmodification := ACCESS({source='PlanModificationWatcher',wrapper='GenericPush',transport='planmodificationwatcher',datahandler='Tuple'})",
					"PQL", currentSession, null);
		}

		if ((!executor.containsViewOrStream("eca_time", currentSession))) {
			if (!(rule.getTimerIntervall() < 1)) {
				timer = rule.getTimerIntervall();
			} else {
				timer = 2000;
			}
			LOG.info("Input source eca_timer registered: " + timer);
			executor.addQuery("eca_time :=  TIMER({period =" + timer + ", timefromstart = true, source = 'source'})",
					"PQL", currentSession, null);
		}
	}
}