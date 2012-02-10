///** Copyright [2011] [The Odysseus Team]
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  *     http://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
//package de.uniol.inf.is.odysseus.scars;
//
//import java.util.List;
//
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpressionParseException;
//
//public class SDFObjectRelationalExpression extends SDFExpression {
//
//	private static final long serialVersionUID = 1L;
//	private List<String[]> attributePaths;
//	private int[][] attributeIntPaths;
//	private int[] targetPath;
//
//	public SDFObjectRelationalExpression(String URI, String value, IAttributeResolver attributeResolver) throws SDFExpressionParseException {
//		super(URI, value, attributeResolver);
//	}
//
////	@Override
////	protected void insertAttributePath(String token) {
////		if( attributePaths == null ) 
////			attributePaths = new ArrayList<String[]>();
////		this.attributePaths.add(token.split("\\."));
////	}
////	
//	/**
//	 * 
//	 * @deprecated Wird nirgends verwendet
//	 */
//	public void initAttributePaths( SDFSchema schema ) {
//		this.attributeIntPaths = new int[this.attributePaths.size()][];
//		
//		for( int i = 0; i < this.attributePaths.size(); i++ ) {
//			String[] p = this.attributePaths.get(i);
//			
//			int[] indices = new int[p.length];
//			findNext( schema, p, 0, indices);
//			
//			this.attributeIntPaths[i] = indices;
//		}
//	}
//
//	/**
//	 * @deprecated Wird nur in Methoden verwendet, die nirgends verwendet werden
//	 */
//	private void findNext(SDFSchema list, String[] path, int index, int[] indices) {
//		String attrToFind = path[index];
//		for( int i = 0; i < list.getAttributeCount(); i++ ) {
//			SDFAttribute attr = list.get(i);
//			if( attr.getAttributeName().equals(attrToFind)) {
//				indices[index] = i;
//				if( index < path.length - 1) 
//					findNext(attr.getSubattributes(), path, index +1, indices);
//			}
//		}
//	}
//	
//	/**
//	 * 
//	 * @deprecated Wird nirgends verwendet
//	 */
//	public int[][] getAttributePaths() {
//		return this.attributeIntPaths;
//	}
//	
//	/**
//	 * @deprecated Wird nirgends verwendet
//	 */
//	public void initTarget( String targetAttributeName, SDFSchema schema ) {
//		String[] parts = targetAttributeName.split("\\.");
//		targetPath = new int[parts.length];
//		findNext( schema, parts, 0, targetPath);
//	}
//	
//	/**
//	 * @deprecated Wird nirgends verwendet
//	 */
//	public int[] getTargetPath() {
//		return targetPath;
//	}
//}
