/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluecow;

import java.util.Comparator;

/**
 *
 * @author S19001531
 */
class ComparatorByMoney implements Comparator<Capital>{

    @Override
    public int compare(Capital o1, Capital o2) {
        return ((int)(o2.getMoney()-o1.getMoney()));
    }
    
}
