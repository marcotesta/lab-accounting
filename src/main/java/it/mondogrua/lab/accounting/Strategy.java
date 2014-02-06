package it.mondogrua.lab.accounting;

import java.util.ArrayList;
import java.util.List;

public interface Strategy {

    public static final Strategy EMPTY = new Strategy() {

        public List<CashFlow> indirectCostsFor(CenterId centerId) {
            return new ArrayList<CashFlow>();
        }
    };

    public List<CashFlow> indirectCostsFor(CenterId centerId);

}