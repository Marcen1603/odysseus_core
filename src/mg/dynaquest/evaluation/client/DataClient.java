package mg.dynaquest.evaluation.client;

import java.rmi.Naming;
//import java.rmi.RMISecurityManager;
import java.util.Random;
import mg.dynaquest.evaluation.server.RemoteDataProvider;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;

public class DataClient {

	public static void testProvider(RemoteDataProvider dataProvider, String name)
			throws Exception {

		dataProvider
				.executeQuery(
						"Select VkNr, Vorname, Nachname from Dynq_Verkaeufer where Ort = 'CDorf'",
						name, null);
		RelationalTuple res = null;

		System.out.println(name + " --> Start " + System.currentTimeMillis());
		while ((res = dataProvider.nextObject(name)) != null) {
			System.out.println(name + " ---> " + res.toString());
		}
		System.out.println(name + " ---> Ready " + System.currentTimeMillis());

	}

	public static void main(String[] args) {
		try {
			Random rnd = new Random();
			//RMISecurityManager secMan = new RMISecurityManager();
			//System.setSecurityManager(secMan);
			RemoteDataProvider dataProvider = (RemoteDataProvider) Naming
					.lookup("rmi://power2.offis.uni-oldenburg.de/DataProvider_Dynq_Verkauf_Vollst");
			testProvider(dataProvider, "test" + rnd.nextInt(10));
			//long now = System.currentTimeMillis();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}

	}
}