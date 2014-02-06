package it.mondogrua.lab.accounting;


public class Transaction {

    private final String account;
    private String description;
    private final CenterId centerId;

    private final Money money;

    // Constructor ------------------------------------------------------------

    public Transaction(String account, String description, CenterId centerId, Money money) {
        super();
        this.account = account;
        this.description = description;
        this.centerId = centerId;
        this.money = money;
    }

    // Public Methods ---------------------------------------------------------

    public Money getCost() {
        if (money.isCost()) {
            return money;
        } else {
            return Money.ZERO;
        }
    }

    public void addCost(CashFlow cost) {
        cost.add(money);
    }

    public CenterId getId() {
        return centerId;
    }

    public String getAccount() {
        return account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
