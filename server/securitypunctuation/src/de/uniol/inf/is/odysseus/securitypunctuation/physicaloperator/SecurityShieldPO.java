package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SecurityShieldPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	ArrayList<String> roles = new ArrayList<String>();
	SAOperatorDelegate<T> saOpDel = new SAOperatorDelegate<>();
	boolean match = false;
	boolean SPchecked = false;
	private ISession currentUser = UserManagementProvider.getUsermanagement(true).getSessionManagement()
			.loginSuperUser(null);
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);
	List<ISecurityPunctuation> recentlySentSPs;
	// SDFSchema schema;

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

	public SecurityShieldPO() {
		super();
		addRoles();
		recentlySentSPs = new ArrayList<>();
	}

	public SecurityShieldPO(SecurityShieldPO<T> securityShieldPO) {
		super();
		addRoles();
		recentlySentSPs = new ArrayList<>();
	}

	private void addRoles() {
		for (IRole role : this.currentUser.getUser().getRoles()) {
			this.roles.add(role.getName());
		}

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	public void checkSP() {
		// removes the sps that dont contain any role of the query specifier but
		// have the same TS like other SPs in the cache
		List<ISecurityPunctuation> punctuationsToRemove = new ArrayList<>();
		for (ISecurityPunctuation sp : this.saOpDel.getRecentSPs()) {
			if (!sp.getSRP().getRoles().get(0).equals("*")) {
				if (Collections.disjoint(roles, sp.getSRP().getRoles())) {
					punctuationsToRemove.add(sp);
					LOG.info("Punctuation entfernt:"+sp.toString());
				}
			}

		}
		this.saOpDel.getRecentSPs().removeAll(punctuationsToRemove);
		if (!saOpDel.getRecentSPs().isEmpty()) {
			for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {
				for (String role : roles) {
					for (String roles : sp.getSRP().getRoles()) {
						if ((roles.equals(role) && sp.getSign() == true)
								|| (roles.equals("*") && sp.getSign() == true)) {

							this.match = true;
							return;
						}

						else {
							this.match = false;
							punctuationsToRemove.add(sp);
						}

					}
				}
			}
		}else{
			this.saOpDel.getRecentSPs().add(new SecurityPunctuation("-2,-2", "", "", false, false, -1L));
		}

	}

	/**
	 * If the compatibility of the SP(s) in the buffer and the role of the query
	 * specifier hasn't been checked yet, it will be controlled. Else if the
	 * object is compatible with any of the SPs the object and the SP(s) will be
	 * transferred.
	 */
	@Override
	protected void process_next(T object, int port) {
		if (!SPchecked) {
			checkSP();
			SPchecked = true;
		}

		// if (this.schema == null) {
		// this.schema = this.getInputSchema(port);
		// }

		if (match) {
			if (!saOpDel.getRecentSPs().isEmpty()) {

				for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {
					// könnte sein, dass match bei einem späteren tupel true ist
					if (sp.getDDP().match(object, this.getInputSchema(port))) {

						sendPunctuation(sp);

						recentlySentSPs.add(sp);
					}
				}

			}
			if (!recentlySentSPs.isEmpty()) {
				for (ISecurityPunctuation sp : recentlySentSPs) {
					if (sp.getDDP().match(object, this.getInputSchema(port))) {

						saOpDel.getRecentSPs().removeAll(recentlySentSPs);
						// saOpDel.getRecentSPs().clear();
						transfer(object);
						break;
					}
				}
			}

		}

	}

}
