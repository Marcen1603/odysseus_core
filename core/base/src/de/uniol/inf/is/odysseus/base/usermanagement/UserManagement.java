package de.uniol.inf.is.odysseus.base.usermanagement;


public class UserManagement extends AbstractUserManagement {

	static private UserManagement instance = null;

	public UserManagement(IUserStore userStore) {
		super(userStore);
	}

	public synchronized static UserManagement getInstance() {
		if (instance == null) {

			try {
				instance = new UserManagement(new MemoryUserStore());
//				instance = new UserManagement(new FileUserStore(
//						System.getProperty("user.home") + "/odysseus/users"));
				// TODO: Spaeter entfernen

				instance.registerUser("Console", "");
				instance.registerUser("Benchmark", "");
				instance.registerUser("RCP", "");
				instance.registerUser("Super", "held");
				instance.registerUser("P2P", "jkhdsfkewrt");
			} catch (UsernameAlreadyUsedException e) {
				e.printStackTrace();
			} catch (UserStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}

	// Hack um kurzfristig Nutzer im P2P zu erlauben und spaeter einfach wieder zu entfernen
	public User getP2PUser() {
		return login("P2P", "jkhdsfkewrt");
	}

}
