package org.drools.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.definitions.impl.KnowledgePackageImp;
import org.drools.rule.Package;
import org.drools.rule.builder.dialect.java.JavaDialectConfiguration;
import org.drools.rule.builder.dialect.mvel.MVELDialectConfiguration;
import org.drools.util.DroolsStreamUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Like the directory scanner, but this scans paths in osgi bundles instead of
 * filesystem directories for rule files. This has to be injected via a fragment
 * to org.drools.agent, because the abstract PackageProvider methods have
 * package visibility instead of protected.
 * 
 * @author Jonas Jacobi
 * @todo caching anstaendig auf bundleebene implementieren, sowie aenderungen
 *       wie hinzufuegen/entfernen von regeln feststellen
 */
public class OSGiPathScanner extends PackageProvider {

	private static final String LOG = "osgi.log";
	private static final String CLASS_LOADER = "osgi.class_loader";
	private static final String BUNDLE_CONTEXT = "osgi.bundle_context";
	private static final String OSGI = "OSGI";
	private String cache_directory;

	private String path;
	private BundleContext bundleContext;
	private ClassLoader classLoader;
	private PackageBuilderConfiguration packageBuilderConfig;
	private ArrayList<URL> urls;
	private Logger logger;
	private boolean isCachingEnabled;
	private Preferences pathConfig;

	@Override
	void configure(Properties config) {
		this.path = config.getProperty(OSGI);
		this.bundleContext = (BundleContext) config.get(BUNDLE_CONTEXT);
		this.classLoader = (ClassLoader) config.get(CLASS_LOADER);
		String log = config.getProperty(LOG, "drools");
		this.logger = LoggerFactory.getLogger(log);
		this.cache_directory = "cache" + "/" + path.replace('/', '_');

		ServiceReference sr = bundleContext
				.getServiceReference(PreferencesService.class.getName());
		this.isCachingEnabled = false;
		if (sr != null) {
			PreferencesService preferencesService = (PreferencesService) bundleContext
					.getService(sr);
			if (preferencesService != null) {
				Preferences preferences = preferencesService
						.getSystemPreferences();
				Preferences cachingConfig = preferences.node("caching");
				isCachingEnabled = cachingConfig.getBoolean("enabled", true);
				if (isCachingEnabled) {
					this.pathConfig = cachingConfig.node(this.path.replace('/',
							'_'));
				}

				File file = bundleContext.getDataFile(cache_directory);
				try {
					if (file == null || (file.exists()) && !file.isDirectory()) {
						throw new Exception();
					}
					if (!file.exists() && !file.mkdirs()) {
						throw new Exception();
					}
				} catch (Exception e) {
					logger.warn("unable to create caching directory: "
							+ file.getName());
					this.isCachingEnabled = false;
				}
			}
		}

		JavaDialectConfiguration javaDialectConfig = new JavaDialectConfiguration();
		javaDialectConfig.setCompiler(JavaDialectConfiguration.ECLIPSE);
		this.packageBuilderConfig = new PackageBuilderConfiguration(
				this.classLoader);
		// MVEL config is needed, because for some kind of complex predicates in
		// "when clauses" Drools seems to translate them to some kind of
		// mvelexpressions and if there is no config supplied, this results in a
		// NullPointerException
		MVELDialectConfiguration mvelConfig = new MVELDialectConfiguration();
		this.packageBuilderConfig.setDialectConfiguration("mvel", mvelConfig);
		mvelConfig.init(packageBuilderConfig);
		this.packageBuilderConfig.setDialectConfiguration("java",
				javaDialectConfig);
		this.packageBuilderConfig.setDefaultDialect("java");
		javaDialectConfig.init(packageBuilderConfig);
		this.urls = new ArrayList<URL>();
	}

