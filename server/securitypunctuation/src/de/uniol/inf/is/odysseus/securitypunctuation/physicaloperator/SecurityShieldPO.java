package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;

public class SecurityShieldPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	ArrayList<String> roles = new ArrayList<String>();
	SAOperatorDelegatePO<T> saOpDelPO = new SAOperatorDelegatePO<>();
	boolean match = false;
	boolean SPchecked = false;
	private ISession currentUser = UserManagementProvider.getUsermanagement(true).getSessionManagement()
			.loginSuperUser(null);
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);


	/***
	 * if a new SP arrives SPchecked is set to false. When a new Tuple arrives
	 * the compatibility of the SP in the buffer and the role of the Query
	 * Specifier will be checked again
	 */
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof AbstractSecurityPunctuation) {
			saOpDelPO.override((AbstractSecurityPunctuation) punctuation);
			SPchecked = false;
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

	/*
	 * eventuell Roles in SRP auf Typ von IRole ändern, dann kann diese Methode
	 * vereinfacht werden und equals verwendet werden
	 */

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
		for (AbstractSecurityPunctuation sp : saOpDelPO.getRecentSPs()) {
			for (String role : roles) {
				for (String roles : sp.getSRP().getRoles()) {
					if (roles.equals(role) && sp.getSign() == true) {
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
		 * If the compatibility of the SP(s) in the buffer and the role of the
		 * query specifier hasn't been checked yet, it will be controlled. Else
		 * if the is compatible with any of the SPs the object and the SP(s)
		 * will be transferred.
		 */
	@Override
	protected void process_next(T object, int port) {
		
		if (!SPchecked) {
			checkSP();
			SPchecked = true;
		}
		

		// macht es sinn die SPs weiterzuschicken, wenn keine Tupel versendet
		// werden (für spätere Operatoren)?
		if (match)

		{
			if (!saOpDelPO.getRecentSPs().isEmpty()) {
				for (AbstractSecurityPunctuation sp : saOpDelPO.getRecentSPs()) {
					sendPunctuation(sp);
				}
				saOpDelPO.recentSPs.clear();
			}
			transfer(object);
		}

	}

}
