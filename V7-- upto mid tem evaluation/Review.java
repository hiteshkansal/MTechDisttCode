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

public class Review 
{       
    public static void reviewBasedPolarity(Final_Opinion_Polarity FOP)
    {
        int index,k=0,r=0,total=0;
        Iterator it,it_sen;
        it = FOP.reviewID.iterator();
        while(it.hasNext())
        {
            index = (Integer)it.next();
            it_sen = FOP.sentnum.iterator();
            for(int kch=0;kch<r;kch++)
            {
                it_sen.next();
            }
            while(it_sen.hasNext() && FOP.sentnum.get(r)<index)       //This loop runs for 1 review each time.
            {
                    total=0;
                    if(FOP.polarity[r]==0)
                    {
                        for(int tmp=k;tmp<index;tmp++)
                        {
                            if(tmp==r)
                            {
                                continue;
                            }
                            total+=FOP.polarity[tmp];
                        }
                        if(total>0)
                        {
                            FOP.polarity[r] = 1;
                        }
                        else if(total<0)
                        {
                            FOP.polarity[r] = -1;
                        }
                    }
                    r++;
                    it_sen.next();
            }
            k=index;
        }
    }   //End Function
    /*
    public static void create_reviewHash(HashMap<Integer, Final_Opinion_Polarity> rev, Final_Opinion_Polarity FOP)
    {
        int index,rid=1,k=0,r=0;
        Final_Opinion_Polarity temp;
        Iterator it,it_sen;
        it = FOP.reviewID.iterator();
        it_sen = FOP.sentnum.iterator();
        while(it.hasNext())
        {
            k=0;
            temp = new Final_Opinion_Polarity();
            index = (Integer)it.next();
            while(it_sen.hasNext())// && FOP.sentnum.get(r)<index)       //This loop runs for 1 review each time.
            {
                if(FOP.sentnum.get(r)<index)
                {
                    temp.feature[k] = FOP.feature[r];
                    temp.opinion[k] = FOP.opinion[r];
                    temp.polarity[k] = FOP.polarity[r];
                    r++;
                    k++;
                }
                else
                {
                    break;
                }
                it_sen.next();
            }
            rev.put(rid, temp);
            rid++;
        }
        System.out.println("rid: "+rid);
        
    }   //End Function
    */
}   //End Class