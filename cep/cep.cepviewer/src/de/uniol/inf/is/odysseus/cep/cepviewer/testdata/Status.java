package de.uniol.inf.is.odysseus.cep.cepviewer.testdata;

public enum Status {
	
	RUNNING ("Running", 0),
	FINISHED ("Finished", 1),
	ABORTED ("Aborted", 2);

	private String name;
	private int iD;
	
	Status(String name, int iD) {
		this.name = name;
		this.iD = iD;
	}

	public String getName() {
		return name;
	}

	public int getiD() {
		return iD;
	}
	
}
