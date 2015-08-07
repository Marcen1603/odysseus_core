package de.uniol.inf.is.odysseus.query.transformation.target.platform;

import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;

public abstract class AbstractTargetPlatform implements ITargetPlatform{
	
	private BlockingQueue<ProgressBarUpdate> progressBarQueue;
	
	public void updateProgressBar(int value, String text){
		if(progressBarQueue!= null){
			try {
				progressBarQueue.put(new ProgressBarUpdate(value, text));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public BlockingQueue<ProgressBarUpdate> getProgressBarQueue() {
		return progressBarQueue;
	}

	public void setProgressBarQueue(BlockingQueue<ProgressBarUpdate> progressBarQueue) {
		this.progressBarQueue = progressBarQueue;
	}
	
}
