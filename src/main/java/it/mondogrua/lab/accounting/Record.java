package it.mondogrua.lab.accounting;


public class Record {

    private final CenterId centerId;

    private final Money cost;

    // Constructor ------------------------------------------------------------

    public Record(CenterId centerId, Money cost) {
        super();
        this.centerId = centerId;
        this.cost = cost;
    }

    // Public Methods ---------------------------------------------------------

    public Money getCost() {
        return cost;
    }

    public void addCost(Amount amount) {
        amount.add(cost);
    }

    public CenterId getId() {
        return centerId;
    }

}
