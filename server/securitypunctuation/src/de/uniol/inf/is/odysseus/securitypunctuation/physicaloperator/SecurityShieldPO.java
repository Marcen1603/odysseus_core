package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
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
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;

public class SecurityShieldPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	ArrayList<String> roles = new ArrayList<String>();
	SAOperatorDelegate<T> saOpDel = new SAOperatorDelegate<>();
	boolean match = false;
	boolean SPchecked = false;
	private ISession currentUser = UserManagementProvider.getUsermanagement(true).getSessionManagement()
			.loginSuperUser(null);
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);
	List<AbstractSecurityPunctuation> recentlySentSPs;
//	SDFSchema schema;

	/*
	 * When a new SP arrives SPchecked is set to false. When a new Tuple arrives
	 * the compatibility of the SP in the buffer and the role of the Query
	 * Specifier will be checked again
	 */
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof AbstractSecurityPunctuation) {
			saOpDel.override((AbstractSecurityPunctuation) punctuation);
			SPchecked = false;
			recentlySentSPs = new ArrayList<>();
		}
	}

	public SecurityShieldPO() {
		super();
		addRoles();

	}

	public SecurityShieldPO(SecurityShieldPO<T> securityShieldPO) {
		super();
		addRoles();
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
		for (AbstractSecurityPunctuation sp : saOpDel.getRecentSPs()) {
			for (String role : roles) {
				for (String roles : sp.getSRP().getRoles()) {
					if ((roles.equals(role) && sp.getSign() == true) || (roles.equals("*") && sp.getSign() == true)) {

						this.match = true;
						return;
					}

					else {
						this.match = false;
					}

				}
			}
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
//		if (this.schema == null) {
//			this.schema = this.getInputSchema(port);
//		}


		if (match) {
			if (!saOpDel.getRecentSPs().isEmpty()) {
				
				for (AbstractSecurityPunctuation sp : saOpDel.getRecentSPs()) {
					//könnte sein, dass match bei einem späteren tupel true ist
					if (sp.getDDP().match(object, this.getInputSchema(port))) {

						sendPunctuation(sp);

						recentlySentSPs.add(sp);
					}
				}

			}
			if (!recentlySentSPs.isEmpty()) {
				for (AbstractSecurityPunctuation sp : recentlySentSPs) {
					if (sp.getDDP().match(object, this.getInputSchema(port))) {
						//removeall ist wwahrscheinlcih besser
					//	saOpDel.getRecentSPs().removeAll(recentlySentSPs);
						saOpDel.getRecentSPs().clear();
						transfer(object);
						break;
					}
				}
			}

		}

	}

}