	@SuppressWarnings("unchecked")
	@Override
	PackageChangeInfo loadPackageChanges() {
		PackageChangeInfo info = new PackageChangeInfo();

		PackageBuilder topBuilder = new PackageBuilder(
				this.packageBuilderConfig);

		Enumeration<URL> pkgEntries = bundleContext.getBundle().findEntries(
				path, "*.pkg", false);
		if (pkgEntries != null) {
			while (pkgEntries.hasMoreElements()) {
				URL nextPkg = pkgEntries.nextElement();
				if (this.urls.contains(nextPkg)) {
					// urlsToRemove.remove(nextPkg.toString());
					continue;
				}
				Object obj;
				try {
					obj = DroolsStreamUtils.streamIn(nextPkg.openStream());
					if (obj instanceof KnowledgePackageImp) {
						info.addPackage(((KnowledgePackageImp) obj).pkg);
					} else {
						info.addPackage((org.drools.rule.Package) obj);
					}
					this.urls.add(nextPkg);
					logger.info("binary rule loaded: " + nextPkg);
				} catch (Exception e) {
					logger.warn("could not load " + nextPkg.toString(), e);
				}
			}
		}

		Enumeration<URL> drlEntries = bundleContext.getBundle().findEntries(
				path, "*.drl", false);
		if (drlEntries != null) {
			while (drlEntries.hasMoreElements()) {
				URL nextDrl = drlEntries.nextElement();

				if (this.urls.contains(nextDrl)) {
					// urlsToRemove.remove(nextDrl.toString());
					continue;
				}
				try {
					PackageBuilder builder = new PackageBuilder(
							this.packageBuilderConfig);

					BundleEntryInfo entryInfo = bundleEntryInfo(nextDrl);
					boolean loadFromCache = false;
					if (this.isCachingEnabled
							&& this.pathConfig.getBoolean(entryInfo.toString(),
									false)) {
						try {
							File file = entryInfo.toCacheFile();
							Object obj = DroolsStreamUtils
									.streamIn(new FileInputStream(file),
											this.classLoader);
							builder.addPackage((Package) obj);
							topBuilder.addPackage(builder.getPackage());
							logger.info("rule file loaded from cache: "
									+ nextDrl);
							loadFromCache = true;
						} catch (FileNotFoundException e) {
							//cache file isn't created yet - first run (after a -clean start) 
						} catch (Exception e) {
							logger.warn("could not load cache entry for: "
									+ nextDrl + "; " + e.getMessage());
						}
					}

					if (!loadFromCache) {
						builder.addPackageFromDrl(new InputStreamReader(nextDrl
								.openStream()));
						if (builder.hasErrors()) {
							logger.warn("could not load rule file: " + nextDrl
									+ "\n\t" + builder.getErrors().toString());
						} else {
							topBuilder.addPackage(builder.getPackage());
							this.urls.add(nextDrl);
							logger.info("rule file loaded: " + nextDrl);
							if (this.isCachingEnabled) {
								try {
									File file = entryInfo.toCacheFile();
									DroolsStreamUtils.streamOut(
											new FileOutputStream(file), builder
													.getPackage());
									this.pathConfig.putBoolean(entryInfo
											.toString(), true);
								} catch (Exception e) {
									logger.warn("could not cache: " + nextDrl,
											e);
								}
							}
						}
					}
				} catch (Throwable e) {
					logger.warn("could not load " + nextDrl.toString(), e);
				}
			}
		}

		Enumeration<URL> rfEntries = bundleContext.getBundle().findEntries(
				path, "*.rf", false);
		if (rfEntries != null) {
			while (rfEntries.hasMoreElements()) {
				URL nextRf = rfEntries.nextElement();
				if (this.urls.contains(nextRf)) {
					// urlsToRemove.remove(nextRf.toString());
					continue;
				}
				try {
					PackageBuilder builder = new PackageBuilder(
							this.packageBuilderConfig);
					builder.addRuleFlow(new InputStreamReader(nextRf
							.openStream()));
					topBuilder.addPackage(builder.getPackage());
					this.urls.add(nextRf);
					if (builder.hasErrors()) {
						logger.warn("could not load rule flow file: " + nextRf
								+ "\n\t" + builder.getErrors().toString());
					} else {
						logger.info("rule flow loaded: " + nextRf);
					}
				} catch (Throwable e) {
					logger.warn("could not load " + nextRf.toString(), e);
				}
			}
		}
		for (Package pkg : topBuilder.getPackages()) {
			info.addPackage(pkg);
		}
		if (isCachingEnabled) {
			try {
				this.pathConfig.flush();
			} catch (Exception e) {
				logger.warn("could not create cache");
			}
		}
		// info.addRemovedPackages(topackagenames(urlsToRemove));

		return info;
	}

	private BundleEntryInfo bundleEntryInfo(URL url) {
		// bundle urls have the following format
		// bundleentry://<bundleid>.<unknown>/<pathname>
		int bundleId = Integer.parseInt(url.getHost().substring(0,
				url.getHost().indexOf('.')));
		Bundle bundle = this.bundleContext.getBundle(bundleId);
		return new BundleEntryInfo(bundle.getSymbolicName(), bundle
				.getVersion(), url.getPath());
	}

	private final class BundleEntryInfo {
		private String name;
		private Version version;
		private String pathname;

		public BundleEntryInfo(String bundleName, Version version,
				String pathname) {
			this.name = bundleName;
			this.version = version;
			this.pathname = pathname;
		}

		@Override
		public boolean equals(Object obj) {
			BundleEntryInfo info = (BundleEntryInfo) obj;
			return info.name.equals(this.name)
					&& info.version.equals(this.version)
					&& this.pathname.equals(info.pathname);
		}

		@Override
		public int hashCode() {
			return 113 * this.pathname.hashCode() + 17 * this.name.hashCode()
					+ 5 * this.version.hashCode();
		}

		public File toCacheFile() {
			return bundleContext.getDataFile(this.toString());
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder(cache_directory);
			builder.append('/');
			builder.append(name);
			builder.append('_');
			builder.append(version);
			builder.append(pathname.replaceAll("/", "_"));
			builder.append(".pkg");
			return builder.toString();
		}
	}

}
