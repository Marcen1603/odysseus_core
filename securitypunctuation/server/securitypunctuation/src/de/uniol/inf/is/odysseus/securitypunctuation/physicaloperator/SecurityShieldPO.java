package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;

public class SecurityShieldPO<M extends ITimeInterval, T extends Tuple<M>> extends AbstractPipe<T, T> {
	// attribute for validation of the tupleRange
	private String tupleRangeAttribute;

	// contains the roles of the queryexecutor
	private ArrayList<String> roles = new ArrayList<String>();


	// contains the currently active SPs
	private SAOperatorDelegate<T> saOpDel = new SAOperatorDelegate<>();

	// flag is set to true if the compatibility of the roles in the SP and the
	// roles of the queryexecutor has been checked
	private boolean SPchecked = false;

	// flag is set to true, if one of the roles in the SP fits to one of the
	// roles of the queryexecutor
	private boolean match = false;

	// stores the last sent SPs
	private List<ISecurityPunctuation> recentlySentSPs;

	/*
	 * When a new SP arrives SPchecked is set to false. When a new Tuple arrives
	 * the compatibility of the SP in the buffer and the role of the Query
	 * Specifier will be checked again
	 */
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof ISecurityPunctuation) {
			saOpDel.override((ISecurityPunctuation) punctuation);
			SPchecked = false;
			recentlySentSPs.clear();
		
		}
	}

	public SecurityShieldPO(String tupleRangeAttribute, List<? extends IRole> roles) {
		super();
		addRoles(roles);
		recentlySentSPs = new ArrayList<>();
		this.tupleRangeAttribute = tupleRangeAttribute;

	}

	public SecurityShieldPO(SecurityShieldPO<M, T> securityShieldPO) {
		super();
		this.roles=securityShieldPO.getRoles();
		recentlySentSPs = new ArrayList<>();
		this.tupleRangeAttribute = securityShieldPO.tupleRangeAttribute;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	// adds the roles of the queryspecifier to a list
	private void addRoles(List<? extends IRole> roles) {
		for (IRole role : roles) {
			this.roles.add(role.getName());
		}

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	// checks the compatibility of the currently active SPs with the roles of
	// the queryexecutor
	public void checkSP() {
		ArrayList<ISecurityPunctuation> spsToRemove = new ArrayList<>();
		match=false;
		if (!saOpDel.getRecentSPs().isEmpty()) {
			for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {
				if (sp.getSign() == false || !sp.checkRole(roles)) {
					spsToRemove.add(sp);
				}
			}
			saOpDel.getRecentSPs().removeAll(spsToRemove);
			if (saOpDel.getRecentSPs().isEmpty()) {
				match = false;

			} else {
				match = true;
			}

		}
		

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		//object has to be cloned, to perform restriction on it
		T clone=(T) object.clone();
		// check if the SP contains any of the roles of the queryexecutor
		if (!SPchecked) {
			checkSP();
			SPchecked = true;
		}

		// only go on, if a SP fits to a role of the queryexecutor
		if (match) {
			if (!saOpDel.getRecentSPs().isEmpty()) {
				for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {

					if (sp.checkRole(roles)
							&& sp.getDDP().match(clone, this.getInputSchema(port), tupleRangeAttribute)) {

						sendPunctuation(sp);
						recentlySentSPs.add(sp);
					}
				}
				saOpDel.getRecentSPs().removeAll(recentlySentSPs);
			}
			if (!recentlySentSPs.isEmpty()) {
				for (ISecurityPunctuation sp : recentlySentSPs) {
					if (sp.getDDP().match(clone, this.getInputSchema(port), tupleRangeAttribute)) {
						//sets the attributes the queryexecutor has no access to to null
						sp.restrictObject(clone, this.getInputSchema(port), recentlySentSPs, tupleRangeAttribute);
						transfer(clone);
						break;
					}
				}
			}

		}

	}

}
