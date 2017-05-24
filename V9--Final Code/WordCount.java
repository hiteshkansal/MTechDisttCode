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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class WordCount 
{
        public static float wordCount(String fileName) throws Exception 
        {
                BufferedReader br = new BufferedReader(new FileReader("E:\\Disertation\\Reviews\\Texts\\"+fileName));
                String line = "", str = "";
                float count = 0;
                while ((line = br.readLine()) != null) 
                {
                        str += line + " ";
                }
                StringTokenizer st = new StringTokenizer(str);
                while (st.hasMoreTokens()) 
                {
                        String s = st.nextToken();
                        count++;
                }
                return count;
        }
}