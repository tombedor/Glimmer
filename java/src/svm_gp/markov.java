/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm_gp;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

/**
 *
 * @author thomasbedor
 */
@SuppressWarnings("unchecked")

public class markov {
  
    HashMap kmers;
    int[][] keys;
    int[][] models;// = {one,two,three,four,five,six,seven};
    
    ArrayList modelsI;

    
    
 
    markov(){
        sOP o = new sOP();
        kmers = new HashMap();
        
        
        
        //initialize:
        String[] toExtend;
        String[] toAdd = {"A", "T", "C", "G"};
        int len = 1;
        do {
            //System.out.println("there are "+toAdd.length+" "+(len)+"mers");
            for (int i=0; i<toAdd.length; i++){
                kmers.put(toAdd[i], 0);
                
            }
            toExtend = toAdd;
            toAdd = o.kmerExtend(toExtend);
            len++;
            }   while (len < 10);
        //System.out.println("kmers size = "+kmers.size());
    }
    
    void kmerCount(ORF[] inlist){
        String frag;
        String input;
        int key;
        int[] ans = new int[349524];
        for (int x = 0; x<inlist.length; x++){
            input = inlist[x].seq;
            for (int i =0; i<input.length(); i++){
                for (int j = 1; j <10; j++){
                    if (i+j <= input.length()){
                        frag = input.substring(i,i+j);
                        //System.out.println("found kmer "+frag);
                        key = (int) kmers.get(frag);
                        key++;
                        kmers.remove(frag);
                        kmers.put(frag, key);
                        //System.out.println("score for "+frag+" ="+ans[key]);
                        
                    }
                }
                //System.out.println("looping");
            }
        }
    }
    

        double alpha(int k, int x, ORF orf){
            // Returns confidence in predictive power of kmer up to but not 
            //including x to predict x. x must be at least 1, and greater than k.
            
            
         //   System.out.println("calculating alpha for k= "+k+", x= "+x);
            
            //kmer up to but not including x.
            String predicted = orf.seq.substring(x-k,x+1);
            //Returns confidence measure as defined in 1998 Glimmer paper
            String context = orf.seq.substring(x-k,x);

            if (!kmers.containsKey(predicted)){System.out.println("kmers dictionary does not include "+predicted);}
            int predictedFreq = (int) kmers.get(predicted);
            if (predictedFreq > 399){
                return 1;
            }
            if (kmerSum(context) <50){
                return 0;
            }
            else{
                String shortened = context.substring(1,context.length());
                ChiSquareTest t = new ChiSquareTest();
                // Get an array of the indices of i+1 substrings:
                double[] fullLength = {(double)(int) kmers.get(context+"A")+.1, (double)(int) kmers.get(context+"T")+.1,
                (double)(int) kmers.get(context+"C")+.1,(double)(int) kmers.get(context+"G")+.1};
                // Get counts of these:
         
                long[] shortFreq = {(long)(int) kmers.get(shortened+"A"), (long)(int) kmers.get(shortened+"T"),
                (long)(int) kmers.get(shortened+"C"),(long)(int) kmers.get(shortened+"G")};
                
                //apply chi squared test to see if predictions with shorter string are more 

                double p = 1-t.chiSquareTest(fullLength,shortFreq);
                if (p<.5){
                    return 0;
                }
                else{
                    return (fullLength[0]+fullLength[1]+fullLength[2]+fullLength[3])*p/400;
                    
                }

            }
        }
        
        
 
        int kmerSum(String stub){
            //returns the frequency of the context string.
            int[] subs = {(int) kmers.get(stub + "A"),(int) kmers.get(stub + "T"),
            (int) kmers.get(stub + "C"),(int) kmers.get(stub + "G")};    
            int ans =  subs[0]+subs[1]+subs[2]+subs[3];
            //if (ans == 0){
              //  System.out.println();
                
            //}
            //System.out.println(ans);
            return ans;
        
        }
        double piSx (int k, int x, ORF orf){
           // System.out.println("calculating piSx for k= "+k+", x= "+x);
            
            String context = orf.seq.substring(x-k,x);
            String predicted = orf.seq.substring(x-k,x+1);
            //freq of input string:
            double nom = (double) (int) kmers.get(predicted);
            //System.out.println("piSX kmersum = "+ kmerSum(context)+" for x="+x+", k = "+k);
            return nom/(double) kmerSum(context);
            
        }
        
        double imm (int k, int x, ORF orf){
           // System.out.println("calculating imm for k= "+k+", x= "+x);
            //base case 0th order model.
            if (x == 0){
                String str = orf.seq.substring(x,x+1);
                System.out.println("XXXXX"+str);
                System.out.println("imm specialcase kmersum = "+ kmerSum(""));
                return (double)(int)kmers.get(str)/kmerSum("");
            }
            if (x-k <=0){
                k = x-1;
            }
            
            
            
            
            
            double alphaV = alpha(k,x,orf);
            if (alphaV == (double) 1){
                return alphaV*piSx(k,x,orf);
            }
            else if (alphaV == 0){
                return imm(k-1,x,orf);
            
            }
            else{
                return alphaV*piSx(k,x,orf)+(1-alphaV)*
                        imm(k-1,x,orf);
            }
        } 
        double immTotal (ORF orf){
            double sum = 0;
            for (int x = 1; x<orf.seq.length(); x++){
                sum += imm(8,x,orf);
            }
            return (double) sum;
        }
                
        
        
        
}
       
    
    
    
        
    

