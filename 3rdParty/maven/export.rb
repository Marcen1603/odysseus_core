#!/usr/bin/env ruby

filename = ARGV[0]
puts filename
return unless File.exists?(filename)

file = File.open(filename)

while !file.eof? do
  line = file.readline
  if /^Export-Package:/ =~ line
    line = line[15, line.length - 1]
    while !file.eof? && !(/^[^;]+:/ =~ line) do
      match = /\w+(\.(\w|_)+)*/.match(line)
      while !match.nil? do
	puts match[0]
	pos = line.index(',', match.end(0))
	break if pos.nil?
	match = /\w+(\.(\w|_)+)*/.match(line[pos, line.length - 1])
      end
      line = file.readline
    end
  end
end