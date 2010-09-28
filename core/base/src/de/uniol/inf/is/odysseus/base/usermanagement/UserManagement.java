package de.uniol.inf.is.odysseus.base.usermanagement;



public class UserManagement extends AbstractUserManagement {

	static private UserManagement instance = null;

	public UserManagement(IUserStore userStore) {
		super(userStore);
	}
	
	public synchronized static UserManagement getInstance() {
		if (instance == null) {

			try {
//				instance = new UserManagement(new MemoryUserStore());
				instance = new UserManagement(new FileUserStore(
						System.getProperty("user.home") + "/odysseus/users.store"));
				// TODO: Spaeter entfernen
				
				if (instance.hasNoUsers()){
					instance.registerUserInt("Console", "");
					instance.registerUserInt("Benchmark", "");
					instance.registerUserInt("RCP", "");
					instance.registerUserInt("Super", "held");
					instance.registerUserInt("P2P", "jkhdsfkewrt");
					instance.registerUserInt("Test", "jkhdsfkewrt");

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

	// Hack um kurzfristig Nutzer im P2P zu erlauben und spaeter einfach wieder zu entfernen
	public User getP2PUser() {
		return login("P2P", "jkhdsfkewrt");
	}

	public User getTestUser() {
		return login("Test","jkhdsfkewrt");
	}

	

}
