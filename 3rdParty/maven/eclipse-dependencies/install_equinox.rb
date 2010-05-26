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
    file = file.split("_")
    name = file[0]
    version = file[1...file.length].join("_")
    if name && version
      name = name.split(".")
      version = version.split(".")
      if version[-1] == "jar"
        version = version[0...version.length-1]
        group = name[0,3]
        if name.length < 4
          artifact = name[2...name.length]
        else
          artifact = name[3...name.length]
        end
        puts group.join(".")
        puts artifact.join(".")
        puts version.join(".")
        puts `mvn install:install-file -Dfile=#{f} -DgroupId=#{group.join(".")} -DartifactId=#{artifact.join(".")} -Dversion=#{version.join(".")} -Dpackaging=jar -DgeneratePom=true`
      end
    end
  end
}

