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

public class Polarity_using_lexicon 
{
    public static void polarity_using_lexicon(Sentences sent[], HashMap<String, Integer> dictionary, HashMap<String, String> features, Final_Opinion_Polarity FOP)
    {
        int i=0,j=0,prev=1,rev=0,rr=0;
        String noun = null;
        while(sent[i]!=null)
        {
            int k=0;
            if(sent[i].reviewID!=prev && sent[i].reviewID!=0)
            {
                FOP.reviewID.add(rev,i);
                rev++;
            }
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
                    if(Integer.parseInt(sent[i].posAdjective[k]) > sent[i].posNegation[m])
                    {
                        int d = Math.abs(Integer.parseInt(sent[i].posAdjective[k])-sent[i].posNegation[m]);
                        if(d<4)
                        {
                            neg = 1;                       
                        }
                    }
                }              
                if(fnd_noun==1)
                {
                    try
                    {
                        FOP.feature[j] = noun;
                        FOP.sentnum.add(j,i);
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
                            if(neg==1)
                            {
                                FOP.ngtn[j] = true;
                            }
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

            if(sent[i].reviewID!=0)
            {
                prev = sent[i].reviewID;
            }
            i++;
        }
        i=0;rr=0;
        noun = null;
        
        while(sent[i]!=null)
        {
            int vb=0;
            while(sent[i].verbs[vb]!=null)
            {
                int l=0, m=0;
                int fnd_noun = 0, neg=0;
                int min = 10000;
                if(!(dictionary.containsKey(sent[i].verbs[vb])))
                {
                    vb++;
                    continue;
                }
                
                for(;sent[i].nouns[l]!=null;l++)
                {
                    if(features.containsKey(sent[i].nouns[l]) && (sent[i].posNoun[l] < Integer.parseInt(sent[i].posVerb[vb])))
                    {
                        fnd_noun = 1;
                        int d = Math.abs(Integer.parseInt(sent[i].posVerb[vb])-sent[i].posNoun[l]);
                        if(d<min)
                        {
                            min = d;
                            noun = sent[i].nouns[l];
                        }
                    }
                    else if(features.containsKey(sent[i].nouns[l]) && (sent[i].posNoun[l] > Integer.parseInt(sent[i].posVerb[vb])))
                    {
                        fnd_noun = 1;
                        int d = Math.abs(Integer.parseInt(sent[i].posVerb[vb])-sent[i].posNoun[l]);
                        if(d<min)
                        {
                            min = d;
                            noun = sent[i].nouns[l];
                        }
                        break;      //because after noun, we will consider most closest verb.
                    }
                }
                
                for(;sent[i].negation[m]!=null;m++)
                {
                    if(Integer.parseInt(sent[i].posVerb[vb]) > sent[i].posNegation[m])
                    {
                        int d = Math.abs(Integer.parseInt(sent[i].posVerb[vb])-sent[i].posNegation[m]);
                        if(d<4)
                        {
                            neg = 1;                       
                        }
                    }
                }
                if(fnd_noun==1)
                {
                    try
                    {
                        rr=0;
                        Iterator i1 = FOP.sentnum.iterator();
                        while(i1.hasNext())
                        {
                            if(FOP.sentnum.get(rr) <= i)
                            {
                                rr++;
                            }
                            else
                            {
                                break;
                            }
                            i1.next();
                        }
                        rr--;
                        while(FOP.sentnum.get(rr)==i)
                        {
                            if(noun==FOP.feature[rr] && FOP.polarity[rr]==0)
                            {
                               // System.out.print("sent no: "+FOP.sentnum.get(rr)+"  i: "+i);
                                //System.out.print("feature: "+FOP.feature[rr]);
                               // System.out.print("  prev adj: "+FOP.opinion[rr]);
                                FOP.opinion[rr] = sent[i].verbs[vb];
                                //System.out.println("  new verb: "+FOP.opinion[rr]);
                                int polarity = dictionary.get(sent[i].verbs[vb]);
                                if(neg==0)
                                {
                                    FOP.polarity[rr] = polarity;
                                }                                    
                                else
                                {
                                    FOP.polarity[rr] = -1*polarity;
                                    FOP.ngtn[rr] = false;
                                }
                                break;
                            }
                            rr--;
                            //i1.next();
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();                    
                    }
                }
                vb++;
            }
            i++;
        }
        
        System.out.println("rev: "+rev);
    }   //End Function
    
   
    public static void apply_conjunction_rule(Sentences sent[], Final_Opinion_Polarity FOP)
    {
        int i=0;
        int less=0, greater=0,more=1;
        while(sent[i]!=null)
        {
            int c=0,k=0,pos_low=-1,pos_high=-1;
            String next_con="";
            while(sent[i].conjunction[c]!=null)
            {
                /*
                if(sent[i].conjunction.length>c+1 && sent[i].conjunction[c+1]!=null)
                {
                    next_con = sent[i].posConjunction[c+1];
                }
                */
                 while(sent[i].adjective[k]!=null)      //We use adjective instead of noun becoz each noun is not adjective and sentence contains all nouns not frequent noun.
                 {
                        if((Integer.parseInt(sent[i].posAdjective[k]) < Integer.parseInt(sent[i].posConjunction[c])) && more==1)
                        {
                            less = 1;
                            pos_low = k;        //Position of 1st adjective.
                            more=0;                           
                        }
                        else if(Integer.parseInt(sent[i].posAdjective[k]) > Integer.parseInt(sent[i].posConjunction[c]))
                        {                           
                            greater = 1;
                            pos_high = k;       //Position of first adjective after conjunction.
                            break;
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
                            intra_conjunction(sent, FOP, i, c, pos_low,-1,next_con);     //-1 for but clause
                        }
                    }
                  /*  
                    else if(less==0 && greater==1)      //Inter-Sentence conjuction rule.
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
                            intra_conjunction(sent, FOP, i, c, pos_low,1,next_con);      //+1 for and clause
                        }
                    }                    
                }
                c++;
            }
            i++;
        }
    }   //End function
            
     public static void intra_conjunction(Sentences sent[], Final_Opinion_Polarity FOP, int i, int c, int pos_low, int value, String next_con)
     {
            try
            {
                        int polarity=0,m=0; 
                        Iterator i1 = FOP.sentnum.iterator();
                        while(i1.hasNext())
                        {
                            if(FOP.sentnum.get(m) < i)
                            {
                                m++;
                            }
                            else
                            {
                                break;
                            }
                            i1.next();
                        }
                        int temp=m,before=-1, pos=0;
                        while(i1.hasNext() && FOP.sentnum.get(temp)==i)
                        {
                            if(FOP.polarity[temp]!=0)   //whether we know the polarity of a opinion..
                            {                    
                                if(Integer.parseInt(sent[i].posAdjective[pos]) < Integer.parseInt(sent[i].posConjunction[c]))
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
                            i1.next();
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
                                while(sent[i].posAdjective[pos_low]!=null)
                                {
                                    if(Integer.parseInt(sent[i].posAdjective[pos_low]) < Integer.parseInt(sent[i].posConjunction[c]))
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
                                Iterator i1 = FOP.sentnum.iterator();
                                while(i1.hasNext())
                                {
                                    if((Integer)i1.next()==m)
                                    {
                                        break;
                                    }
                                }
                                while(i1.hasNext())
                                {
                                    if(FOP.sentnum.get(m)==i)
                                    {
                                      /*  if(next_con!=null)
                                        {
                                            if(Integer.parseInt(sent[i].posAdjective[m]) < Integer.parseInt(next_con))
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
                                      */  {
                                            if(FOP.polarity[m]==0)
                                            {
                                                FOP.polarity[m] = value*polarity;
                                            }
                                            m++; 
                                            i1.next();
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
