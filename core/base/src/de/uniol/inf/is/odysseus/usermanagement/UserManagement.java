package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.List;

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
				// TODO: Spaeter entfernen

				// TODO: testen mit Scott / tiger

				if (instance.hasNoUsers()) {
					// ------------------------------------------------
					// create system user
					instance.registerUserInt("System", "manager");
				}

				if (instance.hasNoRoles()) {
					// create admin Role
					Role adminrole = new Role("admin", UserManagement
							.getInstance().getRoleID());

					// create permissions for admin
					List<IUserActions> adminops = new ArrayList<IUserActions>();
					adminops.add(UserManagementActions.CREATEPRIV);
					adminops.add(UserManagementActions.CREATEROLE);
					adminops.add(UserManagementActions.CREATEUSER);
					adminops.add(UserManagementActions.DELETEUSER);
					adminops.add(UserManagementActions.GRANT);
					adminops.add(UserManagementActions.REVOKE);
					adminops.add(UserManagementActions.UPDATEUSER_PASSWORD);

					// create privilege for admin
					Privilege adminpriv = new Privilege("adminprivs", null,
							adminops, UserManagement.getInstance().getPrivID());

					// add privilege for admin
					adminrole.addPrivilege(adminpriv);

					// add admin role to system
					instance.findUser("System", null).addRole(adminrole);

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

	public User getSuperUser(){
		return login("System", "manager");
	}
	
}
