pdflatex Anforderungen.tex
makeindex -s abtisstud/abtisstud.ist Anforderungen.idx
bibtex Anforderungen
pdflatex Anforderungen.tex
pdflatex Anforderungen.tex
DEL *.aux *.bbl *.blg *.glo *.idx *.log *.toc *.ind *.lof *.ilg *.out