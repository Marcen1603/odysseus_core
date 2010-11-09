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
				instance = new UserManagement(new MemoryUserStore());
				// instance = new UserManagement(new FileUserStore(
				// System.getProperty("user.home")
				// + "/odysseus/users.store"));

				if (instance.hasNoUsers()) {
					// create system user
					instance.registerUserInt("System", "manager");
				}

				if (instance.hasNoRoles()) {
					User sys = instance.login("System", "manager");

					// create admin Role
					Role adminrole = new Role("sys_admin", UserManagement
							.getInstance().getRoleID());

					// create permissions for admin_temp
					List<IUserAction> adminops = new ArrayList<IUserAction>();
					adminops.addAll(UserManagementAction.getAll());

					// create privilege for admin (kann nicht über create, da
					// noch keine Rechte vorhanden
					Privilege adminpriv = new Privilege("UserManagement",
							adminrole, adminops, instance.getPrivID());

					// add privilege for admin_temp
					adminrole.addPrivilege(adminpriv);

					// add admin role to system
					sys.addRole(adminrole);

					// ------------------------------------------------
					// create DataDictoinary Role
					instance.createRole("datadictionary", sys);
					// create permission for admin
					List<IUserAction> admindatadic = new ArrayList<IUserAction>();
					admindatadic.addAll(DataDictionaryAction.getAll());
					// create DataDic Priv and add to Role
					for (IUserAction action : admindatadic) {
						instance.grantPermission(sys, "datadictionary", action,
								"DataDictionary");
					}
					// add to system
					instance.grantRole(sys, "datadictionary", "System");

					// ----- TEST ----
					// instance.createRole("admin",
					// instance.login("System", "manager"));
					System.out.println("USER TEST");
					// create user
					instance.registerUser(sys, "Tester", "test");
					System.out.println("User Tester angelegt.");
					instance.grantPermission(sys, "Tester",
							UserManagementAction.CREATE_USER, "UserManagement");
					// instance.grantPermission(sys, "Tester",
					// UserManagementAction.CREATE_PRIV, "UserManagement");
					// instance.grantPermission(sys, "Tester",
					// UserManagementAction.GRANT, "UserManagement");
					// instance.registerUser(instance.login("Tester", "test"),
					// "Joe", "test");
					// instance.grantPermission(instance.login("Tester",
					// "test"),
					// "Joe", UserManagementAction.CREATE_USER,
					// "UserManagement");
					System.out.println("add permission 'ADD_ENTITY'");
					instance.grantPermission(sys, "Tester",
							DataDictionaryAction.ADD_ENTITY,
							DataDictionaryAction.alias);
					System.out
							.println("add permission ADD_SOURCETYPE on existing objekt");
					instance.grantPermission(sys, "Tester",
							DataDictionaryAction.ADD_SOURCETYPE,
							DataDictionaryAction.alias);
					instance.grantPermission(sys, "Tester",
							DataDictionaryAction.ADD_VIEW,
							DataDictionaryAction.alias);
					// System.out.println("add group admin to Tester");
					// instance.grantRole(sys, "admin", "Tester");
					// instance.grantRole(sys, "datadictionary", "Joe");
					// for (IUserAction action : UserManagementAction.getAll())
					// {
					// instance.grantPermission(sys, "admin", action,
					// "UserManagement");
					// }
					// instance.grantRole(instance.login("Tester", "test"),
					// "admin", "Joe");
					// instance.revokePermission(sys, "admin",
					// UserManagementAction.GRANT_ALL, "UserManagement");
					// instance.revokePermission(sys, "admin",
					// UserManagementAction.REVOKE_ALL, "UserManagement");
					// System.out.println("Tester hat kein GRANT_ALL mehr");
					// instance.grantRole(instance.login("Tester", "test"),
					// "admin", "Joe");
					// instance.revokeRole(instance.login("Tester", "test"),
					// "datadictionary", "Joe");
					// System.out.println("joe sollte in role sys_adin sein");
					// System.out.println("grant group dataD to Tester");
					// instance.grantRole(sys, "datadictionary", "Tester");
					// System.out.println("revoke group dataD from Tester");
					// instance.revokeRole(sys, "datadictionary", "Tester");
					// System.out
					// .println("revoke permission ADD_SOURCE from Tester");
					// instance.revokePermission(sys, "Tester", ar2,
					// "DataDictionary");
					// instance.deleteUser(sys, "Tester");

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
