package it.mondogrua.lab.accounting;


public class Transaction {

    private final String _account;
    private String _description;
    private final CenterId _centerId;

    private final Money _money;

    // Constructor ------------------------------------------------------------

    public Transaction(String account, String description, CenterId centerId, Money money) {
        super();
        this._account = account;
        this._description = description;
        this._centerId = centerId;
        this._money = money;
    }

    // Public Methods ---------------------------------------------------------

    public Money getCost() {
        if (_money.isCost()) {
            return _money;
        } else {
            return Money.ZERO;
        }
    }

    public void addCost(CashFlow cost) {
        cost.add(_money);
    }

    public CenterId getId() {
        return _centerId;
    }

    public String getAccount() {
        return _account;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }


}
