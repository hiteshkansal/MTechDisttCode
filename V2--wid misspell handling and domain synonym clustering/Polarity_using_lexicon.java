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

public class Polarity_using_lexicon {
    
    public static void polarity_using_lexicon(Sentences sent[], HashMap<String, Integer> dictionary, HashMap<String, String> features, Final_Opinion_Polarity FOP)
    {
        int i=0,j=0;
        String noun = null;
        while(sent[i]!=null)
        {
            int k=0;
            while(sent[i].adjective[k]!=null)
            {
                int l=0, m=0;
                int fnd_noun = 0, neg=0;
                int min = 10000;
                for(;sent[i].nouns[l]!=null;l++)
                {
                    if(features.containsKey(sent[i].nouns[l]))
                    {
                        fnd_noun = 1;
                        int d = Math.abs(Integer.parseInt(sent[i].posAdjective[k])-sent[i].posNoun[l]);
                        if(d<min)
                        {
                            min = d;
                            noun = sent[i].nouns[l];
                        }
                    }
                }
                for(;sent[i].negation[m]!=null;m++)
                {
                    int d = Math.abs(Integer.parseInt(sent[i].posAdjective[k])-sent[i].posNegation[m]);
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
                        FOP.opinion[j] = sent[i].adjective[k];

                        if(dictionary.containsKey(sent[i].adjective[k]))
                        {
                            int polarity = dictionary.get(sent[i].adjective[k]);
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
            int c=0;
            
            while(sent[i].conjunction[c]!=null)
            {
                 int k=0,pos_low=-1,pos_high=-1;
                 while(sent[i].adjective[k]!=null)      //We use adjective instead of noun becoz each noun is not adjective and sentence contains all nouns not frequent noun.
                 {
                        if(Integer.parseInt(sent[i].posAdjective[k]) < sent[i].posConjunction[c] && more==1)
                        {
                            less = 1;
                            pos_low = k;        //Position of 1st adjective.
                            more=0;                           
                        }
                        else if(Integer.parseInt(sent[i].posAdjective[k]) > sent[i].posConjunction[c])
                        {                           
                            greater = 1;
                            pos_high = k;       //Position of last adjective
                        }
                        k++;
                }
                more=1;
                if(sent[i].conjunction[c].equals("but") || sent[i].conjunction[c].equals("But") || sent[i].conjunction[c].equals("however") || sent[i].conjunction[c].equals("However"))
                {
                    if(less==1 && greater==1)   //Intra-sentence conjunction rules
                    {
                        if(pos_low!=-1)
                        {                            
                            intra_conjunction(sent, FOP, i, c, pos_low,-1);     //-1 for but clause
                        }
                    }
                  /*  else if(less==0 && greater==1)      //Inter-Sentence conjuction rule.
                    {
                        
                    }
                   */
                }
                else if(sent[i].conjunction[c].equals("and") || sent[i].conjunction[c].equals("And"))
                {
                    if(less==1 && greater==1)   //Intra-sentence conjunction rules
                    {
                        if(pos_low!=-1)
                        {
                            intra_conjunction(sent, FOP, i, c, pos_low,1);      //+1 for and clause
                        }
                    }                    
                }
                c++;
            }
            i++;
        }
    }   //End function
            
     public static void intra_conjunction(Sentences sent[], Final_Opinion_Polarity FOP, int i, int c, int pos_low, int value)
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
                                if(Integer.parseInt(sent[i].posAdjective[pos]) < sent[i].posConjunction[c])
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
                           result(sent,FOP,i,c,pos_low,m,value,1,polarity);
                        }
                        else if(before==0)  //implying opinion word found after conjunction.
                        {
                           result(sent,FOP,i,c,pos_low,m,1,value,polarity);
                        }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }       //End Function
    
    public static void result(Sentences sent[], Final_Opinion_Polarity FOP, int i, int c, int pos_low, int m, int value, int temp ,int polarity)
     {
            try
            {
                                while(sent[i].posAdjective[pos_low]!=null)
                                {
                                    if(Integer.parseInt(sent[i].posAdjective[pos_low]) < sent[i].posConjunction[c])
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
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
     }
    
}   //End Class
