package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;


public class SDFConstantList extends SDFSchemaElementSet<SDFConstant> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7031492990540138630L;


	public SDFConstantList(String URI) {
		super(URI);
	}

	public SDFConstantList() {
		super();
	}

	public void addAll(SDFConstantList constants) {
		super.addAll(constants);
	}

	public SDFConstant getConstant(int index) {
		return (SDFConstant) super.get(index);
	}
	

    /**
     * Erzeugt eine neue Kostantenmenge mit der Schnittmenge der beiden
     * Mengen
     * @param constantSet1
     * @param constantSet2
     * @return
     */
    public static SDFConstantList intersection(SDFConstantList constantSet1, SDFConstantList constantSet2) {
               
        // Die Trivialfaelle, wenn eine der beiden Mengen leer ist, gibt es keine Schnittmenge
        if (constantSet1 == null && constantSet2 == null){
            return null;
        }

        SDFConstantList newSet = new SDFConstantList();
        // Die kleinere der beiden Menge sollte die Ausgangsmenge sein
        SDFConstantList smallerSet = constantSet1;
        SDFConstantList largerSet = constantSet1;
        if (constantSet1.size()>constantSet2.size()){
            smallerSet = constantSet2;
            largerSet = constantSet1;
        }
        for (int i=0;i<smallerSet.size();i++){
            if (largerSet.contains(smallerSet.get(i))){
                newSet.add(smallerSet.getConstant(i));
            }
        }
        return newSet;
    }

    /**
     * @return
     */
    
	
}