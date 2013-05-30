/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm_gp;

/**
 *
 * @author thomasbedor
 */
class ORF {
    String seq;
    int start;
    int stop;
    boolean gene;
    int firstStartCodon;
    ORF(String in, int s1, int s2){
        seq = in;
        start = s1;
        stop = s2;
        gene = false;
        //System.out.println("seq is of length "+seq.length()+", from "+start+" to "+stop);
        
    
    
    }
 


    
}
