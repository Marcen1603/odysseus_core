package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryAction;

public class UserManagement extends AbstractUserManagement {

	static private UserManagement instance = null;

	public UserManagement(IUserStore userStore) {
		super(userStore);
	}

	public synchronized static UserManagement getInstance() {
		if (instance == null) {

			try {
				// instance = new UserManagement(new MemoryUserStore());
				instance = new UserManagement(new FileUserStore(
						System.getProperty("user.home")
								+ "/odysseus/users.store"));

				if (instance.hasNoUsers()) {
					// create system user
					instance.registerUserInt("System", "manager");
				}

				if (instance.hasNoRoles()) {
					// create admin Role
					Role adminrole = new Role("admin", UserManagement
							.getInstance().getRoleID());

					// create permissions for admin
					List<IUserAction> adminops = new ArrayList<IUserAction>();
					adminops.addAll(UserManagementAction.getAll());

					// create privilege for admin (kann nicht über create, da
					// noch keine Rechte vorhanden
					Privilege adminpriv = new Privilege("UserManagement",
							adminrole, adminops, instance.getPrivID());

					// add privilege for admin
					adminrole.addPrivilege(adminpriv);

					// add admin role to system
					instance.login("System", "manager").addRole(adminrole);

					// ------------------------------------------------
					// create DataDictoinary Role
					Role ddrole = new Role("datadictionary", UserManagement
							.getInstance().getRoleID());
					// create permission for admin
					List<IUserAction> admindatadic = new ArrayList<IUserAction>();
					admindatadic.addAll(DataDictionaryAction.getAll());
					// create DataDic Priv and add to Role
					ddrole.addPrivilege(instance.createPrivilege(
							"DataDictionary", ddrole, admindatadic,
							instance.login("System", "manager")));
					// add to system
					instance.login("System", "manager").addRole(ddrole);
				}

			} catch (UsernameAlreadyUsedException e) {
			} catch (UserStoreException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public User getSuperUser() {
		return login("System", "manager");
	}

}
