package de.uniol.inf.is.odysseus.usermanagement;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryAction;
import de.uniol.inf.is.odysseus.planmanagement.executor.ExecutorAction;

public class UserManagement extends AbstractUserManagement {

	static private UserManagement instance = null;

	public UserManagement(IUserStore userStore) {
		super(userStore);
	}

	public synchronized static UserManagement getInstance() {
		if (instance == null) {

			try {
				if (OdysseusDefaults.storeUsers){
					 instance = new UserManagement(new FileUserStore(OdysseusDefaults.userStoreFilename));
				}else{
					instance = new UserManagement(new MemoryUserStore());
				}

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
//					List<IUserAction> adminops = new ArrayList<IUserAction>();
//					adminops.addAll(UserManagementAction.getAll());

					// create privilege for admin (kann nicht über create, da
					// noch keine Rechte vorhanden
					Privilege adminpriv = new Privilege("UserManagement",
							adminrole, UserManagementAction.getAll(), instance.getPrivID());

					// add privilege for admin_temp
					adminrole.addPrivilege(adminpriv);

					// add admin role to system
					sys.addRole(adminrole);

					// ------------------------------------------------
					// create DataDictoinary Role
					instance.createRole("datadictionary", sys);
					// create permission for admin
					// create DataDic Priv and add to Role
					for (IUserAction action : DataDictionaryAction.getAll()) {
						instance.grantPermission(sys, "datadictionary", action,
								DataDictionaryAction.alias);
					}
					// add to system
					instance.grantRole(sys, "datadictionary", "System");

					// -----------------------------------------------------
					// create ExecutorRole
					instance.createRole("queryexecutor", sys);
					for (IUserAction action : ExecutorAction.getAll()) {
						instance.grantPermission(sys, "queryexecutor", action,
								ExecutorAction.alias);
					}
					// add to system
					instance.grantRole(sys, "queryexecutor", "System");
					
					// ----- TEST ----
					// instance.createRole("admin",
					// instance.login("System", "manager"));
					//System.out.println("USER TEST");
					// create user
					instance.registerUser(sys, "Tester", "test");
					//System.out.println("User Tester angelegt.");
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
					//System.out.println("add permission 'ADD_ENTITY'");
					instance.grantPermission(sys, "Tester",
							DataDictionaryAction.ADD_ENTITY,
							DataDictionaryAction.alias);
				//	System.out
							//.println("add permission ADD_SOURCETYPE on existing objekt");
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

					// Create Default Role for users
					instance.createRole("DSUser", sys);
					// Anfragen erstellen und entfernen
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.ADD_ENTITY, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.REMOVE_ENTITY, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.ADD_LOGICAL_VIEW, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.ADD_VIEW, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.REMOVE_VIEW, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.ADD_SOURCETYPE, DataDictionaryAction.alias);
					// Anfrage verwalten
					instance.grantPermission(sys, "DSUser", ExecutorAction.ADD_QUERY, ExecutorAction.alias);
					instance.grantPermission(sys, "DSUser", ExecutorAction.START_QUERY, ExecutorAction.alias);
					instance.grantPermission(sys, "DSUser", ExecutorAction.STOP_QUERY, ExecutorAction.alias);
					instance.grantPermission(sys, "DSUser", ExecutorAction.REMOVE_QUERY, ExecutorAction.alias);
					
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
