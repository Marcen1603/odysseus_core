package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Category class for a treeviewer
 * 
 * @author Stefan Bothe
 * 
 */
public class PropertiesCategory {
  private String name;
  private int sort;
  private List<Object> list = new ArrayList<Object>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSort() {
    return sort;
  }

  public void setSort(int sort) {
    this.sort = sort;
  }

  public List<Object> getList() {
    return list;
  }
} 