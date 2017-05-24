/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hitesh
 */
import edu.cmu.lti.ws4j.RelatednessCalculator;
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
import edu.cmu.lti.ws4j.impl.*;
import edu.cmu.lti.lexical_db.*;

public class Feature_Parsing {

    static int i = 0;
    static int num = 0;
    public static void parsefeature(Sentences sent[], HashMap<String, Noun_Word> noun, String fileout, HashMap<String, String> negation) {
        try {
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
        NodeList nl = elem.getElementsByTagName(tg);
        int nn = 0, n,aj=0,ng=0,cc=0;
        int len = nl.getLength();
        Node wrd;
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
                    if(s.equals("t"))
                    {
                        num++;
                    }
                    else if (!(s.equals("##") || s.equals("p")))
                    {
                        
                        if (vl.equals("NNPS") || vl.equals("NNS"))
                        {
                            n = s.length();
                            s = s.substring(0, n - 1);
                        }
                        
                        sent.nouns[nn] = s.toLowerCase();
                        sent.posNoun[nn] = Integer.parseInt(el.getAttribute("wid"));
                        sent.reviewID = num;
                        Noun_Word word = noun.get(sent.nouns[nn]);
                        if (word == null) 
                        {
                            word = new Noun_Word(sent.nouns[nn], 1);
                        } 
                        else
                        {
                            word.increment();
                        }
                        noun.put(sent.nouns[nn], word);
                        nn++;                        
                    }
                    
                }
            }

            else if(vl.equals("JJ") || vl.equals("JJS") || vl.equals("JJR"))
            {
                if(s!=null)
		{
                        if (!(s.equals("##") || s.equals("<") || s.equals("=")))
                        {
                            sent.adjective[aj]= s.toLowerCase();
                            sent.posAdjective[aj] = el.getAttribute("wid");					
                            //System.out.println("Adjective: "+sent.adjective[aj]+" Pos"+sent.posAdjective[nn]);
                            aj++;
                        }
		}
            }
            
            else if(vl.equals("CC"))
            {
                if(s!=null)
                {
                    if(!(s.equals("x") || s.equals("+") || s.equals("&")))
                    {
                        sent.conjunction[cc] = s.toLowerCase();
                        sent.posConjunction[cc] = Integer.parseInt(el.getAttribute("wid"));
                        //System.out.println("Conjunction: "+sent.conjunction[cc]+" Pos "+sent.posConjunction[cc]);
                        cc++;
                    }
                }
            }
            
