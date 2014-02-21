package it.mondogrua.lab.accounting;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 */
public class Accounting
{
    private static final String DEFAULT_FILE = "transactions.txt";

    private Center _root;
    private Factory _factory;
    private TransactionFile _transactionFile;

    public Accounting() {
        _factory = new Factory();
        _root = _factory.createRootCenter();
        String pathname = DEFAULT_FILE;
        _transactionFile = new TransactionFile(pathname, _factory);
    }

    public void load() throws IOException {

        _transactionFile.load(_root);
    }


    public static void main( String[] args )
    {
        try {
            Accounting accounting = new Accounting();
            accounting.load();

            System.out.println( "Write center id: " );
            Scanner in = new Scanner(System.in);
            String centerId = in.next();
            in.close();

            accounting.printReportFor(centerId, System.out);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void printReportFor(String centerId, PrintStream out) {
        Report report = _root.getReport(centerId);
        out.print( report.toString() );
    }
}
