/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hitesh
 */
package final_dissertation;

import java.util.HashMap;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

public class Starter {

    public static Sentences productSentences[] = new Sentences[4000];
    public static Sentences genericSentences[] = new Sentences[4000];
    
    public static HashMap<String,Noun_Word> productnouns = new HashMap<>(); 
    public static HashMap<String,Noun_Word> genericnouns = new HashMap<>(); 
    //public static HashMap<String,Noun_Word> product_newnouns = new HashMap<>(); 
    //public static HashMap<String,Noun_Word> generic_newnouns = new HashMap<>(); 
    
    public static String productFileInput = "CanonG3.txt";
    public static String productFileOutput = "CanonG3OutPut.xml";
    public static final String genericFileInput = "DiaperChamp.txt";
    public static final String genericFileOutput = "DiaperChampOutPut.xml";
    
    public static HashMap<Integer, String> productSentence = new HashMap<>();
    public static HashMap<Integer, String> genericSentence = new HashMap<>();
    
    public static HashMap<String, String> negation = new HashMap<>();
    public static HashMap<String,String> frequentFeature = new HashMap<>();
    public static HashMap<String, NounGroup> productNounGroup = new HashMap<>(); 
    
    public static String positive = "positive-words.txt";
    public static String negative = "negative-words.txt";
    public static final HashMap<String, Integer> dictionary = new HashMap<>();
    public static final HashMap<String, WordPolarity> featureRecord = new HashMap<>();
    public static final HashMap<String, WordPolarity> polarity = new HashMap<>();
    public static final HashMap<String, WordPolarity> wordLabel = new HashMap<>();
    public static HashMap<String, Cluster> positiveFeature = new HashMap<>();
    public static HashMap<String, Cluster> negativeFeature = new HashMap<>();
    
    public static Final_Opinion_Polarity result = new Final_Opinion_Polarity();
    
    public static HashMap<Integer, Final_Opinion_Polarity> rev_Hash = new HashMap<>();
    
    public static HashMap<String,String> ff = new HashMap<>();
    
