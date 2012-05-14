/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    StructureProducer.java
 *    Copyright (C) 2011 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.gui.beans;

import weka.core.Instances;

/**
 * Interface for something that can describe the structure of what
 * is encapsulated in a named event type as an empty set of
 * Instances (i.e. attribute information only). For example,
 * batchAssociationRulesEvent can be described as LHS (String), RHS (String)
 * and a number of metrics (Numeric); dataSetEvent, instanceEvent, 
 * trainingSetEvent etc. are all straightforward, since they already
 * encapsulate instances; textEvent can be described in terms of
 * the title (String) and the body (String) of the text. Sometimes
 * it is not possible to know the structure of the encapsulated output
 * in advance (e.g. certain filters may not know how many attributes
 * will be produced until the input data arrives), in this case
 * any implementing class can return null to indicate this. 
 * 
 * @author Mark Hall (mhall{[at]}pentaho{[dot]}com)
 * @version $Revision: 7439 $
 *
 */
public interface StructureProducer {
  
  /**
   * Get the structure of the output encapsulated in the named
   * event. If the structure can't be determined in advance of
   * seeing input, or this StructureProducer does not generate
   * the named event, null should be returned.
   * 
   * @param eventName the name of the output event that encapsulates
   * the requested output.
   * 
   * @return the structure of the output encapsulated in the named
   * event or null if it can't be determined in advance of seeing input
   * or the named event is not generated by this StructureProduce.
   */
  public Instances getStructure(String eventName);
}
