#!/usr/bin/env ruby

class ProjectDependencyGenerator
public
	attr_reader :projectDependencies, :unresolvedDependencies, :unknownDepAr, :externalDependencies

	def parse(dir)
		@packages = Hash.new
      @manifestImports =  Hash.new
      @unresolvedDependencies = Hash.new
      @unknownDepAr = Array.new
      @externalDependencies = {
        "org.osgi.framework" => "<dependency><groupId>org.osgi</groupId><artifactId>org.osgi.core</artifactId><version>4.2.0</version></dependency>",
		"org.osgi.service.component" => "<dependency><groupId>org.osgi</groupId><artifactId>org.osgi.core</artifactId><version>4.2.0</version></dependency>",
	"org.osgi.util.tracker" => "<dependency><groupId>org.osgi</groupId><artifactId>org.osgi.core</artifactId><version>4.2.0</version></dependency>",
"org.osgi.service.framework" => "<dependency><groupId>org.osgi</groupId><artifactId>org.osgi.core</artifactId><version>4.2.0</version></dependency>",
"org.simpleframework.xml" => "<dependency><groupId>org.simpleframework</groupId><artifactId>simple-xml</artifactId><version>2.3.5</version></dependency>",
"org.simpleframework.xml.core" => "<dependency><groupId>org.simpleframework</groupId><artifactId>simple-xml</artifactId><version>2.3.5</version></dependency>",
"org.apache.commons.math" => "<dependency><groupId>org.apache.commons</groupId><artifactId>commons-math</artifactId><version>2.1</version></dependency>",
"org.apache.commons.math.linear" => "<dependency><groupId>org.apache.commons</groupId><artifactId>commons-math</artifactId><version>2.1</version></dependency>",
"org.slf4j" => "<dependency><groupId>org.slf4j</groupId><artifactId>com.springsource.slf4j.api</artifactId></dependency>",
"net.jxta" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.discovery" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.endpoint" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.document" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.exception" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.id" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.impl.peer" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.peer" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.peergroup" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.pipe" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.platform" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.protocol" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.socket" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.util" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",

"net.jxta.access" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.codat" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.content" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.credential" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.id.jxta" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.logging" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.membership" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.meta" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.rendezvous" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.resolver" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.service" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",
"net.jxta.util.documentSerializable" => "<dependency><groupId>jxta</groupId><artifactId>jxta</artifactId><version>2.0</version></dependency>",






"com.Ostermiller.util" => "<dependency><groupId>org.ostermiller</groupId><artifactId>utils</artifactId><version>1.07.00</version></dependency>",

"org.eclipse.swt" => "<dependency><groupId>org.eclipse.swt</groupId><artifactId>${swt.artifactId}</artifactId><version>[3.5,)</version></dependency>",
"org.eclipse.swt.custom" => "<dependency><groupId>org.eclipse.swt</groupId><artifactId>${swt.artifactId}</artifactId><version>[3.5,)</version></dependency>",
"org.eclipse.swt.events" => "<dependency><groupId>org.eclipse.swt</groupId><artifactId>${swt.artifactId}</artifactId><version>[3.5,)</version></dependency>",
"org.eclipse.swt.graphics" => "<dependency><groupId>org.eclipse.swt</groupId><artifactId>${swt.artifactId}</artifactId><version>[3.5,)</version></dependency>",
"org.eclipse.swt.layout" => "<dependency><groupId>org.eclipse.swt</groupId><artifactId>${swt.artifactId}</artifactId><version>[3.5,)</version></dependency>",
"org.eclipse.swt.widgets" => "<dependency><groupId>org.eclipse.swt</groupId><artifactId>${swt.artifactId}</artifactId><version>[3.5,)</version></dependency>",

"org.eclipse.equinox.app" => "<dependency><groupId>org.eclipse.equinox</groupId><artifactId>app</artifactId></dependency>",
"org.eclipse.osgi.framework.console" => "<dependency><groupId>org.eclipse.osgi</groupId><artifactId>osgi</artifactId></dependency>",
"org.osgi.service.prefs" => "<dependency><groupId>org.osgi</groupId><artifactId>org.osgi.core</artifactId></dependency>",

"org.eclipse.ui.plugin" => "<dependency><groupId>org.eclipse</groupId><artifactId>eclipse-platform</artifactId><version>[3.5,)</version></dependency>",
"org.eclipse.core.runtime" => "<dependency><groupId>org.eclipse.core</groupId><artifactId>runtime</artifactId><version>[3.5,)</version></dependency>",
"org.eclipse.core.commands" => "<dependency><groupId>org.eclipse.core</groupId><artifactId>commands</artifactId><version>[3.5,)</version></dependency>",
"com.hp.hpl.jena.ontology" => "<dependency><groupId>com.hp.hpl.jena</groupId><artifactId>jena</artifactId><version>2.6.3</version></dependency>",
"com.hp.hpl.jena.rdf.model" => "<dependency><groupId>com.hp.hpl.jena</groupId><artifactId>jena</artifactId><version>2.6.3</version></dependency>",
"com.hp.hpl.jena.util" => "<dependency><groupId>com.hp.hpl.jena</groupId><artifactId>jena</artifactId><version>2.6.3</version></dependency>",
"com.hp.hpl.jena.util.iterator" => "<dependency><groupId>com.hp.hpl.jena</groupId><artifactId>jena</artifactId><version>2.6.3</version></dependency>",

"org.openrdf" => "<dependency>    <groupId>org.openrdf</groupId>    <artifactId>sesame</artifactId>    <version>1.2.7</version></dependency>",


"org.slf4j.impl" => "<dependency><groupId>org.slf4j</groupId><artifactId>com.springsource.slf4j.log4j</artifactId></dependency>",
        "org.apache.log4j" => "<dependency>        <groupId>org.apache.log4j</groupId>        <artifactId>com.springsource.org.apache.log4j</artifactId></dependency>",
        "org.apache.log4j.spi" => "<dependency>        <groupId>org.apache.log4j</groupId>        <artifactId>com.springsource.org.apache.log4j</artifactId></dependency>",


"org.apache.cxf.binding" => "<dependency>    <groupId>org.apache.cxf</groupId>    <artifactId>cxf-bundle</artifactId>    <version>2.2.9</version></dependency>",
"org.apache.cxf.endpoint" => "<dependency>    <groupId>org.apache.cxf</groupId>    <artifactId>cxf-bundle</artifactId>    <version>2.2.9</version></dependency>",
"org.apache.cxf.endpoint.dynamic" => "<dependency>    <groupId>org.apache.cxf</groupId>    <artifactId>cxf-bundle</artifactId>    <version>2.2.9</version></dependency>",
"org.apache.cxf.jaxws.endpoint.dynamic" => "<dependency>    <groupId>org.apache.cxf</groupId>    <artifactId>cxf-bundle</artifactId>    <version>2.2.9</version></dependency>",
"org.apache.cxf.service" => "<dependency>    <groupId>org.apache.cxf</groupId>    <artifactId>cxf-bundle</artifactId>    <version>2.2.9</version></dependency>",
"org.apache.cxf.service.model" => "<dependency>    <groupId>org.apache.cxf</groupId>    <artifactId>cxf-bundle</artifactId>    <version>2.2.9</version></dependency>",
        "org.junit" => "      <dependency>            <groupId>junit</groupId>            <artifactId>junit</artifactId><scope>compile</scope></dependency>"}

        findSrc(dir, "")
		buildDependencies
		return @projectDependencies
	end

