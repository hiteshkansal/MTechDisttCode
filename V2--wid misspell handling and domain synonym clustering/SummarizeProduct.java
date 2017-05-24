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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class SummarizeProduct 
{
    public static void positiveSummery(HashMap<String, Cluster> positiveFeature, HashMap<String, Cluster> negativeFeature, HashMap<Integer, String> productSentence)
    {
        Collection<Cluster> cl = positiveFeature.values();
        Cluster cluster[] = new Cluster[100];
        int i1=0;
        for(Iterator itr = cl.iterator();itr.hasNext();)
	{
            cluster[i1] = (Cluster) itr.next();
            //System.out.println("cluster"+cluster[i1].count);
            i1++;
	}
        
        for(int i=0;cluster[i] != null; i++)
	{
            int max = i;
            for(int j=i+1;cluster[j] !=null; j++)
            {
		if(cluster[j].count>cluster[max].count)
		{
                    max = j;
		}
            }
            Cluster temp = cluster[i];
            cluster[i] = cluster[max];
            cluster[max] = temp;
        }

        int k=0;
        System.out.println("Cluster Count: ");
        while(cluster[k]!=null)
        {
            System.out.println(cluster[k].count);
            k++;
        }
        
        float productRating;
	float totalRating =0;
        int i=0;
	while(cluster[i]!=null)
	{
            float stars =0;
            int count = cluster[i].count;
            float total;
            if(negativeFeature.get(cluster[i].feature) != null)
            {
    		total = count + negativeFeature.get(cluster[i].feature).count;
            }
            else
            {
		total = count;
            }
            stars = count/total*5;
            totalRating = totalRating + stars;
			
            System.out.println(cluster[i].feature+"\t :"+stars+" Stars");
			
            System.out.println(cluster[i].feature+" with a count "+cluster[i].count);
            int lineNo = 1;
            for(Iterator itr = cluster[i].sNo.iterator();itr.hasNext();)
            {
		int n = (Integer) itr.next();
		System.out.println(lineNo+"  "+productSentence.get(n));
		//System.out.println("");
		lineNo++;
            }
            i++;
	}
	productRating = totalRating/i;
	System.out.println("OverAll Rating is : "+productRating+" Out of 5 Stars");
        
    }   //End Function
    
    public static void negativeSummery(HashMap<String, Cluster> negativeFeature, HashMap<Integer, String> productSentence)
    {
        Collection<Cluster> cl = negativeFeature.values();
        Cluster cluster[] = new Cluster[100];
        int i1=0;
        for(Iterator itr = cl.iterator();itr.hasNext();)
	{
            cluster[i1] = (Cluster) itr.next();
            //System.out.println("cluster"+cluster[i1].count);
            i1++;
	}
        System.out.println("Negative summery is: ");
        for(int i=0;cluster[i] != null; i++)
	{
            int max = i;
            for(int j=i+1;cluster[j] !=null; j++)
            {
		if(cluster[j].count>cluster[max].count)
		{
                    max = j;
		}
            }
            Cluster temp = cluster[i];
            cluster[i] = cluster[max];
            cluster[max] = temp;
        }
        
        for(int i = 0; i < 5 ; i++)
	{
            int lineNo = 1;
            for(Iterator itr = cluster[i].sNo.iterator();itr.hasNext();)
            {
		int n = (Integer) itr.next();
		System.out.println(lineNo+"  "+productSentence.get(n));
		System.out.println("");
		lineNo++;
            }
	}
    }   //End Function
    
}   //End Class
