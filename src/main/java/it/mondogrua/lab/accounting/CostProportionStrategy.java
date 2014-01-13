package it.mondogrua.lab.accounting;

public class CostProportionStrategy implements Strategy {

    public Money computeStrictlyIndirectCosts(Center center, CenterId centerId) {
        if (! center.isChild(centerId)) {
            throw new IllegalArgumentException("'" + centerId +"' is not a sub-center of '" + center + "'");
        }

        Money strictlyDirectCosts = center.strictlyDirectCosts();

        Double ratio = computeChildRatio(center, centerId);

        return strictlyDirectCosts.times(ratio);
    }

    public Money computeTotalIndirectCosts(Center center, CenterId centerId) {
        if (! center.isChild(centerId)) {
            throw new IllegalArgumentException("'" + centerId +"' is not a sub-center of '" + center + "'");
        }

        Money strictlyCosts = center.strictlyCosts();

        Double ratio = computeChildRatio(center, centerId);

        return strictlyCosts.times(ratio);
    }

    // Private Methods ---------------------------------------------------------

    private Double computeChildRatio(Center center, CenterId centerId) {
        Center child = center.get(centerId);
        if (child == null) {
            throw new IllegalArgumentException(centerId + " is not a sub-center of " + center);
        }

        Money childrenTotalCosts = center.childrenTotalDirectCosts();
        Money branchCost = child.totalDirectCosts();

        return branchCost.div(childrenTotalCosts);
    }
}
