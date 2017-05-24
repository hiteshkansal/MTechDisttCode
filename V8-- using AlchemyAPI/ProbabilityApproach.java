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

public class ProbabilityApproach 
{
    public static double count(int k, Final_Opinion_Polarity FOP)
    {
        int i=0, total=0;
        while(FOP.opinion[i]!=null)
        {
            if(FOP.polarity[i]==k)
            {
                total++;
            }
            i++;
        }
        return total;
    }
    public static void nounlabel(Final_Opinion_Polarity FOP, HashMap<String, WordPolarity> featureRecord)  // find how many times each feature is positive or negative
    {
        int i=0;
        while(FOP.feature[i]!=null)
        {
            WordPolarity wp = featureRecord.get(FOP.feature[i]);
            if(wp==null)
            {
                wp = new WordPolarity();
            }
            if(FOP.polarity[i]==1)
            {
                wp.pos++;
            }
            else if(FOP.polarity[i]==-1)
            {
                wp.neg++;
            }
            else
            {
                wp.neutral++;
            }
            wp.input = FOP.feature[i];
            wp.total = wp.pos+wp.neg+wp.neutral;
            featureRecord.put(FOP.feature[i], wp);
            i++;
        }
    }
    
    public static void probabilityApproach(Final_Opinion_Polarity FOP, HashMap<String, WordPolarity> polarity, HashMap<String, WordPolarity> featureRecord, HashMap<String, WordPolarity> wrdlbl)
    {
        int i=0;
        double x, z, y_pos, y_neg, xyz_pos, xyz_neg, xz, yz_pos, yz_neg,xy_pos,xy_neg,pos_result,neg_result;
        y_pos = count(1,FOP);
        y_neg = count(-1,FOP);
        while(FOP.feature[i]!=null)
        {
            x = 0;              //total target word count
            z = 0;              //total feature count P(z)
            xyz_pos = 0;        //total positive feature-opinion count P(x,y,z) where y = positive
            xyz_neg = 0;        //total negative feature-opinion count P(x,y,z) where y = negative
            xz = 0;             //total feature-opinion count P(xz)
            yz_neg = 0;         //count of feature with negative label P(yz)
            yz_pos = 0;         //count of feature with positive label P(yz)
            xy_neg = 0;
            xy_pos = 0;
            pos_result = 0;
            neg_result = 0;
            if(FOP.polarity[i]==0)
            {
                String key = FOP.feature[i]+FOP.opinion[i];
                WordPolarity t1;
                if(featureRecord.containsKey(FOP.feature[i]))
                {
                    t1 = featureRecord.get(FOP.feature[i]);
                    z = t1.total;
                    yz_pos = t1.pos;
                    yz_neg = t1.neg;
                }
                if(polarity.containsKey(key))
                {
                    t1 = polarity.get(key);
                    xz = t1.total;
                    xyz_pos = t1.pos;
                    xyz_neg = t1.neg;
                }
                if(wrdlbl.containsKey(FOP.opinion[i]))
                {
                    t1 = polarity.get(key);
                    x = t1.total;
                    xy_pos = t1.pos;
                    xy_neg = t1.neg;
                }
                //System.out.println("values: "+xyz_neg+" "+xyz_pos+" "+xz+ " "+yz_neg+" "+yz_pos+" "+z);
                //pos_result = xyz_pos*(Math.log((z*xyz_pos+1)/(xz*yz_pos+1)));
                //neg_result = xyz_neg*(Math.log((z*xyz_neg+1)/(xz*yz_neg+1)));
                double temp1, temp2;
                temp1 = xy_pos*yz_pos*xz;
                temp2 = x*y_pos*z*xyz_pos;
                pos_result = Math.log((temp1+1)/(temp2+1));
                
                temp1 = xy_neg*yz_neg*xz;
                temp2 = x*y_neg*z*xyz_neg;
                neg_result = Math.log((temp1+1)/(temp2+1));
                //System.out.println("values: "+pos_result+"   "+neg_result);
                if(pos_result > neg_result)
                {
                   // System.out.println("Sent no. "+FOP.sentnum.get(i)+"Positive: "+pos_result+"  "+neg_result+"  "+FOP.feature[i]+" "+FOP.opinion[i]);
                    if(FOP.ngtn[i]==true)
                    {
                        FOP.polarity[i] = -1;
                        y_neg++;
                    }
                    else
                    {
                        FOP.polarity[i] = 1;
                        y_pos++;
                    }
                }
                else if(neg_result > pos_result)
                {
                   // System.out.println("Sent no. "+FOP.sentnum.get(i)+"Negative: "+pos_result+"  "+neg_result+"  "+FOP.feature[i]+" "+FOP.opinion[i]);
                    if(FOP.ngtn[i]==true)
                    {
                        FOP.polarity[i] = 1;
                        y_pos++;
                    }
                    else
                    {
                        FOP.polarity[i] = -1;
                        y_neg++;
                    }
                }
            }
            i++;
        }
    }   //End Function
}   //End Class
