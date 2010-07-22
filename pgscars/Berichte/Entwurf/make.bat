pdflatex Entwurf.tex
makeindex -s abtisstud/abtisstud.ist Entwurf.idx
bibtex Entwurf
pdflatex Entwurf.tex
pdflatex Entwurf.tex
DEL *.aux *.bbl *.blg *.glo *.idx *.log *.toc *.ind *.lof *.ilg *.out