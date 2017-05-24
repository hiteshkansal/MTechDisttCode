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
public class DivisionofDictionary 
{    
    public static void division(Final_Opinion_Polarity FOP, HashMap<String, WordPolarity> polarity)
    {
        int i=0;
        String key;
        while(FOP.feature[i]!=null)
        {
            key = FOP.feature[i]+FOP.opinion[i];
            WordPolarity wp = polarity.get(key);
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
            wp.total++;
            wp.input = key;
            polarity.put(key, wp);
            i++;
        }      
    }    //End Function
    
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
}   //End Class
