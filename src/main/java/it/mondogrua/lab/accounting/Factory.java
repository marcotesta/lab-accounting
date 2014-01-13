package it.mondogrua.lab.accounting;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Factory {

    // Public Methods ---------------------------------------------------------

    public Record createRecord(String centerIdString, Money cost) throws ParseException {
        CenterId centerId = createCenterId(centerIdString);
        return new Record(centerId, cost);
    }

    public CenterId createCenterId(String centerIdString) throws ParseException {
        List<String> id = convertCenterIdString(centerIdString);
        return new CenterId(id);
    }

    // Private Methods --------------------------------------------------------


    private List<String> convertCenterIdString(String centerIdString) throws ParseException {
        if (centerIdString == null ||
                centerIdString.length() < 1) {
            throw new ParseException("Centro id cannot be empty");
        }

        List<String> id = new ArrayList<String>();
        if (!centerIdString.substring(0, 1).equals(CenterId.ROOT_NAME)) {
            throw new InvalidParameterException("Id must start with '"+CenterId.ROOT_NAME+"'");
        }
        id.add(CenterId.ROOT_NAME);
        String[] tokens = centerIdString.substring(1).split(CenterId.SEPARATOR);
        for (String token : tokens) {
            id.add(token);
        }
        return id;
    }


}
