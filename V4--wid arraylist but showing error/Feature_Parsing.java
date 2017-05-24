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

import edu.stanford.nlp.process.Morphology;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Feature_Parsing {

    static int i = 0;
    static int num = 0;

    public static void parsefeature(Sentences sent[], HashMap<String, Noun_Word> noun, String fileout, HashMap<String, String> negation) 
    {
        try 
        {
            i = 0;
            File xmlfile = new File("E:\\Disertation\\Reviews\\XMLs\\" + fileout);
            DocumentBuilderFactory dbfact = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbfact.newDocumentBuilder();
            Document doc = db.parse(xmlfile);
            doc.getDocumentElement().normalize();
            NodeList nlist = doc.getElementsByTagName("sentence");
            int len = nlist.getLength();
            //System.out.println(len);

            for (int t = 0; t < len; t++) {
                Node nd = nlist.item(t);
                if (nd.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) nd;
                    sent[i] = new Sentences();
                    getTag("word", elem, sent[i], noun, negation);
                    i++;
                }
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }

    public static void getTag(String tg, Element elem, Sentences sent, HashMap<String, Noun_Word> noun, HashMap<String, String> negation) 
    {
        int global_offset = 0;
        NodeList nl = elem.getElementsByTagName(tg);
        int nn = -1, n, aj = 0, ng = 0, cc = 0, tp, flag = 1, temp = -1;
        int len = nl.getLength();
        Node wrd;
        String temp_noun = "",prev="";
        Morphology mr = new Morphology();
        
        for (int i = 0; i < len; i++) 
        {
            wrd = nl.item(i);
            Element el = (Element) wrd;
            String vl = el.getAttribute("pos");
            String s = el.getFirstChild().getNodeValue();
            if (vl.equals("NN") || vl.equals("NNS") || vl.equals("NNP") || vl.equals("NNPS")) 
            {
                if (s != null) 
                {
                    if (s.equals("t")) 
                    {
                        num++;
                    }
                    else if (!(s.equals("##") || s.equals("p"))) 
                    {
                        nn++;
                        if (vl.equals("NNPS") || vl.equals("NNS"))
                        {
                            s = mr.stem(s);
                        }
                        tp = Integer.parseInt(el.getAttribute("wid"));
                        temp_noun = s.toLowerCase();
                        if (temp != -1) 
                        {
                            if (temp + 1 == tp) 
                            {
                                nn--;
                                if (flag == 1) 
                                {
                                    flag = 0;
                                    Noun_Word w1 = noun.get(sent.nouns.get(nn));
                                    if (w1.cnt == 1) 
                                    {
                                        noun.remove(sent.nouns.get(nn));
                                    } 
                                    else 
                                    {
                                        w1.cnt--;
                                        noun.put(sent.nouns.get(nn), w1);
                                    }
                                }
                                global_offset++;        //It is for Noun Phrases and to correct their position.
                                String tmp = sent.nouns.get(nn);
                                sent.nouns.remove(nn);
                                tmp = tmp+" "+temp_noun;
                                sent.nouns.add(nn, tmp);
                                temp = tp;
                                continue;
                            }
                        }
                        sent.nouns.add(nn,temp_noun);
                        sent.posNoun.add(nn,(tp - global_offset));
                        sent.reviewID = num;
                        temp = tp;
                        Noun_Word word = noun.get(sent.nouns.get(nn));
                        if (word == null) 
                        {
                            word = new Noun_Word(sent.nouns.get(nn), 1);
                        }
                        else 
                        {
                            word.increment();
                        }
                        noun.put(sent.nouns.get(nn), word);
                    }

                }
            } 
            else if (vl.equals("JJ") || vl.equals("JJS") || vl.equals("JJR")) 
            {
                flag = 1;
                if (s != null) 
                {
                    if (!(s.equals("##") || s.equals("<") || s.equals("="))) 
                    {
                        int tp1 = Integer.parseInt(el.getAttribute("wid"));
                        sent.adjective.add(aj,s.toLowerCase());
                        sent.posAdjective.add(aj,(tp1 - global_offset));
                        //System.out.println("Adjective: "+sent.adjective[aj]+" Pos"+sent.posAdjective[nn]);
                        aj++;
                    }
                }
            } 
            else if (vl.equals("CC")) 
            {
                flag = 1;
                if (s != null) {
                    if (!(s.equals("x") || s.equals("+") || s.equals("&"))) 
                    {
                        int tp1 = Integer.parseInt(el.getAttribute("wid"));
                        sent.conjunction.add(cc, s.toLowerCase());
                        sent.posConjunction.add(cc,(tp1-global_offset));
                        //System.out.println("Conjunction: "+sent.conjunction[cc]+" Pos "+sent.posConjunction[cc]);
                        cc++;
                    }
                }
            } 
            else if (el.getFirstChild().getNodeValue() != null) 
            {
                flag = 1;
                if (negation.containsKey(s)) 
                {
                    Node wrd1 = nl.item(i+1);
                    Element el1 = (Element) wrd1;
                    s = s.toLowerCase();
                    String s1 = el1.getFirstChild().getNodeValue().toLowerCase();
                    
                    if(s.equals("n't")&& (prev.equals("do") || prev.equals("does")))
                    {
                        int tp1 = Integer.parseInt(el.getAttribute("wid"));
                        sent.negation.add(ng, s);
                        sent.posNegation.add(ng, (tp1 - global_offset));
                        //System.out.println("Negation Words :"+prev+s);
                        ng++;
                        
                    }
                    else if(!((s.equals("not")&&(s1.equals("only") || s1.equals("just"))) || (s.equals("no")&& s1.equals("wonder"))))        //Excludind “not only”, “not just” and “no wonder”.
                    {
                        int tp1 = Integer.parseInt(el.getAttribute("wid"));
                        sent.negation.add(ng, s);
                        sent.posNegation.add(ng, (tp1 - global_offset));
                        //System.out.println("Negation Words ignore :"+prev+s+s1);
                        ng++;
                    }
                }
            }
           prev = s; 
        }    //End for loop

    }//End function

    public static void nounSynonymGrouping(HashMap<String, Noun_Word> nouns, HashMap<String, NounGroup> nounGroup) 
    {
        Collection<Noun_Word> words = nouns.values();
        //System.out.println(words.size());
        /*
         int ttt=0;
         for (Noun_Word word : words) 
         {
         System.out.println(word.noun+"   =   "+word.cnt);
         ttt++;
         }
         //System.out.println(ttt);
         */
        
        int clusterCount = 0;
        Iterator<Noun_Word> itr1,itr2;

        for (itr1 = words.iterator(); itr1.hasNext();) 
        {
            Noun_Word word1 = itr1.next();
            int nounCountInCluster = 0;
            NounGroup noun = new NounGroup();
            noun.nouns[nounCountInCluster++] = word1.noun;
            noun.count = word1.cnt;
            nounGroup.put(word1.noun, noun);

            for (itr2 = words.iterator(); itr2.hasNext();) 
            {
                Noun_Word word2 = itr2.next();
                if (word1.noun.equals(word2.noun)) 
                {
                    //System.out.println(word2.noun);
                    continue;
                }
                double n = WordNet.runPATH(word1.noun, word2.noun);
                if (n >= .33) 
                {
                    if (word1.cnt > word2.cnt) 
                    {
                        //System.out.println(word1.noun+"....."+word2.noun);
                        NounGroup localNoun = nounGroup.get(word1.noun);
                        localNoun.nouns[nounCountInCluster++] = word2.noun;
                        localNoun.count = localNoun.count + word2.cnt;
                        nounGroup.put(word1.noun, localNoun);
                    } 
                    else 
                    {
                        nounGroup.remove(word1.noun);
                        break;
                    }
                }
            }
            clusterCount++;
        }
    /*
         Collection<NounGroup> group = nounGroup.values();
         int clusterNo = 1;
         for (NounGroup noungroup : group) 
         {
            System.out.println("Noun Based Cluster no. :"+ clusterNo);
            int sz=0;
            for(int j=0; noungroup.nouns[j]!=null ;j++)
            {
               sz++;
               System.out.println(noungroup.nouns[j]);
            }
            System.out.println("Size: "+sz);
            System.out.println("total Noun Count: "+noungroup.count);
            clusterNo++;  
         }
        */
    }

    public static void genericNounSynonymGrouping(HashMap<String, Noun_Word> genericNouns) 
    {
        Collection<Noun_Word> words = genericNouns.values();
        HashMap<String, Noun_Word> nouns = new HashMap<>();
        for (Noun_Word word : words) 
        {
            nouns.put(word.noun, word);
        }
        Iterator<Noun_Word> itr1,itr2;
        for (itr1 = words.iterator(); itr1.hasNext();) 
        {
            Noun_Word word1 = itr1.next();
            int count = nouns.get(word1.noun).cnt;
            for (itr2 = words.iterator(); itr2.hasNext();) 
            {
                Noun_Word word2 = itr2.next();
                if (word2.checked == false)
                {
                    if (word1.noun.equals(word2.noun)) 
                    {
                        continue;
                    }
                    double n = WordNet.runPATH(word1.noun, word2.noun);
                    if (n >= .33) 
                    {
                        //System.out.println(word1.noun+"....."+word2.noun);
                        word1.cnt += word2.cnt;
                        word2.cnt += count;
                        genericNouns.put(word2.noun, word2);
                    }
                 }  
            }
            genericNouns.put(word1.noun, word1);
            word1.checked = true;
         }
    }

    public static void freqFeatureExtraction(HashMap<String, NounGroup> productNounGroup, HashMap<String, Noun_Word> genericNouns, float productWordCount, float genericWordCount, HashMap<String, String> frequentFeature) 
    {
        Collection<NounGroup> words = productNounGroup.values();
        int k = 0;
        System.out.println("Most Important Features: ");
        for (NounGroup productWord : words) 
        {
            Noun_Word genericWord = genericNouns.get(productWord.nouns[0]);

            if (productWord.count >= 10) 
            {
                //System.out.println(productWord.nouns[0]);
                if (genericWord != null) 
                {
                    float productFreq = (float) productWord.count / productWordCount * 1000;
                    float genericFreq = (float) genericWord.cnt / genericWordCount * 1000;
                    float theta = productFreq - genericFreq;
                    if (theta > 1) 
                    {
                        k++;
                        frequentFeature.put(productWord.nouns[0], productWord.nouns[0]);
                        System.out.println(k + " : " + productWord.nouns[0] + "=" + theta);
                    }
                } 
                else 
                {
                    float theta = (float) productWord.count / productWordCount * 1000;
                    if (theta > 1) 
                    {
                        k++;
                        frequentFeature.put(productWord.nouns[0], productWord.nouns[0]);
                        System.out.println(k + " : " + productWord.nouns[0] + "=" + theta);
                    }
                }
            }
        }
    }

    public static void ReplaceNounsByNounGroup(Sentences sentence[], HashMap<String, NounGroup> productNounGroup) 
    {
        Collection<NounGroup> nounsGroup = productNounGroup.values();
        //System.out.println("LAST FUNCTION STARTED....");
        try
        {    
        for (int temp = 0; sentence[temp] != null; temp++) 
        {
            for (NounGroup nouns : nounsGroup) 
            {
                for (int j = 1; nouns.nouns[j] != null; j++) 
                {
                    Iterator noun_itr = sentence[i].nouns.iterator();
                    for (int k = 0; noun_itr.hasNext();k++) 
                    {
                        String temp1 = (String)noun_itr.next();
                        if (temp1.equals(nouns.nouns[j])) 
                        {
                            sentence[temp].nouns.remove(k);
                            sentence[temp].nouns.add(k, nouns.nouns[0]);
                        }
                    }
                }
            }
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        /*   for(int l=0;sentence[l]!=null;l++)
         {
         for(int m=0;sentence[l].nouns[m]!=null;m++)
         {
         System.out.println(sentence[l].nouns[m]);
         }
         }
         */
    }

}//End class
