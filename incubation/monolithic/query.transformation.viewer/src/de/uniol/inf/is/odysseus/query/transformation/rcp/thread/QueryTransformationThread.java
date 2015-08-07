package de.uniol.inf.is.odysseus.query.transformation.rcp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.main.QueryTransformation;
import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.transformation.rcp.window.QueryTransformationWindow;



public class QueryTransformationThread extends Thread {
	
	private static Logger LOG = LoggerFactory.getLogger(QueryTransformationThread.class);
	private final QueryTransformationWindow window;
	
	private TransformationParameter parameter; 
	
	 private BlockingQueue<ProgressBarUpdate> queue = new ArrayBlockingQueue<ProgressBarUpdate>(100);
	
	public QueryTransformationThread(TransformationParameter parameter, QueryTransformationWindow window){
		this.window = window;
		this.parameter = parameter;
	}
	
	@Override
	public void run() {
		LOG.debug("Query transformation started...");
		queue = new ArrayBlockingQueue<ProgressBarUpdate>(100);
		

		Thread checkUpdates =  new Thread(){
			      public void run() {
			         while (!isInterrupted()) {
			            try { 
			            	Thread.sleep(1000); 
			            
			            	ProgressBarUpdate updateInfo = queue.take();
			           if(updateInfo != null){
			        	   window.updateProgressbar(updateInfo);
			           }
			            } catch (InterruptedException e) { 
			            	interrupt();
			            }finally{
			            	
			            	ProgressBarUpdate updateInfo;
							try {
								while(!queue.isEmpty()){
									updateInfo = queue.take();
									if(updateInfo != null){
						        	   window.updateProgressbar(updateInfo);
									}
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	
			            }
			      }
			 }
		};
		   
		checkUpdates.setDaemon(true);
		checkUpdates.start();
	
		QueryTransformation.startQueryTransformation(parameter,queue);
		window.showFinishButton();
		
		checkUpdates.interrupt();
		
	}
}
