package it.mondogrua.lab.accounting;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 *
 */
public class Money implements Comparable<Money> {

    public static final Money ZERO = new Money(new BigDecimal(0));

    private final BigDecimal value;

    // Constructor -------------------------------------------------------------

    public Money(BigDecimal value) {
        super();
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    // Overridden Object Methods ----------------------------------------------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Money other = (Money) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Money [value=" + value + "]";
    }

    // Overridden Comparable Methods -------------------------------------------

    public int compareTo(Money that) {
        return value.compareTo(that.value);
    }

    // Public Methods ----------------------------------------------------------

    public boolean isCost() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     *
     * @param  augend value to be added to this {@code Money}.
     * @return
     */
    public Money add(Money augend) {
        return new Money(value.add(augend.value));
    }

    public Money divide(BigDecimal divisor) {
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("divided by zero");
        }
        return new Money(value.divide(divisor, 2, RoundingMode.HALF_UP));
    }

    public BigDecimal divide(Money divisor) {
        if (divisor.compareTo(Money.ZERO) == 0) {
            throw new IllegalArgumentException("divided by zero");
        }
        return value.divide(divisor.value, 2, RoundingMode.HALF_UP);
    }

    public Money multiply(BigDecimal multiplicand) {
        return new Money(value.multiply(multiplicand).setScale(2, RoundingMode.HALF_UP));
    }


}