    public static void main(String[] args) throws Exception 
    {
                MaxentTagger mt = new MaxentTagger("E:\\Disertation\\APIs\\stanford-postagger-full-2014-01-04\\models\\english-left3words-distsim.tagger");
                System.out.println(productFileInput+ " output with :"+genericFileInput);
                preprocess(productFileInput);
                misspell(productFileInput);
		POStag.TextToXml(mt, productFileInput, productFileOutput, productSentence);
               
                preprocess(genericFileInput);
                misspell(genericFileInput);
                POStag.TextToXml(mt, genericFileInput, genericFileOutput, genericSentence);
                
                negationWord();
                
                Feature_Parsing.parsefeature(productSentences, productnouns, productFileOutput, negation);
                float productWordCount = WordCount.wordCount(productFileInput);
                Feature_Parsing.parsefeature(genericSentences, genericnouns, genericFileOutput, negation);
		float genericWordCount = WordCount.wordCount(genericFileInput);
               
               // Feature_Parsing.shared_word_finding(productnouns, product_newnouns);
               // Feature_Parsing.shared_word_finding(genericnouns,generic_newnouns);
                /*
                int i=0;
                while(productSentences[i]!=null)
                {
                    int j=0;
                    System.out.println("REview id: "+ productSentences[i].reviewID);
                    System.out.println("Sentence "+i+" conj: ");
                    while(productSentences[i].conjunction[j]!=null)
                    {
                        System.out.print("Id: "+productSentences[i].posConjunction[j]+" ");
                        System.out.println(productSentences[i].conjunction[j]);
                        j++;
                    }
                    j=0;
                    System.out.println("Sentence "+i+" adjectives: ");
                    while(productSentences[i].negation[j]!=null)
                    {
                        System.out.print("id: "+productSentences[i].posNegation[j]+" ");
                        System.out.println(productSentences[i].negation[j]);
                        j++;
                    }
                    j=0;
                    System.out.println("Sentence "+i+" nouns: ");
                    while(productSentences[i].nouns[j]!=null)
                    {
                        System.out.print("id: "+productSentences[i].posNoun[j]+" ");
                        System.out.println(productSentences[i].nouns[j]);
                        j++;
                    }
                    j=0;
                    System.out.println("Sentence "+i+" verbs: ");
                    while(productSentences[i].verbs[j]!=null)
                    {
                        System.out.print("id: "+productSentences[i].posVerb[j]+" ");
                        System.out.println(productSentences[i].verbs[j]);
                        j++;
                    }
                    i++;
                }
             
               */
		Feature_Parsing.nounSynonymGrouping(productnouns, productNounGroup);
		Feature_Parsing.genericNounSynonymGrouping(genericnouns);
		Feature_Parsing.freqFeatureExtraction(productNounGroup, genericnouns, productWordCount, genericWordCount,frequentFeature);
		/*Feature_Parsing.ReplaceNounsByNounGroup(productSentences, productNounGroup);
               
                /*
                ff.put("zoom","zoom");
                ff.put("time","time");
                ff.put("image","image");
                ff.put("quality","quality");
                ff.put("g2","g2");
                ff.put("g3","g3");
                ff.put("lcd","lcd");
                ff.put("software","software");
                ff.put("lens","lens");
                ff.put("megapixel","megapixel");
                ff.put("shoot","shoot");
                ff.put("auto","auto");
                ff.put("subject","subject");
                ff.put("moment","moment");
                ff.put("shutter","shutter");
                ff.put("canon","canon");
                ff.put("result","result");
                ff.put("cap","cap");
                ff.put("setting","setting");
                ff.put("camera","camera");
                ff.put("unit","unit");
                ff.put("flash","flash");
                ff.put("focus","focus");
                ff.put("mode","mode");
                ff.put("point","point");
                ff.put("shot","shot");
                ff.put("picture","picture");
                ff.put("film","film");
                ff.put("manual","manual");
                
                /*
                ff.put("camera","camera");
                ff.put("battery", "battery");
                ff.put("picture", "picture");
                ff.put("zoom", "zoom");
                */
                /*
                opinion_lexicon.build_dictionary(dictionary, positive, negative);                             
                Polarity_using_lexicon.polarity_using_lexicon(productSentences, dictionary, frequentFeature, result);
                Polarity_using_lexicon.apply_conjunction_rule(productSentences, result);
                int k=0,l=0;
                while(result.feature[k]!=null)
                {
                    if(result.polarity[k]!=0)
                    {
                           l++;
                    }
                        System.out.print(result.sentnum.get(k)+"  ");
                        System.out.print(result.feature[k]+"  ");
                        System.out.print(result.opinion[k]+"  ");
                        System.out.println(result.polarity[k]);  
                    k++;
                }
                System.out.println("j:  "+l);
                System.out.println("i:  "+k);
                
                //Review.reviewBasedPolarity(result);
                /*
                DivisionofDictionary.division(result, polarity);
                DivisionofDictionary.wordlabel(result, wordLabel);
                ProbabilityApproach.nounlabel(result, featureRecord);
                ProbabilityApproach.probabilityApproach(result, polarity, featureRecord, wordLabel);
                /*
                old_method.wordlabel(result, wordLabel);
                old_method.PMI_old(result, wordLabel);
                */
              /*  FeatureClustering.clustering(positiveFeature, negativeFeature, result);
                SummarizeProduct.positiveSummery(positiveFeature, negativeFeature, productSentence);
                SummarizeProduct.negativeSummery(negativeFeature, productSentence);
                
                //Review.create_reviewHash(rev_Hash, result);
                
                System.out.println("FINAL RESULT: ");
                int i3=0,j1=0;
                while(result.feature[i3]!=null)
                {
                    if(result.polarity[i3]!=0)
                    {
                           j1++;
                    }
                        System.out.print(result.sentnum.get(i3)+"  ");
                        System.out.print(result.feature[i3]+"  ");
                        System.out.print(result.opinion[i3]+"  ");
                        System.out.println(result.polarity[i3]);                   
                     
                    i3++;
                }
                System.out.println("j:  "+j1);
                System.out.println("i:  "+i3);
               /* 
                Collection<Final_Opinion_Polarity> t1 = rev_Hash.values();
                System.out.println("size: "+rev_Hash.size());
                int j=0;
                for(Final_Opinion_Polarity t2 : t1)
                {
                    int i3=0;
                    while(t2.feature[i3]!=null)
                    {
                        if(t2.polarity[i3]!=0)
                        {
                            System.out.print("J: "+j+"  ");
                            System.out.print(t2.feature[i3]+"  ");
                            System.out.print(t2.opinion[i3]+"  ");
                            System.out.println(t2.polarity[i3]);                   
                            j++;
                        }
                        i3++;
                    }
                }
                System.out.println("j: "+j);
                */
               /* 
                System.out.println("From separate dictionary.");
                int i1=0,total=0;
                Iterator iter = pos.keySet().iterator();
                while(iter.hasNext()) 
                {i1++;
                    String key = (String)iter.next();
                    Integer val = (Integer)pos.get(key);
                    System.out.print(i1+"  ");
                    System.out.println("key,val: " + key + "," + val);
                    total+=val;
                }
                System.out.println("positive: "+pos.size());
                //int i1=0;
                iter = neg.keySet().iterator();
                while(iter.hasNext()) 
                {i1++;
                    String key = (String)iter.next();
                    Integer val = (Integer)neg.get(key);
                    System.out.print(i1+"  ");
                    System.out.println("key,val: " + key + "," + val);
                    total+=val;
                }
                System.out.println("negative: "+neg.size());
                System.out.println("total: "+total);
           */
    }
    
    
   public static void negationWord()
   {
	String negationWord[] = {"no","not","never","doesn't","less","without","barely","hardly","rarely","don't","n't"};
        for (String negationWord1 : negationWord) 
        {
           negation.put(negationWord1, negationWord1);
        }
   }
   
