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
import java.util.Iterator;

public class Polarity_using_lexicon {
    
    public static void polarity_using_lexicon(Sentences sent[], HashMap<String, Integer> dictionary, HashMap<String, String> features, Final_Opinion_Polarity FOP)
    {
        int i=0,j=0;
        String noun = null;
        while(sent[i]!=null)
        {
            int k=0;
            Iterator adj = sent[i].adjective.iterator();
            while(adj.hasNext())
            {
                Iterator noun_itr = sent[i].nouns.iterator();
                int l=0, m=0;
                int fnd_noun = 0, neg=0;
                int min = 10000;
                for(;noun_itr.hasNext();noun_itr.next(),l++)
                {
                    int d = Math.abs(sent[i].posAdjective.get(k)-sent[i].posNoun.get(l));
                    if(d<min)
                    {
                        min = d;
                        noun = sent[i].nouns.get(l);
                    }
                }
                
                if(features.containsKey(noun))
                {
                    fnd_noun = 1;
                }
                Iterator neg_itr = sent[i].negation.iterator();
                for(;neg_itr.hasNext();neg_itr.next(),m++)
                {
                    int d = Math.abs(sent[i].posAdjective.get(k)-sent[i].posNegation.get(m));
                    if(d<=3)
                    {
                        neg = 1;                       
                    }
                }              
                if(fnd_noun==1)
                {
                    try
                    {
                        FOP.feature[j] = noun;
                        FOP.sentnum[j] = Integer.toString(i);
                        FOP.opinion[j] = sent[i].adjective.get(k);

                        if(dictionary.containsKey(sent[i].adjective.get(k)))
                        {
                            int polarity = dictionary.get(sent[i].adjective.get(k));
                            if(neg==0)
                            {
                                FOP.polarity[j] = polarity;
                            }                                    
                            else
                            {
                                FOP.polarity[j] = -1*polarity;
                            }
                        }                                    
                        else
                        {
                            FOP.polarity[j] = 0;
                        }
                        j++;   
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();                    
                    }
                }
                adj.next();
                k++;
            }
            i++;
        }
        
    }   //End Function
    
   
    public static void apply_conjunction_rule(Sentences sent[], Final_Opinion_Polarity FOP)
    {
        int i=0;
        int less=0, greater=0,more=1;
        
        while(sent[i]!=null)
        {
            int c=0,k=0,pos_low=-1,pos_high=-1;
            String next_con="";
            Iterator con_itr = sent[i].conjunction.iterator();
            Iterator adj_itr = sent[i].adjective.iterator();
            while(con_itr.hasNext())
            {
                try
                {
                 next_con = Integer.toString(sent[i].posConjunction.get(c+1));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                 while(adj_itr.hasNext())      //We use adjective instead of noun becoz each noun is not adjective and sentence contains all nouns not frequent noun.
                 {
                        if((sent[i].posAdjective.get(k) < sent[i].posConjunction.get(c)) && more==1)
                        {
                            less = 1;
                            pos_low = k;        //Position of 1st adjective.
                            more=0;                           
                        }
                        else if(sent[i].posAdjective.get(k) > sent[i].posConjunction.get(c))
                        {                           
                            greater = 1;
                            pos_high = k;       //Position of first adjective after conjunction.
                            break;
                        }
                        k++;
                }
                more=1;
                if(sent[i].conjunction.get(c).toLowerCase().equals("but") || sent[i].conjunction.get(c).toLowerCase().equals("however"))
                {
                    if(less==1 && greater==1)   //Intra-sentence conjunction rules
                    {
                        if(pos_low!=-1)
                        {                            
                            intra_conjunction(sent, FOP, i, c, pos_low,-1,next_con);     //-1 for but clause
                        }
                    }
                  /*  
                    else if(less==0 && greater==1)      //Inter-Sentence conjuction rule.
                    {
                        
                    }
                   */
                }
                else if(sent[i].conjunction.get(c).toLowerCase().equals("and"))
                {
                    if(less==1 && greater==1)   //Intra-sentence conjunction rules
                    {
                        if(pos_low!=-1)
                        {
                            intra_conjunction(sent, FOP, i, c, pos_low,1,next_con);      //+1 for and clause
                        }
                    }                    
                }
                c++;
                con_itr.next();
            }
            i++;
        }
    }   //End function
            
     public static void intra_conjunction(Sentences sent[], Final_Opinion_Polarity FOP, int i, int c, int pos_low, int value, String next_con)
     {
            try
            {
                        int polarity=0,m=0;                      
                        while(FOP.sentnum[m]!=null)
                        {
                            if(Integer.parseInt(FOP.sentnum[m])<i)
                            {
                                m++;
                            }
                            else
                            {
                                break;
                            }
                        }
                        int temp=m,before=-1, pos=0;
                        while(Integer.parseInt(FOP.sentnum[temp])==i)
                        {
                            if(FOP.polarity[temp]!=0)   //whether we know the polarity of a opinion..
                            {                    
                                if(sent[i].posAdjective.get(pos) < sent[i].posConjunction.get(c))
                                {
                                    before = 1;
                                    break;
                                }                                
                                polarity = FOP.polarity[temp];
                                before = 0;
                                break;
                            }
                            pos++;
                            temp++;                            
                        }
                       
                        if(before==1)       //implying opinion word found before conjunction.
                        {
                           result(sent,FOP,i,c,pos_low,m,value,1,polarity,next_con);
                        }
                        else if(before==0)  //implying opinion word found after conjunction.
                        {
                           result(sent,FOP,i,c,pos_low,m,1,value,polarity,next_con);
                        }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }       //End Function
    
    public static void result(Sentences sent[], Final_Opinion_Polarity FOP, int i, int c, int pos_low, int m, int value, int temp ,int polarity, String next_con)
     {
            try
            {
                Iterator adj_itr = sent[i].posAdjective.iterator();
                    while(adj_itr.hasNext())
                    {
                        if(sent[i].posAdjective.get(pos_low) < sent[i].posConjunction.get(c))
                        {
                            if(FOP.polarity[m]==0)
                            {
                                FOP.polarity[m] = temp*polarity;
                            }
                            m++;
                            pos_low++;
                        }
                        else
                        {
                            break;
                        }
                    }
                    while(FOP.sentnum[m]!=null)
                    {
                        if(Integer.parseInt(FOP.sentnum[m])==i)
                        {
                            if(next_con!=null)
                            {
                                if(sent[i].posAdjective.get(m) < Integer.parseInt(next_con))
                                {
                                    if(FOP.polarity[m]==0)
                                    {
                                        FOP.polarity[m] = value*polarity;
                                    }
                                    m++;
                                }
                                else
                                {
                                    break;
                                }
                            }
                            else
                            {
                                if(FOP.polarity[m]==0)
                                {
                                    FOP.polarity[m] = value*polarity;
                                }
                                m++;    
                            }
                        }
                        else
                        {
                            break;
                        }
                   }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
     }
    
}   //End Class
