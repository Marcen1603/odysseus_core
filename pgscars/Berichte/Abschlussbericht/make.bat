pdflatex Abschlussbericht.tex
makeindex -s abtisstud/abtisstud.ist Abschlussbericht.idx
bibtex Abschlussbericht
pdflatex Abschlussbericht.tex
pdflatex Abschlussbericht.tex
DEL *.aux *.bbl *.blg *.glo *.idx *.log *.toc *.ind *.lof *.ilg *.out