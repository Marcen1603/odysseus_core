package de.uniol.inf.is.odysseus.eca.connect;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.eca.plugin.EcaPlugin;
import de.uniol.inf.is.odysseus.eca.plugin.EcaRuleObj;

/**
 * Verbindung zwischen Xtext und Odysseus
 *
 */
public class EcaPluginConnector {
	EcaPlugin plugin = new EcaPlugin();

/**
 *  Uebergibt die notwendigen Daten aus ACL an AclPlugin-Klasse
 */	
	public void makeQuery(ArrayList<EcaRuleObj> ruleSet) {

		// plugin.makeQuery(ruleSet);
		plugin.addQuerySet(ruleSet);
	}

}
