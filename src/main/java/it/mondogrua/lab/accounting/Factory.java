package it.mondogrua.lab.accounting;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

public class Factory {

    // Public Methods ---------------------------------------------------------

    public Transaction createTransaction(String account, String description,
            String centerIdString, Double val) throws ParseException {
        CenterId centerId = createCenterId(centerIdString);
        Money money = new Money(new BigDecimal(val));
        return new Transaction(account, description, centerId, money);
    }

    public CenterId createCenterId(String centerIdString) throws ParseException {
        if (centerIdString == null ||
                centerIdString.length() < 1) {
            throw new ParseException("Center ID cannot be empty");
        }

        if (!centerIdString.substring(0, 1).equals(CenterId.ROOT_NAME)) {
            throw new InvalidParameterException("ID must start with '"+CenterId.ROOT_NAME+"'");
        }

        if (centerIdString.length() == 1) {
            return CenterId.ROOT;
        }

        CenterId id = CenterId.ROOT;

        String[] tokens = centerIdString.substring(1).split(CenterId.SEPARATOR);
        for (String token : tokens) {
            id = id.add(token);
        }
        return id;
    }

    public Center createRootCenter() {
        Center center = new Center(CenterId.ROOT, Center.NULL, this);
        center.setStrategy(new CostProportionStrategy(center));
        return center;
    }

    public Center createCenter(CenterId id, Center parent) {
        if (parent.getId().startWith(id) &&
               parent.getId().size() +1 == id.size() &&
               !parent.hasChild(id)) {

            throw new IllegalArgumentException();
        }

        Center center = new Center(id, parent, this);
        center.setStrategy(new CostProportionStrategy(center));

        return center;
    }

    public CashFlow createCashFlow(String centerIdString) throws ParseException {
        return new CashFlow(createCenterId(centerIdString));
    }

    public CashFlow createCashFlow(String centerIdString, Double val) throws ParseException {
        CashFlow cashFlow = createCashFlow(centerIdString);
        cashFlow.add(new Money(new BigDecimal(val)));
        return cashFlow;
    }



    // Private Methods --------------------------------------------------------

}
