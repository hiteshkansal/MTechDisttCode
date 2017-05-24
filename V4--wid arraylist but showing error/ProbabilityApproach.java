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

public class ProbabilityApproach {
    
    public static void probabilityApproach(Final_Opinion_Polarity FOP, HashMap<String, Integer> positive, HashMap<String, Integer> negative)
    {
        int i=0,pos,neg;
        while(FOP.feature[i]!=null)
        {
            pos = 0;
            neg = 0;
            if(FOP.polarity[i]==0)
            {
                String key = FOP.feature[i]+FOP.opinion[i];
                if(positive.containsKey(key))
                {
                    pos = positive.get(key);
                }
                if(negative.containsKey(key))
                {
                    neg = negative.get(key);
                }
                
                if(pos>neg)
                {
                    FOP.polarity[i] = 1;
                    positive.put(key,pos+1);
                }
                else if(neg>pos)
                {
                    FOP.polarity[i] = -1;
                    negative.put(key,neg+1);
                }
            }
            
            i++;
        }
        
    }   //End Function
}   //End Class
