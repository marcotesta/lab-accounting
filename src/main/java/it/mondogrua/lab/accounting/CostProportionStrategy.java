package it.mondogrua.lab.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CostProportionStrategy implements Strategy {

    private final Center _center;

    // Constructor -------------------------------------------------------------

    public CostProportionStrategy(Center center) {
        super();
        this._center = center;
    }

    // Public Methods ----------------------------------------------------------

    public List<CashFlow> indirectCostsFor(CenterId childId) {
        ArrayList<CashFlow> childIndirectCosts = new ArrayList<CashFlow>();

        CashFlow directCosts = _center.directCosts();
        childIndirectCosts.add(reallocateCosts(directCosts, childId));

        List<CashFlow> indirectCosts = _center.indirectCosts();
        for (CashFlow cashFlow : indirectCosts) {
            childIndirectCosts.add(reallocateCosts(cashFlow, childId));
        }

        return childIndirectCosts;
    }

    // Private Methods ---------------------------------------------------------

    private CashFlow reallocateCosts(CashFlow costs, CenterId centerId) {
        assert _center.hasChild(centerId);

        CashFlow reallocatedCosts;

        BigDecimal reallocationFactor = reallocationFactor(centerId);
        reallocatedCosts = costs.divide(reallocationFactor);

        return reallocatedCosts;
    }

    private BigDecimal reallocationFactor(CenterId centerId) {
        assert _center.hasChild(centerId);
        Money childrenTotalDirectCosts = _center.childrenTotalDirectCosts();
        CashFlow childTotalDirectCosts = _center.childTotalDirectCosts(centerId);
        BigDecimal reallocationFactor = childTotalDirectCosts.divide(childrenTotalDirectCosts);
        return reallocationFactor;
    }


}
