/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package final_dissertation;

/**
 *
 * @author Hitesh
 */
public class TotalCount 
{
    public static void totalCount(Final_Opinion_Polarity FOP)
    {
        int i=0, j=0, pos=0, neg=0, neut=0;
        System.out.println("Feature\t\tpositive\tnegative\tneutral");
        while(FOP.feature[i]!=null)
        {
            j=i;
            if(FOP.flag[i]==false)
            {
                while(FOP.feature[j]!=null)
                {
                    if(FOP.feature[i].equalsIgnoreCase(FOP.feature[j]))
                    {
                        if(FOP.polarity[j]==1)
                        {
                            pos++;
                        }
                        else if(FOP.polarity[j]==-1)
                        {
                            neg++;
                        }
                        else if(FOP.polarity[j]==0)
                        {
                            neut++;
                        }
                        FOP.flag[j]=true;
                    }
                   // System.out.print(FOP.sentnum.get(j));
                  //  System.out.print("   "+FOP.flag[j]);
                   // System.out.println("   "+pos);
                    j++;
                }
                System.out.print("Sent no."+ FOP.sentnum.get(i)+"\t");
                System.out.println(FOP.feature[i]+"\t\t"+pos+"\t"+neg+"\t"+neut);
            }
            pos = neg = neut = 0;
            i++;
        }
    }
}
