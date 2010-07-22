pdflatex Impedement.tex
makeindex -s abtisstud/abtisstud.ist Impedement.idx
pdflatex Impedement.tex
pdflatex Impedement.tex
DEL *.aux *.bbl *.blg *.glo *.idx *.log *.toc *.ind *.lof *.ilg *.out