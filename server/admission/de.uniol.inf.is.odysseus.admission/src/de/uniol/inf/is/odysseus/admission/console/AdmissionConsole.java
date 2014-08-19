package de.uniol.inf.is.odysseus.admission.console;

import java.util.Collection;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;

public class AdmissionConsole implements CommandProvider {

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("--- Admission commands---\n");
		sb.append("    lsAdmissionRules/listAdmissionRules         		- Lists all registered admission rules\n");
		
		return sb.toString();
	}

	public void _lsAdmissionRules(CommandInterpreter ci) {
		
		for( AdmissionRuleGroup group : AdmissionRuleGroup.values() ) {
			System.out.println("AdmissionRuleGroup: " + group.toString());
			
			Collection<Class<? extends IAdmissionEvent>> eventTypesOfGroup = AdmissionPlugIn.getAdmissionRuleRegistry().getAdmissionEventTypes(group);
			if( !eventTypesOfGroup.isEmpty() ) {
				for( Class<? extends IAdmissionEvent> eventType : eventTypesOfGroup ) {
					System.out.println("\t" + eventType.getName());
					
					List<? extends IAdmissionRule<?>> rules = AdmissionPlugIn.getAdmissionRuleRegistry().getAdmissionRules(group, eventType);
					for( IAdmissionRule<?> rule : rules ) {
						System.out.println("\t\t" + rule.getClass().getName());
					}
				}
			} else {
				System.out.println("\t<none>");
			}
			
			System.out.println();
		}
	}
	
	public void _listAdmissionRules(CommandInterpreter ci ) {
		_lsAdmissionRules(ci);
	}
}
