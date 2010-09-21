package de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator.HypothesisSelectionAO;

public class AssociationDictionary {

  private final static AssociationDictionary instance = new AssociationDictionary();
  private HashMap<String, HypothesisSelectionAO<?>> sources = new HashMap<String, HypothesisSelectionAO<?>>();

  private AssociationDictionary() {
  }

  public static AssociationDictionary getInstance() {
    return instance;
  }

  public void addSource(String opName, HypothesisSelectionAO<?> source) {
    if (!this.sources.containsKey(opName)) {
      this.sources.put(opName, source);
    }
  }

  public HypothesisSelectionAO<?> getSource(String srcName) {
    if (this.sources.containsKey(srcName)) {
      return this.sources.get(srcName);
    }
    return null;
  }
  
  public void clear(){
	  this.sources = new HashMap<String, HypothesisSelectionAO<?>>();
  }
}
