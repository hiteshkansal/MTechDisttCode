
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class opinion_lexicon 
{
    
    public static void build_dictionary(HashMap<String, Integer> dictionary, String positive, String negative) throws FileNotFoundException
    {
        File f1 = new File("E:\\Disertation\\APIs"+positive);
        Scanner s = new Scanner(f1);
        while(s.hasNext())
        {
            String val = s.next();
            if(!dictionary.containsKey(val))
            {
                dictionary.put(val,1);
            }
        }
        
        File f2 = new File("E:\\Disertation\\APIs"+negative);
        Scanner s2 = new Scanner(f2);
        while(s2.hasNext())
        {
            String val2 = s2.next();
            if(!dictionary.containsKey(val2))
            {
                dictionary.put(val2,-1);
            }
        }
    }    
}