private
	def buildDependencies
		@projectDependencies = Hash.new
		@manifestImports.each do |project, packages|
			bundles = Array.new
			unknownDependencies = Array.new
			packages.each do |package|
				bundle = @packages[package]
				if bundle.nil?
					unknownDependencies << package
					unknownDepAr << package
					next
				end
				next if bundle == project
				bundles << bundle

			end
			#puts "unknown import for project #{project}: #{unknownDependencies.join(', ')}"
			@projectDependencies[project] = bundles.uniq
			@unknownDepAr.uniq!
			@unresolvedDependencies[project] = unknownDependencies
		end
	end

	def getPackages(project, prefix, dir)
		Dir.entries(prefix + "/" + dir).each do
			|curDirName|
			next if (/^\./ =~ curDirName) #46 = '.'curDirName[0] ==  46) #46 = '.'
			curPackage = dir.empty? ? curDirName : dir + "/" + curDirName
			curDir = prefix + "/" + curPackage
			begin
			if File.stat(curDir).directory? then
				package = curPackage.gsub(/\//, '.')
				@packages[package] = project
				getPackages(project, prefix, curPackage)
			end
			rescue 
			puts "error"
			end
		end
	end

	def findSrc(path, lastdir)
		Dir.entries(path).each do
			|curDir|
			#puts curDir
			next if (/^\./ =~ curDir) #46 = '.'
			curPath = path + "/" + curDir
			begin
			if File.stat(curPath).directory? then
				if curDir.downcase == "src" then
					manifest = path + "/META-INF/MANIFEST.MF"
					next unless File.exist?(manifest)
					project = lastdir.gsub(/\//, '.')
					importManifest(project, manifest)
					getPackages(project, curPath, "")
				else
					findSrc(curPath, curDir)
				end
			end
			rescue
			puts "error"
			end
		end
	end

	def importManifest(project, path)
		file = File.new(path, 'r')
		@manifestImports[project] = Array.new if @manifestImports[project].nil?
		while not file.eof?
			line = file.readline
			if /^Require-Bundle:/ =~ line
			  addImports file, project, line[15, line.length-1]
			end
			if /^Fragment-Host:/ =~ line
			  addImport project, line[14, line.length-1]
			end
			if /^Import-Package:/ =~ line
				addImports file, project, line[15, line.length-1]
			end
		end
	end
	def addImports file, project, line
	  addImport(project, line[15, line.length-1])
				while not file.eof?
					line = file.readline
					return if /^[^;]+:/ =~ line
					addImport(project, line)
				end
	end
	def addImport(project, line)
		match = (/\w+(\.(\w|_)+)*/.match(line))
		unless match.nil?
			@manifestImports[project] << match[0]
			pos = line.index(',', match.end(0))
			addImport(project, line[pos,line.length-1]) unless pos.nil?
		end
	end

end

if ARGV.length != 2 then
	puts "usage: createpoms.rb <svn project directory> <bundle>"
	exit -1
end

path = ARGV[0]
bundle = ARGV[1]
if not File.stat(path).directory? then
	puts "no such directory: " + path
	exit -1
end

dumpfile = path + "/dependencies"
if not File.exists?(dumpfile) then
  pdg = ProjectDependencyGenerator.new
  pdg.parse path
  Marshal.dump(pdg, File.new(dumpfile,"w+"))
else
  pdg = Marshal.load(File.new(dumpfile))
end

#pdg.unknownDepAr.each{|pkg| puts pkg}
#hier kommst du ...
if pdg.projectDependencies[bundle]
	pdg.projectDependencies[bundle].each {|v|
		artifact = v.split(".")
		artifact = "odysseus-" + artifact[5..artifact.length].join("-")
		puts "<dependency>"
		puts "<groupId>${project.groupId}</groupId>"
		puts "<artifactId>#{artifact}</artifactId>"
		puts "<version>${project.version}</version>"
       		 puts "</dependency>"
	}
  tmp = Array.new
  pdg.unresolvedDependencies[bundle].each{|v|
dep = pdg.externalDependencies[v]
	#puts "unresolved #{v}" if dep.nil?
    tmp << dep unless dep.nil?
}
  tmp.uniq.each{|v|
    puts v
  }
end
#pdg.projectDependencies = hashmap von bundle => <dependencies des bundles>
#pdg.unresolvedDependencies = hashmap von bundle => <dependencies des bundles, die nicht aufgeloest werden konnten (kein src-projekt im svn sind)>
