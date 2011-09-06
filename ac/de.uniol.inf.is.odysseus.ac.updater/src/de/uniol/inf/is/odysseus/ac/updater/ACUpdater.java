package de.uniol.inf.is.odysseus.ac.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

public class ACUpdater extends Thread {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ACUpdater.class);
		}
		return _logger;
	}

	private static final long UPDATE_INTERVAL = 5000;

	private IAdmissionControl ac;
	private boolean isRunning = true;
	
	public ACUpdater(IAdmissionControl ac) {
		this.ac = ac;
		this.setName("ACUpdater");
	}
//	private static final String TEST_QUERY = ""+
//	"SELECT randomStream.number " + 
//	"FROM randomStream [SIZE 20 ADVANCE 1 TUPLE], " + 
//	"     randomStream2 [SIZE 20 ADVANCE 1 TUPLE] " +
//	"WHERE randomStream.number = randomStream2.number2 " + 
//	"  AND randomStream.number > 20 " +
//	"  AND randomStream2.number2 < 80; ";
//	
//	private static final String SRC_DEF_QUERY = ""+
//    " CREATE STREAM randomStream (timestamp STARTTIMESTAMP, number INTEGER ) " +
//	" CHANNEL localhost : 11111; " +
//	" CREATE STREAM randomStream2 (timestamp2 STARTTIMESTAMP, number2 INTEGER ) " + 
//	" CHANNEL localhost : 22222; ";
//	private int steps = STEPS_BEFORE_INSERT; // damit am anfang eine anfrage kommt
//	private IExecutor executor;
//	private static final int STEPS_BEFORE_INSERT = 4;

	@Override
	public void run() {
		try {

//			final User user = GlobalState.getActiveUser("RCP");
//			final IDataDictionary dd = GlobalState.getActiveDatadictionary();
//			final List<IQueryBuildSetting<?>> cfg = executor.getQueryBuildConfiguration("Standard");
//			
//			Collection<IQuery> queries = executor.addQuery(SRC_DEF_QUERY, "CQL", user, dd, cfg.toArray(new IQueryBuildSetting[0]) );
//			for (IQuery query:queries){
//				executor.startQuery(query.getID(), user);
//			}
			
			while (isRunning) {

				ac.updateEstimations();
//				System.out.println(ac.getActualCost());
				
//				steps++;
//				if( steps >= STEPS_BEFORE_INSERT ) {
//					queries = executor.addQuery(TEST_QUERY, "CQL", user, dd, cfg.toArray(new IQueryBuildSetting[0]) );
//					for (IQuery query:queries){
//						executor.startQuery(query.getID(), user);
//					}
//					steps = 0;
//				}
				
				try {
					Thread.sleep(UPDATE_INTERVAL);
				} catch (InterruptedException ex) {
				}
				
				
			}
		} catch( Throwable t ) {
			t.printStackTrace();
		}
	}

	public void stopRunning() {
		isRunning = false;
	}

	public void startRunning(IExecutor executor) {
		isRunning = true;
//		this.executor = executor;
		if (!isAlive())
			start();
	}
}
