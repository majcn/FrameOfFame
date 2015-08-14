# original source: http://stuff.mit.edu/people/jcrost/latexmake.html

latexfile = thesis_template

all: pdf

pdf: $(latexfile).pdf

$(latexfile).bbl: $(latexfile).aux
	bibtex $(latexfile)

$(latexfile).aux: $(latexfile).tex
	pdflatex $(latexfile)

$(latexfile).pdf: $(latexfile).tex $(latexfile).bbl
	while ( pdflatex $(latexfile); grep -q "Rerun to get cross" $(latexfile).log ) do true ; done

clean:
	rm -f pdfa-1b.xmpi $(latexfile).aux $(latexfile).log $(latexfile).bbl $(latexfile).blg $(latexfile).out $(latexfile).toc $(latexfile).xmpdata
