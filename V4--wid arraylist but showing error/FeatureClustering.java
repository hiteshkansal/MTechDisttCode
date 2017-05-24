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

public class FeatureClustering 
{
    public static void clustering(HashMap<String, Cluster> positiveFeature, HashMap<String, Cluster> negativeFeature, Final_Opinion_Polarity FOP)
    {
        int i=0;
        while(FOP.feature[i]!=null)
        {
            Cluster cl = new Cluster();
            if(FOP.polarity[i]==1)
            {
                if(positiveFeature.containsKey(FOP.feature[i]))
                {
                    cl = positiveFeature.get(FOP.feature[i]);
                    cl.count++;
                }
                else
                {
                    cl.count = 1;
                    cl.feature = FOP.feature[i];
                }
                cl.sNo.add(Integer.parseInt(FOP.sentnum[i]));
                positiveFeature.put(FOP.feature[i], cl);
            }
            else if(FOP.polarity[i]==-1)
            {
                if(negativeFeature.containsKey(FOP.feature[i]))
                {
                    cl = negativeFeature.get(FOP.feature[i]);
                    cl.count++;
                }
                else
                {
                    cl.count = 1;
                    cl.feature = FOP.feature[i];
                }
                cl.sNo.add(Integer.parseInt(FOP.sentnum[i]));
                negativeFeature.put(FOP.feature[i], cl);
            }
            
            i++;
        }
    }   //End Function
    
}   //End Class
