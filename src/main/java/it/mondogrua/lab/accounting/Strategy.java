package it.mondogrua.lab.accounting;

public interface Strategy {

    public Money computeStrictlyIndirectCosts(Center center, CenterId centerId);

    public Money computeTotalIndirectCosts(Center center, CenterId centerId);

}