package mg.dynaquest.sourceselection;

import java.util.ArrayList;
/**
 * @author  Jurij Henne  Dient dazu Pläne in einer Liste in ihrer Ausführungsreihenfolge zu ordnen.
 */

public class AnnotatedPlanQueue
{

    private ArrayList<AnnotatedPlan> queue = new ArrayList<AnnotatedPlan>();

    public AnnotatedPlanQueue (){}

    
    public AnnotatedPlanQueue (ArrayList<AnnotatedPlan> planList)
    {
    	queue.addAll(planList);
    }
	public void addPlan(AnnotatedPlan newPlan) {
		this.queue.add(newPlan);
	}
	
	public void addAll(ArrayList<AnnotatedPlan> planList)
	{
		this.queue.addAll(planList);
	}

	
	public AnnotatedPlan getPlan(int pos) {
		return (AnnotatedPlan)this.queue.get(pos);
	}

	public ArrayList<AnnotatedPlan> getQueue() {
		return this.queue;
	}
	
	public int getQueueSize() {
		return this.queue.size();
	}

    
    public void removePlan(int pos) 
    {
    	this.queue.remove(pos);
	}

    public boolean isEmpty()
    {
    	if(this.queue.isEmpty())
    	   return true;
    	else 
    		return false;
    }
    
    
	
	public String toString() {
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < queue.size(); i++) 
		{
			ret.append("[");
			ret.append(getPlan(i).getGlobalOutputPattern() + ",");
			ret.append("]\n");
		}
		return ret.toString();
	}

}
