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
}