            else if(el.getFirstChild().getNodeValue()!=null)
            {
		if(negation.containsKey(s))
		{
			sent.negation[ng] = s.toLowerCase();
			sent.posNegation[ng] = Integer.parseInt(el.getAttribute("wid"));
			//System.out.println("Negation Words :"+s);
			ng++;
		}
            }          
          }//End for loop
        
       }//End function
    
    public static void nounSynonymGrouping(HashMap<String, Noun_Word> nouns, HashMap<String, NounGroup> nounGroup )
	{
		Collection<Noun_Word> words =  nouns.values();
		
                ILexicalDatabase db1 = new NictWordNet();
                RelatednessCalculator rc = new Path(db1);
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
		Iterator<Noun_Word> itr = words.iterator();
                Iterator<Noun_Word> itr2 = words.iterator();
                //System.out.println(itr.next().noun +"   "+itr2.next().noun);
		//System.out.println("before for");
                Noun_Word word1=itr.next();
                //System.out.println(word1.noun);
               // int i=0;
		for(;itr.hasNext();word1 = itr.next())
		{
                              //  System.out.println(i++);
	
				int nounCountInCluster = 0;
				NounGroup noun = new NounGroup();
				noun.nouns[nounCountInCluster++] = word1.noun;
				noun.count = word1.cnt;
				nounGroup.put(word1.noun, noun);
				
				
                                //System.out.println("before inner for");
				for(itr2 = words.iterator();itr2.hasNext();)
				{
					Noun_Word word2 = itr2.next(); 
					if(word1.noun.equals(word2.noun))
					{
                                            //System.out.println(word2.noun);
						continue;
					}
					//System.out.println("inside inner for");
					
						double n = rc.calcRelatednessOfWords(word1.noun, word2.noun);
						if(n>=.33)
						{
							if(word1.cnt > word2.cnt)
							{
								//System.out.println(word1.noun+"....."+word2.noun);
								NounGroup localNoun = nounGroup.get(word1.noun);								
                                                                if(localNoun.nouns[nounCountInCluster]!=null)
                                                                {
                                                                    localNoun.nouns[nounCountInCluster] = word2.noun;
                                                                    nounCountInCluster++;
                                                                }
								localNoun.count = localNoun.count + word2.cnt;
								nounGroup.put(word1.noun, localNoun);							
							}
							else
							{
                                                        //    System.out.println("exit");
								nounGroup.remove(word1.noun);
								break;
			
							}
							
						}
					
				}
                                //System.out.println("after inner for");
				clusterCount++;
                                
		}
               // System.out.println("after for");
		/*
		Collection<NounGroup> group = nounGroup.values();
		int clusterNo = 1;
                for (NounGroup noungroup : group) 
                {
                    System.out.println("Noun Based Cluster no. :"+ clusterNo);
                    for(int j=0; noungroup.nouns[j]!=null ;j++)
                    {
                        System.out.println(noungroup.nouns[j]);
                    }
                    System.out.println("total Noun Count: "+noungroup.count);
                    clusterNo++;  
                }
           */
	}
    
	public static void genericNounSynonymGrouping(HashMap<String, Noun_Word> genericNouns)
	{
		Collection<Noun_Word> words =  genericNouns.values();
		HashMap<String, Noun_Word> nouns = new HashMap<>();
                for (Noun_Word word : words) 
                {
                    nouns.put(word.noun, word);
                }
		
                ILexicalDatabase db1 = new NictWordNet();
                RelatednessCalculator rc = new Path(db1);
                	
		Iterator<Noun_Word> itr = words.iterator();
                Iterator<Noun_Word> itr2;// = words.iterator();
               
                Noun_Word word1=itr.next();
               
		for(;itr.hasNext();word1 = itr.next())
		{
			if(word1.checked == false)
			{
			
				int count = nouns.get(word1.noun).cnt;
				for(itr2 = words.iterator();itr2.hasNext();)
				{
					Noun_Word word2 = itr2.next();
					double n = rc.calcRelatednessOfWords(word1.noun, word2.noun);
						if(n>=.33)
						{						
							//System.out.println(word1.noun+"....."+word2.noun);
							word1.cnt += word2.cnt;	
							word2.cnt +=count;
							genericNouns.put(word2.noun, word2);
						}
				}
				genericNouns.put(word1.noun, word1);
				
			}
	
		}
		
	}

	public static void freqFeatureExtraction(HashMap<String,NounGroup> productNounGroup,HashMap<String,Noun_Word> genericNouns, float productWordCount, float genericWordCount,HashMap<String,String> frequentFeature)
	{
		Collection<NounGroup> words = productNounGroup.values();
		int i=0;
		System.out.println("Most Important Features of  Canon Camera:");
                for (NounGroup productWord : words) 
                {
                 Noun_Word genericWord = genericNouns.get(productWord.nouns[0]);
            
                                if(productWord.count>10)
                                {
                                //System.out.println(productWord.nouns[0]);
                                    if(genericWord != null)
                                    {
                                        float productFreq = (float)productWord.count/productWordCount*1000;
                                        float genericFreq = (float)genericWord.cnt/genericWordCount*1000;
                                        float theta = productFreq - genericFreq;
                                        if(theta>1)
                                           {
                                                i++;
                                                frequentFeature.put(productWord.nouns[0], productWord.nouns[0]);
                                                System.out.println(i+" : "+productWord.nouns[0]+"="+theta);
                                            }
                                    }
                                    else
                                    {
                                        float theta = (float)productWord.count/productWordCount*1000;
                                        if(theta>1)
                                        {
                                            i++;
                                            frequentFeature.put(productWord.nouns[0], productWord.nouns[0]);
                                            System.out.println(i+" : "+productWord.nouns[0]+"="+theta);
                                        }

                                    }

                                }
                     }
	}
        
	public static void ReplaceNounsByNounGroup(Sentences sentence[], HashMap<String,NounGroup> productNounGroup )
	{
		Collection<NounGroup> nounsGroup = productNounGroup.values();
		//System.out.println("LAST FUNCTION STARTED....");
		for(int i=0; sentence[i]!= null; i++)
		{
                    for (NounGroup nouns : nounsGroup) 
                    {
                        for(int j=1; nouns.nouns[j] != null; j++)
                        {
                            for(int k=0; sentence[i].nouns[k] != null; k++)
                            {
                                if(sentence[i].nouns[k].equals(nouns.nouns[j]))
                                {
                                    sentence[i].nouns[k] = nouns.nouns[0];
                                }
                            }
                        }
                    }
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