/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm_gp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author thomasbedor
 */
@SuppressWarnings("unchecked")

public class sOP {
    String[] triNuc;
    String[] Codons;
    sOP(){}
    
    boolean isStop(String input) throws Exception{
        if (input.length() != 3){
            throw new Exception("Codons must have length 3");
        }
        
        if (input.equals("TAG") | input.equals("TAA") | input.equals("TGA")){
            return true;
        }
        else{return false;}
        
    }
    boolean isStart(String input) throws Exception{
        if (input.length() != 3){
            throw new Exception("Codons must have length 3");
        }
        if (input.equals("ATG") | input.equals("GTG") | input.equals("TTG")){
            return true;
        }
        else{return false;}
    }

   
    ORF[] orfPrune(ORF[] input){
        ORF big;
        ORF small;
        int sub = 0;
        Arrays.sort(input, new Comp());
        System.out.println("\tChecking "+input.length+" ORFs");
        for (int i=0; i<input.length-1; i++){
            if (i%10000==0){
                System.out.println("\tChecked "+i+" of "+input.length);
            }
            for (int j=i+1; j<input.length; j++){
                big = input[i];
                small = input[j];
                if (big.start < small.start && big.stop > small.stop && !input[j].seq.equals("X")){
                    //System.out.println("\n Sequence from "+small.start+"to "
                      //      +small.stop+"contained in sequence from "+big.start+
                        //    "to "+big.stop);
                    input[j].seq = "X";
                    sub += 1;
                
                }
                
            }
        
        
        }
        //System.out.println(input.length);
        //System.out.println("\n subtracted = "+sub);
        ORF[] output = new ORF[input.length - sub];
        int index = 0;
        for (int i =0; i<input.length; i++){
            if (!input[i].seq.equals("X")){
                output[index] = input[i];
                //System.out.println("orf of length "+input[i].seq.length());
                index++;
            }
           
        
        
        }
        return output;
    }
    ORF[] trainSet (ORF[] input, int thresh){
        ArrayList<ORF> train = new ArrayList<>();
        for (int i = 0; i< input.length; i++){
            if (input[i].seq.length() > thresh){
                train.add(input[i]);
            }
        }
        ORF[] ans = new ORF[train.size()];
        for (int i=0; i<train.size(); i++){
            ans[i] = (ORF) train.get(i);
        
        }
        return ans;
    }
    ORF[] testSet (ORF[] input, int thresh){
        ArrayList<ORF> test = new ArrayList<>();
        for (int i = 0; i< input.length; i++){
            if (input[i].seq.length() < thresh){
                test.add(input[i]);
            }
        }
        ORF[] ans = new ORF[test.size()];
        for (int i=0; i<test.size(); i++){
            ans[i] = (ORF) test.get(i);
        
        }
        return ans;
    }
    String[] kmerExtend(String[] input){
        String[] ans = new String[input.length*4];
            String[] bases = {"A","T","C","G"};
            String add;
            int count = 0;
            for (int j = 0; j< input.length; j++){
                for (int i = 0; i<4; i++){
                    add = input[j]+bases[i];
                    ans[count] = add;
                    //System.out.println(add);
                    count++;
                }
            }
        return ans;
    }
    
