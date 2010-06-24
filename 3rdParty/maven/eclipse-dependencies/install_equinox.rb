#!/usr/bin/env ruby

require 'find'


Find.find(ENV["PWD"]) { |f|
  if FileTest.directory? f
    if File.basename(f)[0] == ?.
      Find.prune
    else
      next
    end
  else
    puts "-----------"
    file = File.basename(f)
    match = /(.+)_(\d+\.\d+.\d+).*/.match(file)
                      if match.nil?
			puts "error #{file}"
                      next
                      end
                      name = match[1]
                      version = match[2]
    nameAr = name.split(".")
                      
    group = nameAr[0...nameAr.length - 1].join(".")
                      artifactId=nameAr[-1]
                      
        puts group
        puts artifactId
        puts version
        puts `mvn install:install-file -Dfile=#{f} -DgroupId=#{group} -DartifactId=#{artifactId} -Dversion=#{version} -Dpackaging=jar -DgeneratePom=true`
  end
}

