# original source: http://stuff.mit.edu/people/jcrost/latexmake.html

latexfile = master

all: pdf

pdf: $(latexfile).pdf

$(latexfile).bbl: $(latexfile).aux
	bibtex $(latexfile)

$(latexfile).aux: $(latexfile).tex
	pdflatex $(latexfile)

$(latexfile).pdf: $(latexfile).tex $(latexfile).bbl
	while ( pdflatex $(latexfile); grep -q "Rerun to get cross" $(latexfile).log ) do true ; done

clean:
	rm -f $(latexfile).blg $(latexfile).aux $(latexfile).bbl $(latexfile).log $(latexfile).out $(latexfile)-blx.bib $(latexfile).run.xml $(latexfile).toc
