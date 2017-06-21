package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SecurityShieldPO<M extends ITimeInterval, T extends Tuple<M>> extends AbstractPipe<T, T> {

	// attribute for validation of the tupleRange
	String tupleRangeAttribute;

	// contains the roles of the queryexecutor
	ArrayList<String> roles = new ArrayList<String>();

	ArrayList<ISecurityPunctuation> matchingSPs = new ArrayList<ISecurityPunctuation>();
	
	// contains the currently active SPs
	SAOperatorDelegate<T> saOpDel = new SAOperatorDelegate<>();

	// flag is set to true if the compatibility of the roles in the SP and the
	// roles of the queryexecutor has been checked
	boolean SPchecked = false;

	// flag is set to true, if one of the roles in the SP fits to one of the
	// roles of the queryexecutor
	boolean match = false;

	// flag is set to true, if all currrently active SPs in the saOpDel have
	// been sent
	boolean allSpSent = false;

	// queryexecutor
	private ISession currentUser = UserManagementProvider.getUsermanagement(true).getSessionManagement()
			.loginSuperUser(null);
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);

	// stores the last sent SPs
	List<ISecurityPunctuation> recentlySentSPs;

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
			allSpSent = false;
			recentlySentSPs.clear();
			matchingSPs.clear();
		}
	}

	public SecurityShieldPO(String tupleRangeAttribute) {
		super();
		addRoles();
		recentlySentSPs = new ArrayList<>();
		this.tupleRangeAttribute = tupleRangeAttribute;

	}

	public SecurityShieldPO(SecurityShieldPO<M, T> securityShieldPO) {
		super();
		addRoles();
		recentlySentSPs = new ArrayList<>();
		this.tupleRangeAttribute = securityShieldPO.tupleRangeAttribute;
	}

	// adds the roles of the queryspecifier to a list
	private void addRoles() {
		for (IRole role : this.currentUser.getUser().getRoles()) {
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
		if (!saOpDel.getRecentSPs().isEmpty()) {
			for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {
				if (!checkRole(sp)) {
					spsToRemove.add(sp);
				}else matchingSPs.add(sp);
			}
			// TODO new SP entfernen
			saOpDel.getRecentSPs().removeAll(spsToRemove);
			if (saOpDel.getRecentSPs().isEmpty()) {
				match = false;
				this.saOpDel.getRecentSPs().add(new SecurityPunctuation("-2,-2", "", "", false, false, -1L));
			} else {
				match = true;
			}

		} else {
			this.saOpDel.getRecentSPs().add(new SecurityPunctuation("-2,-2", "", "", false, false, -1L));
		}

	}

	@Override
	protected void process_next(T object, int port) {
		LOG.info(tupleRangeAttribute);

		// check if the SP contains any of the roles of the query specifier
		if (!SPchecked) {
			checkSP();
			SPchecked = true;
		}
		LOG.info("Größr:"+matchingSPs.size());
		this.saOpDel.restrictObject(object, this.getInputSchema(port), matchingSPs,tupleRangeAttribute);
		LOG.info(object.toString());
		//only go on, if an SP fits to a role of the queryexecutor
		if (match) {
			if (!saOpDel.getRecentSPs().isEmpty()) {
				for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {

					if (checkRole(sp) && sp.getDDP().match(object, this.getInputSchema(port), tupleRangeAttribute)) {

						sendPunctuation(sp);
						recentlySentSPs.add(sp);
					}
				}
				saOpDel.getRecentSPs().removeAll(recentlySentSPs);
			}
			if (!recentlySentSPs.isEmpty()) {
				for (ISecurityPunctuation sp : recentlySentSPs) {
					if (sp.getDDP().match(object, this.getInputSchema(port), tupleRangeAttribute)) {
						transfer(object);
						break;
					}
				}
			}

		}

	}

	
	// checks if any role in the SP fits to any of the roles of the
	// queryexecutor
	private boolean checkRole(ISecurityPunctuation sp) {
		for (String role : roles) {
			for (String roles : sp.getSRP().getRoles()) {
				if ((roles.equals(role) && sp.getSign() == true) || (roles.equals("*") && sp.getSign() == true)) {
					return true;
				}
			}
		}
		return false;

	}
}
