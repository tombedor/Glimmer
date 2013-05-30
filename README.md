glimmer
=======

Prokaryotic gene prediction

Given a prokaryotic DNA sequence, predicts genes using GLIMMER algorithm

Implements a reasonably faithful implementation of the GLIMMER algorithm for prokaryotic
gene predict, as described in:

Delcher, Arthur L., et al. "Improved microbial gene identification with GLIMMER." Nucleic 
acids research 27.23 (1999): 4636-4641. 

The primary departure is that it does not take the frame positions of bases into account.


Run Instructions:

Example input data is found in dna.fna. Data input and output are in this format.

Run svm_gp.java. The program assumes it is being initialized at "Gene Predict/"

Note that the program makes use of the Apache Commons Math library, 
in order to implement the chi square test.
