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

import edu.cmu.lti.jawjaw.JAWJAW;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.MatrixCalculator;

public class WordNet
{
	
        private static final ILexicalDatabase db = new NictWordNet();
        
        // similarity calculators
        private static final RelatednessCalculator lin = new Lin(db);
        private static final RelatednessCalculator wup = new WuPalmer(db);
        private static final RelatednessCalculator hso = new HirstStOnge(db);
        private static final RelatednessCalculator lch = new LeacockChodorow(db);
        private static final RelatednessCalculator jcn = new JiangConrath(db);
        private static final RelatednessCalculator lesk = new Lesk(db);
        private static final Path path = new Path(db);
        private static final RelatednessCalculator res = new Resnik(db);
       
        
       
        public static double runHSO( String word1, String word2 ) {
        	return hso.calcRelatednessOfWords( word1, word2 );
                }
       
        
        public static double runLCH( String word1, String word2 ) {
                return lch.calcRelatednessOfWords( word1, word2 );
        }
       
       
        public static double runRES( String word1, String word2 ) {
                return res.calcRelatednessOfWords( word1, word2 );
        }
       
        
        public static double runJCN( String word1, String word2 ) {
                return jcn.calcRelatednessOfWords( word1, word2 );
        }
       
       
        public static double runLIN( String word1, String word2 ) {
                return lin.calcRelatednessOfWords( word1, word2 );
        }
       
       
        public static double runLESK( String word1, String word2 ) {
                return lesk.calcRelatednessOfWords( word1, word2 );
        }
       
        
        public static double runPATH( String word1, String word2 ) {
        	  
                return path.calcRelatednessOfWords( word1, word2 );
                
        }

       
        public static double runWUP( String word1, String word2 ) {
        	System.out.println(wup.calcRelatednessOfWords( word1, word2 ));
                return wup.calcRelatednessOfWords( word1, word2 );
        }
                               
        public static double[][] getSynonymyMatrix( String[] words1, String[] words2 ) {
                return MatrixCalculator.getSynonymyMatrix( words1, words2 );
        }
       
}
