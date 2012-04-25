/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 *
 * Copyright (C) 2001 Vivid Solutions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, contact:
 *
 *     Vivid Solutions
 *     Suite #1A
 *     2328 Government Street
 *     Victoria BC  V8T 5G5
 *     Canada
 *
 *     (250)385-6040
 *     www.vividsolutions.com
 */
package com.vividsolutions.jts.algorithm;

/**
 * An interface for rules which determine whether node points
 * which are in boundaries of lineal geometry components
 * are in the boundary of the parent geometry collection.
 * The SFS specifies a single kind of boundary node rule,
 * the {@link Mod2BoundaryNodeRule} rule.
 * However, other kinds of Boundary Node Rules are appropriate
 * in specific situations (for instance, linear network topology
 * usually follows the {@link EndPointBoundaryNodeRule}.)
 * Some JTS operations allow the BoundaryNodeRule to be specified,
 * and respect this rule when computing the results of the operation.
 *
 * @author Martin Davis
 * @version 1.7
 *
 * @see RelateOp
 * @see IsSimpleOp
 */
public interface BoundaryNodeRule
{

  boolean isInBoundary(int boundaryCount);

  /**
   * The Mod-2 Boundary Node Rule (as used in the OGC SFS).
   */
  public static final BoundaryNodeRule MOD2_BOUNDARY_RULE = new Mod2BoundaryNodeRule();


  /**
   * The Endpoint Boundary Node Rule.
   */
  public static final BoundaryNodeRule ENDPOINT_BOUNDARY_RULE = new EndPointBoundaryNodeRule();

  /**
   * The MultiValent Endpoint Boundary Node Rule.
   */
  public static final BoundaryNodeRule MULTIVALENT_ENDPOINT_BOUNDARY_RULE = new MultiValentEndPointBoundaryNodeRule();

  /**
   * The Monovalent Endpoint Boundary Node Rule.
   */
  public static final BoundaryNodeRule MONOVALENT_ENDPOINT_BOUNDARY_RULE = new MonoValentEndPointBoundaryNodeRule();

  /**
   * The Boundary Node Rule specified by the OGC Simple Features Specification,
   * equal to the Mod-2 rule.
   */
  public static final BoundaryNodeRule OGC_SFS_BOUNDARY_RULE = MOD2_BOUNDARY_RULE;

  /**
   * A {@link BoundaryNodeRule} specifies that points are in the
   * boundary of a lineal geometry iff
   * the point lies on the boundary of an odd number
   * of components.
   * Under this rule {@link LinearRing}s and closed
   * {@link LineString}s have an empty boundary.
   * <p>
   * This is the rule specified by the <i>OGC SFS</i>,
   * and is the default rule used in JTS.
   *
   * @author Martin Davis
   * @version 1.7
   */
  public static class Mod2BoundaryNodeRule
      implements BoundaryNodeRule
  {
    public boolean isInBoundary(int boundaryCount)
    {
      // the "Mod-2 Rule"
      return boundaryCount % 2 == 1;
    }
  }

  /**
   * A {@link BoundaryNodeRule} which specifies that any points which are endpoints
   * of lineal components are in the boundary of the
   * parent geometry.
   * This corresponds to the "intuitive" topological definition
   * of boundary.
   * Under this rule {@link LinearRing}s have a non-empty boundary
   * (the common endpoint of the underlying LineString).
   * <p>
   * This rule is useful when dealing with linear networks.
   * For example, it can be used to check
   * whether linear networks are correctly noded.
   * The usual network topology constraint is that linear segments may touch only at endpoints.
   * In the case of a segment touching a closed segment (ring) at one point,
   * the Mod2 rule cannot distinguish between the permitted case of touching at the
   * node point and the invalid case of touching at some other interior (non-node) point.
   * The EndPoint rule does distinguish between these cases,
   * so is more appropriate for use.
   *
   * @author Martin Davis
   * @version 1.7
   */
  public static class EndPointBoundaryNodeRule
      implements BoundaryNodeRule
  {
    public boolean isInBoundary(int boundaryCount)
    {
      return boundaryCount > 0;
    }
  }

  /**
   * A {@link BoundaryNodeRule} which determines that only
   * endpoints with valency greater than 1 are on the boundary.
   * This corresponds to the boundary of a {@link MultiLineString}
   * being all the "attached" endpoints, but not
   * the "unattached" ones.
   *
   * @author Martin Davis
   * @version 1.7
   */
  public static class MultiValentEndPointBoundaryNodeRule
      implements BoundaryNodeRule
  {
    public boolean isInBoundary(int boundaryCount)
    {
      return boundaryCount > 1;
    }
  }

  /**
   * A {@link BoundaryNodeRule} which determines that only
   * endpoints with valency of exactly 1 are on the boundary.
   * This corresponds to the boundary of a {@link MultiLineString}
   * being all the "unattached" endpoints.
   *
   * @author Martin Davis
   * @version 1.7
   */
  public static class MonoValentEndPointBoundaryNodeRule
      implements BoundaryNodeRule
  {
    public boolean isInBoundary(int boundaryCount)
    {
      return boundaryCount == 1;
    }
  }


}