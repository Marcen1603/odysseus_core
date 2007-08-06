/**
 * 
 */
package mg.dynaquest.queryexecution.caching.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import junit.framework.TestCase;
import mg.dynaquest.queryexecution.caching.CacheManager;
import mg.dynaquest.queryexecution.caching.StorageManager;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.caching.CachingPO;
import mg.dynaquest.sourcedescription.sdf.description.SDFSource;

/**
 * @author Tobias Hesselmann
 * 
 */
public class CachingTest extends TestCase {

	int semanticRegionId = -1;
	
	/**
	 * Testet ob Caching PO geplant werden kann
	 * 
	 * @throws TimeoutException
	 * @throws POException
	 */
	public void testCachingPOAccessPO() throws POException, TimeoutException {
		SDFSource source = new SDFSource(
				"http://134.106.52.176/Automarkt2004Quelle1");
		SchemaTransformationAccessPO schemaTransformationAccessPO = new SchemaTransformationAccessPO();
		schemaTransformationAccessPO.setSource(source);
		CachingPO c3po = new CachingPO(schemaTransformationAccessPO);
		assertEquals(c3po.getInputPO().getPOName(), "RMIDataAccessPO");
	}

	/**
	 * Testet ob Properties geladen werden können
	 * 
	 * @throws TimeoutException
	 * @throws POException
	 */
	public void testCacheManagerProperties() {
		assertNotNull(CacheManager.getInstance().isCachingEnabled());
	}

	/**
	 * Testet Datenbankverbindung
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws TimeoutException
	 * @throws POException
	 */
//	public void testDatabaseConnection() throws ClassNotFoundException,
//			SQLException, FileNotFoundException, IOException {
//		assertNotNull(StorageManager.getInstance().getDataBaseConnection());
//	}

	/**
	 * Legt semantische Region an und löscht sie wieder
	 *
	 */
//	public void testSemanticRegionCreation() {
//		SDFSource sdfSource = new SDFSource(
//				"http://www-is.uni-oldenburg.de/test.xml#source1");
//		ArrayList<SDFSimplePredicate> predicateList = new ArrayList<SDFSimplePredicate>();
//		SDFAttribute attribute = new SDFAttribute(
//				"http://www-is.uni-oldenburg.de/test.xml#Vorname");
//
//		SDFConstant sdfConst = new SDFNumberConstant(
//				"http://www-is.uni-oldenburg.de/test.xml#nummer", 23);
//
//		SDFSimplePredicate predicate = SDFSimplePredicateFactory
//				.createSimplePredicate(
//						"http://www-is.uni-oldenburg.de/test.xml#uri",
//						SDFPredicates.NumberPredicate, attribute,
//						SDFCompareOperatorFactory
//								.getCompareOperator(SDFPredicates.Equal), null,
//						sdfConst, null, null);
//
//		predicateList.add(predicate);
//
//		try {
//			this.semanticRegionId = StorageManager.getInstance().createSemanticRegion(predicateList, sdfSource);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertNotSame(this.semanticRegionId, -1);
//		
//		try {
//			StorageManager.getInstance().deleteSemanticRegion(this.semanticRegionId);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
