SHELL=/bin/bash

BASEFILE="Impedement"

doitall:
	pdflatex $(BASEFILE)
	makeindex -s abtisstud/abtisstud.ist Impedement.idx
	pdflatex $(BASEFILE)
	pdflatex $(BASEFILE)

clean:
	for f in aux bbl blg glo idx log toc ind lof ilg out; do \
	if [ -z "$$(ls *.$$f)" ]; then true; else rm *.$$f; fi \
	done

