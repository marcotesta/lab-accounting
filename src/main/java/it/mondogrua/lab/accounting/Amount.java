package it.mondogrua.lab.accounting;

public class Amount {

    private Money money;

    public void add(Money cost) {
        money = money.add(cost);
    }

    public Money getValue() {
        return money;
    }

}
