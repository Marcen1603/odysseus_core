pdflatex Product.tex
makeindex -s abtisstud/abtisstud.ist Product.idx
pdflatex Product.tex
pdflatex Product.tex
DEL *.aux *.bbl *.blg *.glo *.idx *.log *.toc *.ind *.lof *.ilg *.out