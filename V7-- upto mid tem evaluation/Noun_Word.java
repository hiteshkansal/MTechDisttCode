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

public class Noun_Word 
{
    String noun;
    int cnt;
    boolean checked;
    boolean flag;       //if in shared word grouping, 2nd word is bigger than we set this flag.
    public Noun_Word(String noun, int cnt) 
    {
        this.noun = noun;
        this.cnt = cnt;
        this.checked = false;
        this.flag = false;
    }
    public void increment()
    {
        cnt++;
    }
}
