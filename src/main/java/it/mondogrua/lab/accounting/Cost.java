package it.mondogrua.lab.accounting;

public class Cost {

    private final CenterId centerId;
    private Money money;

    public Cost(CenterId centerId) {
        this.centerId = centerId;
    }

    public void add(Money cost) {
        money = money.add(cost);
    }

    public Money getValue() {
        return money;
    }

}
