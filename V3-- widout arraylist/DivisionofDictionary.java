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
    public static void division(Final_Opinion_Polarity FOP, HashMap<String, Integer> positive, HashMap<String, Integer> negative)
    {
        int i=0,val;
        String key;
        while(FOP.feature[i]!=null)
        {
            val=1;
            key = FOP.feature[i]+FOP.opinion[i];
            if(FOP.polarity[i]==-1)
            {
                if(negative.containsKey(key))
                {
                   val = negative.get(key);
                   val++;
                }
                negative.put(key, val);
            }
            else if(FOP.polarity[i]==1)
            {
                if(positive.containsKey(key))
                {
                    val = positive.get(key);
                    val++;
                }
                positive.put(key,val);
            }
            i++;
        }      
    }    //End Function
}   //End Class
