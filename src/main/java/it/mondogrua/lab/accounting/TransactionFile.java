package it.mondogrua.lab.accounting;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class TransactionFile {

    private static final int TRANSACT_ELEM_NUM = 4;

    private String pathname = "";

    private final Factory _factory;

    // Constructor -------------------------------------------------------------

    public TransactionFile(String pathname, Factory factory) {
        super();
        this.pathname = pathname;
        _factory = factory;
    }

    // Public Methods ----------------------------------------------------------

    public void load(Center _root) throws IOException {
        File file = new File(pathname);
        CSVReader reader = new CSVReader(new FileReader(file ));

        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            try {
                Transaction transaction = createTransaction(nextLine);

                _root.add(transaction);
            } catch (NumberFormatException e) {
                continue;
            } catch (Exception e) {
                continue;
            }
        }
        reader.close();
    }

    // Private Methods ---------------------------------------------------------

    private Transaction createTransaction(String[] nextLine)
            throws NumberFormatException, Exception {

        if (nextLine.length != TRANSACT_ELEM_NUM) {
            throw new Exception("Invalid row");
        }
        String account = nextLine[0];
        String description = nextLine[1];
        String centerIdString = nextLine[3];
        Double val = Double.valueOf(nextLine[2]);
        Transaction transaction = _factory.createTransaction(account, description, centerIdString, val);
        return transaction;
    }
}
