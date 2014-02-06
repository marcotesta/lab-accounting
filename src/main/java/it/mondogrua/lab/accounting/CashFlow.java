package it.mondogrua.lab.accounting;

import java.math.BigDecimal;

public class CashFlow {

    public static final CashFlow EMPTY = new CashFlow(CenterId.EMPTY) {
        @Override
        public CashFlow add(Money money) {
            return this;
        }
    };

    // Instance Member Fields --------------------------------------------------

    private final CenterId _centerId;

    private Money _money = Money.ZERO;

    // Constructor -------------------------------------------------------------

    public CashFlow(CenterId centerId) {
        this._centerId = centerId;
    }

    // Overridden Object Methods -----------------------------------------------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_centerId == null) ? 0 : _centerId.hashCode());
        result = prime * result + ((_money == null) ? 0 : _money.hashCode());
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
        CashFlow other = (CashFlow) obj;
        if (_centerId == null) {
            if (other._centerId != null) {
                return false;
            }
        } else if (!_centerId.equals(other._centerId)) {
            return false;
        }
        if (_money == null) {
            if (other._money != null) {
                return false;
            }
        } else if (!_money.equals(other._money)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CashFlow [centerId=" + _centerId + ", money=" + _money + "]";
    }

    // Public Methods ----------------------------------------------------------

    public CashFlow add(Money money) {
        _money = this._money.add(money);
        return this;
    }

    public CashFlow divide(BigDecimal denominator) {
        _money = _money.divide(denominator);
        return this;
    }

    public BigDecimal divide(Money denominator) {
        return _money.divide(denominator);
    }

    public CashFlow multiply(BigDecimal multiplicand) {
        _money = _money.multiply(multiplicand);
        return this;
    }

    public Money getValue() {
        return _money;
    }

    public CenterId getId() {
        return _centerId;
    }

}
