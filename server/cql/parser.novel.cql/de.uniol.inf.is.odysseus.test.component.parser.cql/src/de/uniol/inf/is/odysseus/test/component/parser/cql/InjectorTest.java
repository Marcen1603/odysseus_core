package de.uniol.inf.is.odysseus.test.component.parser.cql;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.parser.novel.cql.CQLStandaloneSetupGenerated;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;

public class InjectorTest {

	public static void main(String... args) {

		try {
			
//			new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("./");
//			new org.eclipse.emf.mwe.utils.StandaloneSetup().addRegisterGenModelFile("/Users/jp/git/odysseus/server/cql/parser.novel.cql/de.uniol.inf.is.odysseus.parser.novel.cql/model/generated/CQL.genmodel");
//			new org.eclipse.emf.mwe.utils.StandaloneSetup().addRegisterEcoreFile("/Users/jp/git/odysseus/server/cql/parser.novel.cql/de.uniol.inf.is.odysseus.parser.novel.cql/model/generated/CQL.ecore");
//			ResourceSet rs = new XtextResourceSet();
//			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
//			EPackage.Registry.INSTANCE.getEPackage(CQLPackage.eNS_URI);
//			CQLPackage.eINSTANCE.getClass();
			Injector injector = new CQLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
			
//			IGenerator generator = (IGenerator) injector.getInstance(CQLGenerator.class);
////			generator.setNameProvider(new NameProvider());
//			ResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
//			((XtextResourceSet) resourceSet).addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
//			Resource resource = resourceSet.createResource(URI.createURI("dummy:/example.cql"));
//			CQLParser p = new CQLParser();

		} catch (IllegalAccessError e) {
			System.out.println("----------------------------------");
			System.out.println(e.getMessage());
			System.out.println("CAUSE: " + e.getCause());
			for (StackTraceElement t : e.getStackTrace()) {
				System.out.println(t.getFileName());
			}

			for (Throwable t : e.getSuppressed()) {
				System.out.println("----------------------------------");
				System.out.println(t.getCause().getLocalizedMessage());
			}
		} catch (NoClassDefFoundError e) {
			//do nothing
		} finally {
			System.out.println();
			System.out.println("DONE!");
		}
	}
}
