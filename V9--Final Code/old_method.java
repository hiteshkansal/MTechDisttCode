/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package final_dissertation;

import java.util.HashMap;

/**
 *
 * @author Hitesh
 */
public class old_method 
{
    public static void wordlabel(Final_Opinion_Polarity FOP, HashMap<String, WordPolarity> wrdlbl)
    {
        int i=0;
        String key;
        while(FOP.feature[i]!=null)
        {
            key = FOP.opinion[i];
            WordPolarity wp = wrdlbl.get(key);
            if(wp==null)
            {
                wp = new WordPolarity();
            }
            if(FOP.polarity[i]==-1)
            {
                wp.neg++;
            }
            else if(FOP.polarity[i]==1)
            {
                wp.pos++;
            }
            else
            {
                wp.neutral++;
            }
            wp.input = key;
            wp.total++;
            wrdlbl.put(key, wp);
            i++;
        }      
    }
    
    public static int labelCount(Final_Opinion_Polarity FOP, int v)
    {
        int total=0,i=0;
        while(FOP.opinion[i]!=null)
        {
            if(FOP.polarity[i]==v)
            {
                total++;
            }
            i++;
        }
        return total;
    }
    
    public static void PMI_old(Final_Opinion_Polarity FOP, HashMap<String, WordPolarity> wrdlbl)
    {
        int i=0,pos_cnt,neg_cnt;
        pos_cnt = labelCount(FOP, 1);
        neg_cnt = labelCount(FOP, -1);
        int x_pos = 0;      //count of opinion with positive label
        int x_neg = 0;      //count of opinion with negative label
        int label_total=0;
        while(FOP.opinion[i]!=null)
        {
            x_pos = 0;
            x_neg = 0;    
            label_total = 0;
            if(FOP.polarity[i]==0 && wrdlbl.containsKey(FOP.opinion[i]))
            {
                WordPolarity wp = wrdlbl.get(FOP.opinion[i]);
                x_pos = wp.pos;
                x_neg = wp.neg;
                label_total = wp.total;
                double temp1, temp2;
                temp1 = Math.log((x_pos+1)/(label_total*pos_cnt+1));
                temp2 = Math.log((x_neg+1)/(label_total*neg_cnt+1));
                
                if(temp1 > temp2)
                {
                    System.out.println("Sent no. "+FOP.sentnum.get(i)+"Positive: "+temp1+"  "+temp2+"  "+FOP.feature[i]+" "+FOP.opinion[i]);
                    FOP.polarity[i] = 1;
                    pos_cnt++;
                }
                else if(temp2 > temp1)
                {
                    System.out.println("Sent no. "+FOP.sentnum.get(i)+"Negative: "+temp2+"  "+temp1+"  "+FOP.feature[i]+" "+FOP.opinion[i]);
                    FOP.polarity[i] = -1;
                    neg_cnt++;
                }
            }
            i++;
        }
    }
}
