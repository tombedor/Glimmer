package svm_gp;


import java.util.Comparator;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thomasbedor
 */
public class Comp implements Comparator<ORF>{
    @Override
   /* public int compare(ORF o1, ORF o2) {
        String input1 = o1.seq;
        String input2 = o2.seq;
        if (input1.length() < input2.length()) {
        return 1;
        } else if (input1.length() > input2.length()) {
            return -1;
        } else {
            return 0;
    }
  }
    */
    public int compare(ORF o1, ORF o2) {
        int input1 = o1.start;
        int input2 = o2.start;
        if (input1 > input2) {
        return 1;
        } else if (input1 < input2) {
            return -1;
        } else {
            return 0;
    }
  }
}
