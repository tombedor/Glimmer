/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm_gp;

import java.io.FileNotFoundException;

/**
 *
 * @author thomasbedor
 */
@SuppressWarnings("unchecked")
public class SVM_GP {
  
    /**
     * @param args the command line arguments
     */
    //@SuppressWarnings("unchecked")
    public static void main(String[] args) throws FileNotFoundException, Exception {
        sOP op = new sOP();
               System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
        System.out.print("Loading sequences.\n");
        Sequence S = new Sequence("dna.fna");
        Sequence R = new Sequence(S.seq);
        op.forwardAndReverse(S, "outForward.fna");
        try{
        op.forwardAndReverse(R, "outReverse.fna");
        }catch(Exception e){
            System.out.println("There are no genes predicted for this sequence.");
        }
        
       
        // TODO code application logic here
        
     
        
    }
    
}
