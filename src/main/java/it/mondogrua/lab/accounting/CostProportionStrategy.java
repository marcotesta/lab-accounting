package it.mondogrua.lab.accounting;

public class CostProportionStrategy implements Strategy {

    public Money computeStrictlyIndirectCosts(Center center, CenterId centerId) {
        if (! center.hasChild(centerId)) {
            throw new IllegalArgumentException("'" + centerId +"' is not a sub-center of '" + center + "'");
        }

        Money strictlyDirectCosts = center.directCosts().getValue();

        Double ratio = computeChildRatio(center, centerId);

        return strictlyDirectCosts.times(ratio);
    }

    public Money computeTotalIndirectCosts(Center center, CenterId centerId) {
        if (! center.hasChild(centerId)) {
            throw new IllegalArgumentException("'" + centerId +"' is not a sub-center of '" + center + "'");
        }

        Money strictlyCosts = center.strictlyCosts();

        Double ratio = computeChildRatio(center, centerId);

        return strictlyCosts.times(ratio);
    }

    // Private Methods ---------------------------------------------------------

    private Double computeChildRatio(Center center, CenterId centerId) {
        assert( center.get(centerId) != null);

        Center child = center.get(centerId);

        Money childrenTotalCosts = center.childrenTotalDirectCosts();
        Money branchCost = child.totalDirectCosts();

        return branchCost.div(childrenTotalCosts);
    }
}
