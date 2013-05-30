/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm_gp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author thomasbedor
 */
@SuppressWarnings("unchecked")
public class Sequence {
    
    //Sequence. Int sequence assignments: A=1, T=3,C=5, G=7.
    
    String[] openRF;
    int[][] orf;  
    String input;
    String[] seq;
    int[] intSeq;
    int length;
    
    ArrayList<ORF> train;
    ArrayList trainI;
    ArrayList test;
    ArrayList testI;
    
   
    //String[] openRFs;
    public Sequence(String path) throws FileNotFoundException{
        input = path;
        Scanner s = null;  
        length = 0;
        /*
         * Populate forward sequences
         */
        //System.out.println("determining sequence length. . . . ");
        try {
            s = new Scanner(new BufferedReader(new FileReader(input)));
            s.useDelimiter("");
            while (s.hasNext()){
                String x = s.next();
                //Filters out new lines and header:
                if (x.equals("A") | x.equals("T") | x.equals("C") | x.equals("G")){
                    length +=1;
                }
            }
        }
            finally { 
            if (s != null) {
                s.close();
            }
        }
        //System.out.println("creating space for sequence");
        seq = new String[length]; 
        System.out.println("populating sequence");
        int seqIndex = 0;
        try {
            s = new Scanner(new BufferedReader(new FileReader(input)));
            s.useDelimiter("");
            while (s.hasNext()){
                String x = s.next();
                //Filters out new lines and header:
                if (x.equals("A") | x.equals("T") | x.equals("C") | x.equals("G")){
                    seq[seqIndex] = x;
                    seqIndex +=1;
                }
            }
        }
            finally { 
            if (s != null) {
                s.close();
            }
        }
    }
    Sequence(String[] forw){
    /*creates the reverse compliment sequence.
     * 
     */
        length = forw.length;
        seq = new String[length];
        for (int i =0; i < length; i++){
            seq[i] = forw[length-1-i];
        }
    }

    ORF[] populateORF() throws Exception{
        sOP o = new sOP();
        int pos;
        String orfT;
        String codon;
        int tempStart;
        ORF x;
        ArrayList<ORF> orfCollect = new ArrayList<>(); 
        for (int s = 0; s<3; s++){
            System.out.print("Reading frame "+s+" of 3\n");
            pos = s;
            orfT = "";
            tempStart = pos;
            codon = "";
            while (pos < length-3){
                codon = seq[pos]+seq[pos+1]+seq[pos+2];
                orfT = orfT + codon;
                if (o.isStop(codon)){
                    x = new ORF(orfT, tempStart, pos+3);
                    orfCollect.add(x);
                    orfT = "";
                    tempStart = pos+3;
                }
                pos += 3;
                
            
            
            }
            //if last codon was not stop codon, add last orf.
            if (!o.isStop(codon)){
                x = new ORF(orfT, tempStart, pos+3);
                orfCollect.add(x);
            }
        }
        ORF[] ans = new ORF[orfCollect.size()];
        for (int i=0; i<orfCollect.size(); i++){
            ans[i] = (ORF) orfCollect.get(i);
        
        }
        
        return ans;
     
    }
    
 
    
}
    

