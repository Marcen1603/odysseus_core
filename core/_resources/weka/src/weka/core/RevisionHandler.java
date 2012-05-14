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
 * RevisionHandler.java
 * Copyright (C) 2008 University of Waikato, Hamilton, New Zealand
 */

package weka.core;

/**
 * For classes that should return their source control revision.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.1 $
 * @see     weka.core.RevisionUtils
 */
public interface RevisionHandler {

  /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
  public String getRevision();
}
