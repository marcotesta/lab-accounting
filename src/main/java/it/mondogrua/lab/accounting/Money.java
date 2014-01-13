package it.mondogrua.lab.accounting;

public class Money {

    private final double value;

    public Money(double value) {
        super();
        this.value = value;
    }

    public boolean isCost() {
        return value < 0;
    }

    public Money add(Money that) {
        return new Money(this.value + that.value);
    }

    public Double div(Money that) {
        if (that.value == 0.0) {

        }
        return value/that.value;
    }

    public Money times(Double ratio) {
        return new Money(this.value * ratio);
    }

}
