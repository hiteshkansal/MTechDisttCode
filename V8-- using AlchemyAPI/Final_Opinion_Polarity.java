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

import java.util.ArrayList;

public class Final_Opinion_Polarity {
    
    String feature[] = new String[2000];
    String opinion[] = new String[2000];
   // String sentnum[] = new String[2000];
    int polarity[] = new int[2000];
    ArrayList<Integer> sentnum = new ArrayList<>();
    ArrayList<Integer> reviewID = new ArrayList<>();      //Stores start of the next review.
    
    boolean flag[] = new boolean[2000]; //for totalCount function.
    
    boolean ngtn[] = new boolean[2000]; //for storing negation to use in probabilistic approach
    
    public Final_Opinion_Polarity()
    {
        for(int i=0;i<2000;i++)
        {
            flag[i] = false;
            ngtn[i] = false;
        }
    }
}
