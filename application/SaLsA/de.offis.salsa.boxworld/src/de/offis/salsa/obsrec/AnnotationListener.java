package de.offis.salsa.obsrec;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

import de.offis.salsa.obsrec.annotations.ObjectRule;
import de.offis.salsa.obsrec.annotations.ScanSegmentation;
import de.offis.salsa.obsrec.objrules.IObjectRule;
import de.offis.salsa.obsrec.scansegm.IScanSegmentation;

class AnnotationListener implements ClassAnnotationDiscoveryListener {
	private Logger log = Logger.getLogger("Objektwelt.AnnotationListener");
	
	
	private Objectworld objektwelt;
	
	public AnnotationListener(Objectworld objektwelt) {
		this.objektwelt = objektwelt;
	}

	@Override
	public String[] supportedAnnotations() {
		return new String[]{
				ObjectRule.class.getName(),
				ScanSegmentation.class.getName()
				};
	}
	
	@Override
	public void discovered(String clazz, String annotation) {
		try {
			if(annotation.equals(ObjectRule.class.getName())){
				Object objRule = Class.forName(clazz).newInstance();
				Annotation a = objRule.getClass().getAnnotation(ObjectRule.class);
				String name = ((ObjectRule)a).name();
				objektwelt.registerObjectRule(name, (IObjectRule) objRule);
			} else if (annotation.equals(ScanSegmentation.class.getName())) {
				Object scanSeg = Class.forName(clazz).newInstance();
				Annotation a = scanSeg.getClass().getAnnotation(ScanSegmentation.class);
				String name = ((ScanSegmentation)a).name();
				objektwelt.registerScanSegmentation(name, (IScanSegmentation) scanSeg);
			}
			
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
