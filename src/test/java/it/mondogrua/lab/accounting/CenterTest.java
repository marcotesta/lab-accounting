package it.mondogrua.lab.accounting;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CenterTest {

    private Factory factory;

    @Before
    public void before() {
        factory = new Factory();
    }

    @Test
    public void test_DirectCosts_CenterWithOneTransactionAndNoSubCenters() throws Exception {
        Transaction transaction = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);

        Center root = factory.createRootCenter();
        root.addRecord(transaction);

        CashFlow expected = factory.createCashFlow("#pomodorotechnique:libri:inglese", 0.50);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique:libri:inglese"));

        assertEquals(expected, report.getDirectCosts());
    }

    @Test
    public void test_DirectCosts_OverheadCostReportWhitOnlySubCenterCosts() throws Exception {
        Transaction transaction = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);

        Center root = factory.createRootCenter();
        root.addRecord(transaction);

        CashFlow expected = factory.createCashFlow("#", 0.0);

        Report report = root.getReport(factory.createCenterId("#"));

        assertEquals(expected, report.getDirectCosts());
    }

    @Test
    public void test_DirectCosts_CenterCostReportWhitOnlySubCenterCosts() throws Exception {
        Transaction transaction = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);

        Center root = factory.createRootCenter();
        root.addRecord(transaction);

        CashFlow expected = factory.createCashFlow("#pomodorotechnique", 0.0);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique"));

        assertEquals(expected, report.getDirectCosts());
    }

    @Test
    public void test_DirectCosts_CenterCostReportWhitManyTransactions() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);

        CashFlow expected = factory.createCashFlow("#pomodorotechnique:libri:inglese", 1.05);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique:libri:inglese"));

        assertEquals(expected, report.getDirectCosts());
    }

    @Test
    public void test_ChildrenDirectCosts_CenterWithOneTransactionAndNoSubCenters() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);

        List<CashFlow> expected = new ArrayList<CashFlow>();

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique:libri:inglese"));

        assertEquals(expected, report.getChildrenDirectCosts());
    }

    @Test
    public void test_ChildernDirectCosts_OverheadCostReportWhitOnlySubCenterCosts() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        CashFlow childDirectCosts = factory.createCashFlow("#pomodorotechnique:libri:inglese", 0.50);
        expected.add(childDirectCosts);

        Report report = root.getReport(factory.createCenterId("#"));

        assertEquals(expected, report.getChildrenDirectCosts());
    }

    @Test
    public void test_ChildrenDirectCosts_CenterCostReportWhitOnlySubCenterCosts() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        CashFlow childDirectCosts = factory.createCashFlow("#pomodorotechnique:libri:inglese", 0.50);
        expected.add(childDirectCosts);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique"));

        assertEquals(expected, report.getChildrenDirectCosts());
    }

    @Test
    public void test_TotalDirectCosts() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72", "Bolletta luce Maggio-Giugno 2013", "#", 2.55);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);

        CashFlow expected = factory.createCashFlow("#");
        expected.add(new Money(new BigDecimal(3.60)));

        Report report = root.getReport(factory.createCenterId("#"));

        assertEquals(expected, report.getTotalDirectCosts());

    }

    @Test
    public void test_TotalDirectCostsForMoreSubCenters() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72", "Bolletta luce Maggio-Giugno 2013", "#", 2.55);
        Transaction transaction4 = factory.createTransaction("C134", "Design sito", "#p2p", 2.80);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);
        root.addRecord(transaction4);

        CashFlow expected = factory.createCashFlow("#", 6.40);

        Report report = root.getReport("#");

        assertEquals(expected, report.getTotalDirectCosts());
    }

    @Test
    public void test_IndirectCostsWithoutAllocation() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72", "Bolletta luce Maggio-Giugno 2013", "#", 2.55);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        CashFlow indirectCosts1 = factory.createCashFlow("#");
        indirectCosts1.add(new Money(new BigDecimal(2.55)));
        expected.add(indirectCosts1);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique"));

        assertEquals(expected, report.getIndirectCosts());

    }

    @Test
    public void test_InheritedIndirectCostsWithoutAllocation() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72", "Bolletta luce Maggio-Giugno 2013", "#", 2.55);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        CashFlow indirectCosts1 = factory.createCashFlow("#");
        indirectCosts1.add(new Money(new BigDecimal(2.55)));
        expected.add(indirectCosts1);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique:libri:inglese"));

        assertEquals(expected, report.getIndirectCosts());

    }

    @Test
    public void test_IndirectCostsAllocatedByCostProportion() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72", "Bolletta luce Maggio-Giugno 2013", "#", 2.55);
        Transaction transaction4 = factory.createTransaction("C134", "Design sito", "#p2p", 2.80);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);
        root.addRecord(transaction4);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        CashFlow indirectCosts1 = factory.createCashFlow("#", 0.69);
        expected.add(indirectCosts1);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique"));

        assertEquals(expected, report.getIndirectCosts());

    }

    @Test
    public void test_IndirectCostsAllocatedByCostProportion_bis() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72", "Bolletta luce Maggio-Giugno 2013", "#", 2.55);
        Transaction transaction4 = factory.createTransaction("C134", "Design sito", "#p2p", 2.80);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);
        root.addRecord(transaction4);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        CashFlow indirectCosts1 = factory.createCashFlow("#");
        indirectCosts1.add(new Money(new BigDecimal(1.86)));
        expected.add(indirectCosts1);

        Report report = root.getReport(factory.createCenterId("#p2p"));

        assertEquals(expected, report.getIndirectCosts());

    }

    @Test
    public void test_IndirectCosts_InheritedButReallocatedOnlyOnce() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72", "Bolletta luce Maggio-Giugno 2013", "#", 2.55);
        Transaction transaction4 = factory.createTransaction("C134", "Design sito", "#p2p", 2.80);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);
        root.addRecord(transaction4);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        CashFlow indirectCosts1 = factory.createCashFlow("#");
        indirectCosts1.add(new Money(new BigDecimal(0.69)));
        expected.add(indirectCosts1);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique:libri:inglese"));

        assertEquals(expected, report.getIndirectCosts());

    }

    @Test
    public void test_IndirectCosts_InheritedAndReallocatedTwice() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72", "Bolletta luce Maggio-Giugno 2013", "#", 2.55);
        Transaction transaction4 = factory.createTransaction("C134", "Design sito", "#p2p", 2.80);
        Transaction transaction5 = factory.createTransaction("C201", "Traduzione", "#pomodorotechnique:libri:tedesco", 2.10);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);
        root.addRecord(transaction4);
        root.addRecord(transaction5);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        CashFlow indirectCosts1 = factory.createCashFlow("#", 0.45);
        expected.add(indirectCosts1);

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique:libri:inglese"));

        assertEquals(expected, report.getIndirectCosts());

    }

    @Test
    public void test_IndirectCosts_Many() throws Exception {
        Transaction transaction1 = factory.createTransaction("C201", "traduzione", "#pomodorotechnique:libri:inglese", 0.50);
        Transaction transaction2 = factory.createTransaction("C134", "promozione", "#pomodorotechnique:libri:inglese", 0.55);
        Transaction transaction3 = factory.createTransaction("C72",  "Bolletta luce Maggio-Giugno 2013", "#", 2.55);
        Transaction transaction4 = factory.createTransaction("C134", "Design sito", "#p2p", 2.80);
        Transaction transaction5 = factory.createTransaction("C201", "Traduzione", "#pomodorotechnique:libri:tedesco", 2.10);
        Transaction transaction6 = factory.createTransaction("C18",  "Stesura Cap.3", "#pomodorotechnique:libri", 3.10);

        Center root = factory.createRootCenter();
        root.addRecord(transaction1);
        root.addRecord(transaction2);
        root.addRecord(transaction3);
        root.addRecord(transaction4);
        root.addRecord(transaction5);
        root.addRecord(transaction6);

        List<CashFlow> expected = new ArrayList<CashFlow>();
        expected.add(factory.createCashFlow("#pomodorotechnique:libri", 1.02));
        expected.add(factory.createCashFlow("#", 0.58));

        Report report = root.getReport(factory.createCenterId("#pomodorotechnique:libri:inglese"));

        assertEquals(expected, report.getIndirectCosts());

    }
}
