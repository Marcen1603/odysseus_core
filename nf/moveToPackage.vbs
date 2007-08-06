set args = WScript.Arguments
nargs = args.length


if nargs<>3 then
    MsgBox "Falsche Anzahl Parameter"
     
end if

set fs = CreateObject("Scripting.FileSystemObject")

set input = fs.OpenTextFile(args.item(0), 1, vbTrue)
set output = fs.CreateTextFile(args.item(1),vbTrue)

output.WriteLine args.item(2)
output.writeBlankLines(1)


Do While input.AtEndOfStream <> True
    line = input.ReadLine
        output.writeLine line   
Loop

input.close
output.close
