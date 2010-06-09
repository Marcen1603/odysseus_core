#!/usr/bin/env ruby

class ProjectDependencyGenerator
public
	attr_reader :projectDependencies, :unresolvedDependencies
	
	def parse(dir)
		@packages = Hash.new
		@manifestImports =  Hash.new
		@unresolvedDependencies = Hash.new
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
					next
				end
				bundles << bundle
				
			end
			#puts "unknown import for project #{project}: #{unknownDependencies.join(', ')}"
			@projectDependencies[project] = bundles.uniq
			@unresolvedDependencies[project] = unknownDependencies
		end
	end
	
	def getPackages(project, prefix, dir)
		Dir.entries(prefix + "/" + dir).each do 
			|curDirName|
			next if (curDirName[0] ==  46) #46 = '.'
			curPackage = dir.empty? ? curDirName : dir + "/" + curDirName
			curDir = prefix + "/" + curPackage
			if File.stat(curDir).directory? then
				package = curPackage.gsub(/\//, '.')
				@packages[package] = project
				getPackages(project, prefix, curPackage)
			end
		end
	end
	
	def findSrc(path, lastdir)
		Dir.entries(path).each do 
			|curDir|
			next if (curDir[0] == 46) #46 = '.'
			curPath = path + "/" + curDir
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
		end
	end
	
	def importManifest(project, path)
		file = File.new(path, 'r')
		while not file.eof?
			line = file.readline
			if /^Import-Package:/ =~ line
				@manifestImports[project] = Array.new
				addImport(project, line[15, line.length-1])
				while not file.eof?
					line = file.readline
					return if /^.+:/ =~ line
					addImport(project, line)
				end
			end
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

pdg = ProjectDependencyGenerator.new
pdg.parse path

#hier kommst du ...
if pdg.projectDependencies[bundle]
	puts "<dependencies>"
	pdg.projectDependencies[bundle].each {|v|
		artifact = v.split(".")
		artifact = "odysseus-" + artifact[5..artifact.length].join("-")
		puts "<dependency>"
		puts "<groupId>${project.groupId}</groupId>"
		puts "<artifactId>#{artifact}</artifactId>"
		puts "<version>${project.version}</version>"
       		 puts "</dependency>"
	}
	puts "</dependencies>"
end
#pdg.projectDependencies = hashmap von bundle => <dependencies des bundles>
#pdg.unresolvedDependencies = hashmap von bundle => <dependencies des bundles, die nicht aufgeloest werden konnten (kein src-projekt im svn sind)>
