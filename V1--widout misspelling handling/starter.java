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
import java.util.ArrayList;
import java.util.Iterator;

public class Starter {

    public static Sentences productSentences[] = new Sentences[4000];
    public static Sentences genericSentences[] = new Sentences[4000];
    
    public static HashMap<String,Noun_Word> productnouns = new HashMap<>(); 
    public static HashMap<String,Noun_Word> genericnouns = new HashMap<>(); 
    
    public static String productFileInput ="temp.txt";
    public static String productFileOutput = "tempOutPut.xml";
    public static final String genericFileInput = "CanonG3.txt";
    public static final String genericFileOutput = "CanonG3OutPut.xml";
    
    public static HashMap<Integer, String> productSentence = new HashMap<>();
    public static HashMap<Integer, String> genericSentence = new HashMap<>();
    
    public static HashMap<String, String> negation = new HashMap<>();
    public static HashMap<String,String> frequentFeature = new HashMap<>();
    public static HashMap<String, NounGroup> productNounGroup = new HashMap<>(); 
    
    public static String positive = "positive-words.txt";
    public static String negative = "negative-words.txt";
    public static final HashMap<String, Integer> dictionary = new HashMap<>();
    public static final HashMap<String, Integer> pos = new HashMap<>();
    public static final HashMap<String, Integer> neg = new HashMap<>();
    public static HashMap<String, Cluster> positiveFeature = new HashMap<>();
    public static HashMap<String, Cluster> negativeFeature = new HashMap<>();
    
    public static Final_Opinion_Polarity result = new Final_Opinion_Polarity();
    
    public static HashMap<String,String> ff = new HashMap<>();
    
    public static void main(String[] args) throws Exception 
	{
                MaxentTagger mt = new MaxentTagger("E:\\Disertation\\stanford-postagger-full-2014-01-04\\models\\english-left3words-distsim.tagger");
		POStag.TextToXml(mt, productFileInput, productFileOutput, productSentence);
                POStag.TextToXml(mt, genericFileInput, genericFileOutput, genericSentence);
                
                negationWord();
                
                Feature_Parsing.parsefeature(productSentences, productnouns, productFileOutput, negation);
                float productWordCount = WordCount.wordCount(productFileInput);
                Feature_Parsing.parsefeature(genericSentences, genericnouns, genericFileOutput, negation);
		float genericWordCount = WordCount.wordCount(genericFileInput);

                
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
                    while(productSentences[i].adjective[j]!=null)
                    {
                        System.out.print("id: "+productSentences[i].posAdjective[j]+" ");
                        System.out.println(productSentences[i].adjective[j]);
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
                    
                    i++;
                }
                
                
        /*
               
		Feature_Parsing.nounSynonymGrouping(productnouns, productNounGroup);
		Feature_Parsing.genericNounSynonymGrouping(genericnouns);
		Feature_Parsing.freqFeatureExtraction(productNounGroup, genericnouns,productWordCount,genericWordCount,frequentFeature);
		Feature_Parsing.ReplaceNounsByNounGroup(productSentences, productNounGroup);
        */       
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
    */          
                
                /*
                ff.put("camera","camera");
                ff.put("battery", "battery");
                ff.put("picture", "picture");
                ff.put("zoom", "zoom");
                */
             /*   opinion_lexicon.build_dictionary(dictionary, positive, negative);                             
                Polarity_using_lexicon.polarity_using_lexicon(productSentences, dictionary, frequentFeature, result);
                Polarity_using_lexicon.apply_conjunction_rule(productSentences, result);
                DivisionofDictionary.division(result,pos,neg);
                ProbabilityApproach.probabilityApproach(result, pos, neg);
                FeatureClustering.clustering(positiveFeature, negativeFeature, result);
                SummarizeProduct.positiveSummery(positiveFeature, negativeFeature, productSentence);
                SummarizeProduct.negativeSummery(negativeFeature, productSentence);
               */
                
                /*
                int i=0,j=0;
                while(result.feature[i]!=null)
                {
                    if(result.polarity[i]!=0)
                    {
                        System.out.print("J: "+j+"  ");
                        System.out.print(result.sentnum[i]+"  ");
                        System.out.print(result.feature[i]+"  ");
                        System.out.print(result.opinion[i]+"  ");
                        System.out.println(result.polarity[i]);                   
                        j++;
                    }
                    i++;
                }
                System.out.println("j:  "+j);
                System.out.println("i:  "+i);
                
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
		String negationWord[] = {"no","not","never","doesn't","less","without","barely","hardly","rarely","don't"};
                for (String negationWord1 : negationWord) {
                    negation.put(negationWord1, negationWord1);
                }
	}
}