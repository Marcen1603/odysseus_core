package de.uniol.inf.is.odysseus.admission.console;

import java.util.Collection;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.action.AdmissionActionComponentRegistry;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;
import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusComponentRegistry;

public class AdmissionConsole implements CommandProvider {

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("--- Admission commands---\n");
		sb.append("    lsAdmissionRules/listAdmissionRules         					- Lists all registered admission rules\n");
		sb.append("    lsAdmissionStatusComponents/listAdmissionStatusComponents    - Lists all registered admission status components\n");
		sb.append("    lsAdmissionActionComponents/listAdmissionActionComponents    - Lists all registered admission action components\n");
		
		return sb.toString();
	}

	public void _lsAdmissionRules(CommandInterpreter ci) {
		
		for( AdmissionRuleGroup group : AdmissionRuleGroup.values() ) {
			ci.println("AdmissionRuleGroup: " + group.toString());
			
			Collection<Class<? extends IAdmissionEvent>> eventTypesOfGroup = AdmissionPlugIn.getAdmissionRuleRegistry().getAdmissionEventTypes(group);
			if( !eventTypesOfGroup.isEmpty() ) {
				for( Class<? extends IAdmissionEvent> eventType : eventTypesOfGroup ) {
					ci.println("\t" + eventType.getName());
					
					List<? extends IAdmissionRule<?>> rules = AdmissionPlugIn.getAdmissionRuleRegistry().getAdmissionRules(group, eventType);
					for( IAdmissionRule<?> rule : rules ) {
						ci.println("\t\t" + rule.getClass().getName());
					}
				}
			} else {
				ci.println("\t<none>");
			}
			
			ci.println();
		}
	}
	
	public void _listAdmissionRules(CommandInterpreter ci ) {
		_lsAdmissionRules(ci);
	}
	
	public void _lsAdmissionStatusComponents( CommandInterpreter ci ) {
		AdmissionStatusComponentRegistry registry = AdmissionPlugIn.getAdmissionStatusComponentRegistry();
		Collection<Class<? extends IAdmissionStatusComponent>> components = registry.getAllAdmissionStatusComponentClasses();
		
		if( !components.isEmpty() ) {
			for( Class<? extends IAdmissionStatusComponent> component : components ) {
				System.out.println("\t" + component.getName());
			}
		} else {
			System.out.println("\t<none>");
		}
	}
	
	public void _listAdmissionStatusComponents( CommandInterpreter ci ) {
		_lsAdmissionStatusComponents(ci);
	}
	
	public void _lsAdmissionActionComponents( CommandInterpreter ci ) {
		AdmissionActionComponentRegistry registry = AdmissionPlugIn.getAdmissionActionComponentRegistry();
		
		Collection<Class<? extends IAdmissionActionComponent>> components = registry.getAllAdmissionActionComponentClasses();
		if( !components.isEmpty() ) {
			for( Class<? extends IAdmissionActionComponent> component : components ) {
				ci.println("\t" + component.getName());
			}
		} else {
			ci.println("\t<none>");
		}
	}
	
	public void _listAdmissionActionComponents( CommandInterpreter ci ) {
		_lsAdmissionActionComponents(ci);
	}
}
