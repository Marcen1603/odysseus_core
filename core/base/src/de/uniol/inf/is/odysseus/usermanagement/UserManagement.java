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
					instance.grantPermission(sys, "datadictionary",
							admindatadic, "DataDictionary");
					// add to system
					instance.grantRole(sys, "datadictionary", "System");

					// ----- TEST ----
//					instance.createRole("admin",
//							instance.login("System", "manager"));
//					System.out.println("USER TEST");
//					// create user
//					instance.registerUser(sys, "Tester", "test");
//					System.out.println("User Tester angelegt.");
//					System.out.println("add permission 'ADD_ENTITY'");
//					ArrayList<IUserAction> ar = new ArrayList<IUserAction>();
//					ar.add(DataDictionaryAction.ADD_ENTITY);
//					instance.grantPermission(sys, "Tester", ar,
//							"DataDictionary");
//					System.out
//							.println("add permission ADD_SOURCETYPE on existing objekt");
//					ArrayList<IUserAction> ar2 = new ArrayList<IUserAction>();
//					ar2.add(DataDictionaryAction.ADD_SOURCETYPE);
//					instance.grantPermission(sys, "Tester", ar2,
//							"DataDictionary");
//					System.out.println("add group admin to Tester");
//					instance.grantRole(sys, "admin", "Tester");
//					System.out.println("grant group dataD to Tester");
//					instance.grantRole(sys, "datadictionary", "Tester");
//					System.out.println("revoke group dataD from Tester");
//					instance.revokeRole(sys, "datadictionary", "Tester");
//					System.out
//							.println("revoke permission ADD_SOURCE from Tester");
//					instance.revokePermission(sys, "Tester", ar2,
//							"DataDictionary");
//					instance.deleteUser(sys, "Tester");

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