   public static int word(String str, int len)
    {
        int i1;
        char ar[] = new char[len];
        ar = str.toCharArray();
        for(i1=0;i1<len;i1++)
        {
            if(ar[i1]==' ' && ar[i1+1]=='(')
            {
                break;
            }
        }
        return i1;
    }
    public static void misspell(String input_fil) throws FileNotFoundException, IOException
    {
        HashMap<String, String> st = new HashMap<>();

        String str;
        File f1 = new File("E:\\Disertation\\APIs\\misspellings.txt");
        try (FileReader file = new FileReader(f1)) 
        {
            BufferedReader bf = new BufferedReader(file);
           while ((str = bf.readLine()) != null) 
           {
               int l = str.length();
               int index = word(str,l);
               String key = str.substring(0,index);
               String val = str.substring(index+2,l-1);
                if(!st.containsKey(val))
                {
                    st.put(key,val);
                   // System.out.print(key+"  ");
                   // System.out.println(val);
                }
            }
        }
       
        int cr=0;
        File file = new File("E:\\Disertation\\Reviews\\Texts\\"+input_fil);
        File temp = File.createTempFile("file", ".txt", file.getParentFile());
        String charset = "UTF-8";
        PrintWriter writer;
        
        try (BufferedReader bf1 = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) 
        {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), charset));
           while ((str = bf1.readLine()) != null) 
           {
                String[] words = str.split(" ");
                for (int k = 0; k < words.length; k++) 
                {
                    if (st.containsKey(words[k]))
                    {
                        cr++;
                        writer.print(st.get(words[k]));
                        System.out.print("wrong: "+words[k]);
                        System.out.println("  corrected: "+st.get(words[k]));
                    }
                    else
                    {
                        writer.print(words[k]);
                    }
                    if (k < words.length - 1)
                    {
                        writer.print(" ");
                    }
                }
                writer.println();
            }
        }
            writer.close();
            file.delete();
            temp.renameTo(file);
           // System.out.println("Total correction: "+cr);
    }
    
    public static void preprocess(String fileName) throws IOException
    {
        File file = new File("E:\\Disertation\\Reviews\\Texts\\"+fileName);
        File temp = File.createTempFile("file", ".txt", file.getParentFile());
        String charset = "UTF-8";
        PrintWriter writer;
        String line = "";
        int ind, len;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) 
        {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), charset));
            while ((line = reader.readLine()) != null) {
                ind = line.indexOf("##");
                len = line.length();
                if (ind != -1) {
                    line = line.substring(ind + 2, len);
                }
                writer.println(line);
            }
        }
        writer.close();
        file.delete();
        temp.renameTo(file);
    }
    
}   //End Class