    void hmmPredict(markov train, markov test, ORF[] input) throws Exception{
        double gene;
        double notGene;
        int count = 0;
        System.out.println("Checking "+input.length+" ORFs");
        for (int i =0; i<input.length; i++){
            gene = train.immTotal(input[i]);
            notGene = test.immTotal(input[i]);
            //System.out.println("ORF of length: "+input[i].seq.length()+":\nGene score = "+gene+"\nnot gene score = "+notGene+"\n");
            
            if (gene > notGene){
                input[i].gene = true;
                count++;
               
            }
            if (i%5000==0){
                System.out.println("Checked "+i+" of "+input.length);
            }
        }
        double perc = (double) count*100 / (double) input.length;
        System.out.println("found "+count+" genes, "+perc+" percent");
        
    }
    
    
    void write(ORF[] train,ORF[] test, String path) throws IOException{
        String f = "";
        ArrayList<ORF> all = new ArrayList<>();
        for (int i = 0; i<train.length; i++){
            all.add(train[i]);
        }
        for (int i = 0; i<test.length; i++){
            if (test[i].gene){
                all.add(test[i]);
            }    
        }
        ORF[] ans = new ORF[all.size()];
        for (int i = 0; i<all.size(); i++){
            ans[i] = all.get(i);
        
        
        }
       
        Arrays.sort(ans, new Comp());
        for (int i = 0; i<ans.length; i++){
            f = f+ans[i].start+".."+ans[i].stop+"\n";
        
        
        }
        try{
            FileWriter fstream = new FileWriter(path);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(f);
            out.close();
        }catch (Exception e) {
            System.err.println("Error:" + e.getMessage());
        }
    }
    
    
    
    
    
    
    void verify(Sequence s) throws Exception{
        
        int[] starts = {337, 	2801, 	3734, 	5234, 	8238, 	9306, 	12163, 	14168, 	15445, 	16580, 	17489, 	18721, 	21407, 	22391, 	25207, 	25826, 	26277, 	27293, 	28374, 	29651, 	30817, 	34300, 	42403, 	43188, 	44180, 	45463, 	45807, 	47246, 	47769, 	49712, 	57364, 	58474, 	59121, 	70387, 	71351, 	77388, 	77621, 	84368, 	85630, 	87357, 	88028, 	89634, 	90094, 	91032, 	91413, 	93166, 	94650, 	96002, 	97087, 	98403, 	99644, 	100765, 	102233, 	103155, 	103982, 	105305, 	106557, 	107630, 	108279, 	111044, 	113444, 	118733, 	119281, 	122092, 	123017, 	125695, 	127912, 	131615, 	134388, 	137083, 	141431, 	142779, 	143702, 	144577, 	145081, 	146968, 	162105, 	164730, 	167484, 	169778, 	170575, 	171462, 	175107, 	176610, 	179237, 	180884, 	182463, 	189874, 	190857, 	191855, 	192872, 	193521, 	194906, 	195664, 	195785, 	196546, 	197928, 	200482, 	200971, 	202101, 	202560, 	203348, 	204493, 	205126, 	208621, 	209679, 	211877, 	212331, 	214291, 	214833, 	215269, 	222833, 	224958, 	229167, 	231143, 	231926, 	234816, 	236058, 	237335, 	240370, 	243543, 	244327, 	246835, 	247637, 	250192, 	250898, 	252005, 	252301, 	252787, 	253467, 	253702, 	255977, 	256527, 	257829, 	259612, 	260727, 	269502, 	269827, 	271240, 	272071, 	274383, 	274525, 	275939, 	281502, 	282425, 	284619, 	286013, 	290724, 	302215, 	313581, 	314515, 	314811, 	319451, 	320832, 	321562, 	322982, 	328687, 	331595, 	334504, 	335149, 	336002, 	337549, 	338993, 	339389, 	340349, 	342108, 	343400, 	345708, 	347906, 	349236, 	350439, 	351930, 	354146, 	355395, 	358023, 	358713, 	359189, 	367835, 	369501, 	370463, 	371339, 	372145, 	373092, 	374683, 	375996, 	380575, 	380898, 	384456, 	385431, 	386195, 	387019, 	389475, 	392245, 	393730, 	395863, 	397096, 	398817, 	400610, 	400971, 	402505};
        int[] stops = {2799, 	3733, 	5020, 	5530, 	9191, 	9893, 	14079, 	15298, 	16557, 	16720, 	18655, 	19620, 	22348, 	25207, 	25701, 	26275, 	27227, 	28207, 	29195, 	30799, 	34038, 	34695, 	43173, 	44129, 	45466, 	45750, 	47138, 	47776, 	49631, 	50302, 	58179, 	59124, 	59279, 	71265, 	72115, 	77519, 	78799, 	85312, 	87354, 	87848, 	89032, 	90092, 	91035, 	91397, 	93179, 	94653, 	96008, 	97084, 	98403, 	99647, 	100711, 	102240, 	103153, 	103985, 	105244, 	106456, 	107474, 	108217, 	110984, 	111433, 	114487, 	119284, 	120135, 	122856, 	125680, 	127587, 	129336, 	134212, 	134750, 	138633, 	141967, 	143705, 	144472, 	145017, 	146310, 	147870, 	164534, 	167264, 	169727, 	170575, 	171465, 	173444, 	176528, 	176954, 	180754, 	182308, 	183620, 	190599, 	191708, 	192580, 	193429, 	194717, 	195664, 	195816, 	196534, 	197898, 	200360, 	200967, 	201996, 	202556, 	203348, 	204496, 	205089, 	208608, 	209580, 	211820, 	212266, 	213629, 	214836, 	215255, 	215979, 	223408, 	225212, 	229970, 	231922, 	232549, 	235538, 	236798, 	238120, 	240816, 	244121, 	245094, 	247461, 	248134, 	250827, 	251953, 	252298, 	252699, 	253161, 	253733, 	254202, 	256435, 	257771, 	258230, 	260715, 	261980, 	269870, 	270978, 	271479, 	273216, 	274472, 	275952, 	276871, 	282410, 	284392, 	286001, 	287623, 	291455, 	302829, 	314468, 	314814, 	315677, 	320305, 	321551, 	322989, 	323677, 	330720, 	332683, 	335109, 	336012, 	337549, 	338967, 	339313, 	340339, 	341731, 	343157, 	344215, 	345983, 	348796, 	350405, 	351890, 	353816, 	355405, 	356678, 	358682, 	359183, 	360370, 	369499, 	370445, 	371329, 	372148, 	373095, 	374105, 	375894, 	376535, 	380940, 	381803, 	385418, 	386198, 	387022, 	387870, 	390935, 	393642, 	394353, 	397083, 	398190, 	399029, 	400870, 	402386, 	402825};
        for (int i = 0; i<starts.length; i++){
            int start = starts[i];
            int stop = stops[i];
            String sta = s.seq[start-1]+s.seq[start]+s.seq[start+1];
            String sto = s.seq[stop-3]+s.seq[stop-2]+s.seq[stop-1];
            
            System.out.println("is "+sta+" a start?\t"+isStart(sta));
            System.out.println("is "+sto+" a stop\t"+isStop(sto)+"\n");
        
        }
    
    }
    
    
    void forwardAndReverse(Sequence S, String outputPath) throws Exception{
        ORF[] cand;
        ORF[] test;
        ORF[] train;
        ORF[] back;
        sOP op = new sOP();
        System.out.print("Loading sequence.\n");
        
        
        //op.verify(S);
        //op.verify(R);
        
        System.out.print("Sequence loaded.\n");
        System.out.println("Generating candidate open reading frames.");
        cand = S.populateORF();
        System.out.println("Open reading frames generated.");
        System.out.println("Pruning overlapping ORFs.");
        cand= op.orfPrune(cand);
        System.out.println("Pruning done.");
        System.out.println("Creating training set.");
        test = op.testSet(cand, 1000);
        back = op.testSet(cand, 150);
        System.out.println("Creating training set.");
        train = op.trainSet(cand, 1000);
        markov gene = new markov();
        markov background = new markov();
        System.out.println("Calculating kmer scores. . .");
        gene.kmerCount(train);
        
        background.kmerCount(back);
        System.out.println("Training set:");
        op.hmmPredict(gene, background, train);
        System.out.println("Test set:");
        op.hmmPredict(gene, background, test);
        
        op.write(train, test, outputPath);
        
        
    
    
    
    
    }
    
}

