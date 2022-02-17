package bluecow;

import java.util.Comparator;

class ComparatorByMoney implements Comparator<Capital>{

    @Override
    public int compare(Capital o1, Capital o2) {
        return ((int)(o2.getMoney()-o1.getMoney()));
    }
    
}
