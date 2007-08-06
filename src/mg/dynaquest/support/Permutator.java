/*
 * Created on 02.02.2005
 *
 */
package mg.dynaquest.support;


/**
 * @author Marco Grawunder
 *
 */
public class Permutator {
    
    /**
	 * @uml.property  name="maxValues" multiplicity="(0 -1)" dimension="1"
	 */
    int[] maxValues;
    /**
	 * @uml.property  name="curValue" multiplicity="(0 -1)" dimension="1"
	 */
    int[] curValue;
    /**
	 * @uml.property  name="finished"
	 */
    boolean finished = false;
    /**
	 * @uml.property  name="first"
	 */
    boolean first = true;
        
    public Permutator(int[] maxValues){
        this.maxValues = maxValues;
        curValue = new int[this.maxValues.length];
        restart();
    }

	public void restart() {		
        for (int i=0;i<this.maxValues.length;i++){
            curValue[i]=0;
        }
	}
    
    public Permutator(Permutator p) {
    	if (p != null){
    		if (p.maxValues != null){
    			System.arraycopy(p.maxValues, 0, maxValues, 0, p.maxValues.length);
    		}
    		if (p.curValue != null){
    			System.arraycopy(p.curValue, 0, curValue, 0, p.curValue.length);
    		}
	    	finished = p.finished;
	    	first = p.first;
    	}
	}

	private void incCurValue(){
        boolean overflow = false;
        for (int i=0;i<curValue.length;i++){
            //System.out.println("incCurValue i="+i+" curVal[i]="+curValue[i]+" maxVal[i]"+maxValues[i]+" "+overflow);
            if (curValue[i]<maxValues[i]){
                curValue[i]++;
                if (overflow){
                    for (int j=0;j<i;j++){
                        curValue[j] = 0;
                    }
                }
                return;
            }else{
                overflow = true;
            }
        }
        finished = true;
    }
    
    public int[] nextValue(){
        if (!first){
            incCurValue();
        }
        first = false;
        if (!finished){
            return curValue;
        }else{
            return null;
        }
    }
    
    

    public static void main(String[] args) {
        int[] max1={5,2,2,2};
        Permutator test1=new Permutator(max1);
        int[] val = null;
        while ((val = test1.nextValue())!=null){
            for (int i=0;i<val.length;i++){
                System.out.print(val[i]+" ");
            }
            System.out.println();
        }
    }
}
