package de.uniol.inf.is.odysseus.usermanagement;

import de.uniol.inf.is.odysseus.ConfigurationAction;
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
				if (Boolean.parseBoolean(OdysseusDefaults.get("storeUsers"))){
					 instance = new UserManagement(new FileUserStore(OdysseusDefaults.get("userStoreFilename")));
				}else{
					instance = new UserManagement(new MemoryUserStore());
				}

				if (instance.hasNoUsers()) {
					// create system user
					instance.registerUserInt("System", "manager");
				}
				User sys = instance.login("System", "manager", false);
				// set protection
				sys.setSystemProtection(sys);
				
				if (instance.hasNoRoles()) {
									
					// create admin Role
					Role adminrole = new Role("sys_admin", UserManagement
							.getInstance().getRoleID());
					// set protection
					adminrole.setSystemProtection(sys);

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

					// create public group
					instance.createGroup("Public", sys);
					
					// ------------------------------------------------
					// create DataDictoinary Role
					Role ddrole = instance.createRole("datadictionary", sys);
					// create permission for admin
					// create DataDic Priv and add to Role
					for (IUserAction action : DataDictionaryAction.getAll()) {
						instance.grantPermission(sys, "datadictionary", action,
								DataDictionaryAction.alias);
					}
					// set protection
					ddrole.setSystemProtection(sys);
					// add to system
					instance.grantRole(sys, "datadictionary", "System");
					
					// ---------------------------------------------------
					// create Configuration Role
					Role conf = instance.createRole("configuration", sys);
					for (IUserAction action : ConfigurationAction.getAll()){
						instance.grantPermission(sys, "configuration", action, ConfigurationAction.alias);
					}
					conf.setSystemProtection(sys);
					instance.grantRole(sys, "configuration", "System");

					// -----------------------------------------------------
					// create ExecutorRole
					Role qrole = instance.createRole("queryexecutor", sys);
					for (IUserAction action : ExecutorAction.getAll()) {
						instance.grantPermission(sys, "queryexecutor", action,
								ExecutorAction.alias);
					}
					// set protection
					qrole.setSystemProtection(sys);
					// add to system
					instance.grantRole(sys, "queryexecutor", "System");
					
					// ----- TEST ----
//					 instance.registerUser(sys, "Tester", "test");
					// instance.grantPermission(sys, "Tester",
					// UserManagementAction.CREATE_USER, "UserManagement");
					// instance.grantPermission(sys, "Tester",
					// DataDictionaryAction.ADD_ENTITY,
					// DataDictionaryAction.alias);
					// instance.grantPermission(sys, "Tester",
					// DataDictionaryAction.ADD_SOURCETYPE,
					// DataDictionaryAction.alias);
					// instance.grantPermission(sys, "Tester",
					// DataDictionaryAction.ADD_VIEW,
					// DataDictionaryAction.alias);
					// instance.grantRole(sys, "DSUser", "Tester");

					// Create Default Role for users
					instance.createRole("DSUser", sys);
					// Anfragen erstellen und entfernen
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.ADD_ENTITY, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.REMOVE_ENTITY, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.ADD_VIEW, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.ADD_STREAM, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.REMOVE_VIEW, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.ADD_SOURCETYPE, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.GET_ENTITY, DataDictionaryAction.alias);
					instance.grantPermission(sys, "DSUser", DataDictionaryAction.READ, DataDictionaryAction.alias);

					// Anfrage verwalten
					instance.grantPermission(sys, "DSUser", ExecutorAction.ADD_QUERY, ExecutorAction.alias);
					instance.grantPermission(sys, "DSUser", ExecutorAction.START_QUERY, ExecutorAction.alias);
					instance.grantPermission(sys, "DSUser", ExecutorAction.STOP_QUERY, ExecutorAction.alias);
					instance.grantPermission(sys, "DSUser", ExecutorAction.REMOVE_QUERY, ExecutorAction.alias);
					
//					System.out.println("grant DSU to Tester");
//					instance.grantRole(sys, "DSUser", "Tester");
//					instance.grantPermission(sys, "Tester", UserManagementAction.REVOKE_ROLE, UserManagementAction.alias);
//					instance.grantPermission(sys, "Tester", UserManagementAction.GRANT_ROLE, UserManagementAction.alias);
					
//					System.out.println("grant DSU to sys 1st");
//					instance.grantRole(UserManagement.getInstance().login("Tester", "test", false), "DSUser", "System");
//					instance.grantRole(UserManagement.getInstance().login("Tester", "test", false), "sys_admin", "System");
//					instance.grantRole(sys, "DSUser", "Tester");
//					System.out.println("grant DSU to sys 2nd");
//					instance.grantRole(UserManagement.getInstance().login("Tester", "test", false), "DSUser", "Tester");
//					instance.grantRole(UserManagement.getInstance().login("Tester", "test", false), "DSUser", "System");
//					System.out.println("revoke DSU from sys");
//					instance.revokeRole(UserManagement.getInstance().login("Tester", "test", false), "DSUser", "System");
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

	public void createGroup(String string, User sys) {
		Role r = createRole(string, sys);
		r.setGroup(true);
	}

	public User getSuperUser() {
		return login("System", "manager", false);
	}

}
