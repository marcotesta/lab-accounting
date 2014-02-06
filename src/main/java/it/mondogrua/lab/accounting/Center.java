package it.mondogrua.lab.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A center: cost center, a profit center or a responsibility center
 *
 * Responsibilities:
 * - Knows his children
 * - Create his children
 * - Holds records
 * - Knows the strictly directs costs amount
 * - Knows the direct cost amount of the children
 * - Knows the total direct costs, i.e. the strictly direct costs plus the
 *   children's direct costs.
 * - Computes the indirect costs for a child
 *
 * Collaborators:
 * - IndirectCostComputer: a strategy which knows how to compute the indirect
 *   costs for a given child.
 */
public class Center {

    public static final Center EMPTY = new Center(CenterId.EMPTY, null, null) {
        @Override
        protected List<CashFlow> indirectCostsFor(CenterId centerId) {
            return new ArrayList<CashFlow>();
        }
    };

    private final CenterId _id;

    private final Center _parent;

    private final Map<CenterId, Center> _children = new HashMap<CenterId, Center>();

    private final Collection<Transaction> _records = new ArrayList<Transaction>();

    /**
     * Collaborator: compute the cost reallocation
     */
    private Strategy _strategy;

    /**
     * Collaborator: create the sub-centers.
     */
    private final Factory _factory;

    // Constructor -------------------------------------------------------------

    public Center(CenterId id, Center parent, Factory factory) {
        super();
        _id = id;
        _parent = parent;
        _factory = factory;
        _strategy = Strategy.EMPTY;
    }

    // Public Methods ----------------------------------------------------------

    public boolean addRecord(Transaction transaction) {

        if (!transaction.getId().startWith(_id)) {
            return false;
        }

        if (transaction.getId().equals(_id)) {
            _records.add(transaction);
            return true;
        } else {
            return passToChildren(transaction);
        }
    }

    public Report getReport(String centerIdString) throws ParseException {
        return getReport(_factory.createCenterId(centerIdString));
    }

    public Report getReport(CenterId centerId) {

        if (!centerId.startWith(_id)) {
            return Report.EMPTY;
        }

        if (centerId.equals(_id)) {
            return getReport();
        } else {
            for (Center child : _children.values()) {
                Report report = child.getReport(centerId);
                if (!report.isEmpty()) {
                    return report;
                }
            }
            return Report.EMPTY;
        }
    }

    /**
     * @return The direct costs of the Center.
     */
    public CashFlow directCosts() {
        CashFlow costAmount = new CashFlow(_id);
        for (Transaction record : _records) {
            record.addCost(costAmount);
        }
        return costAmount;
    }

    /**
     *
     * @return the list of indirect cost belonging to the Center
     */
    public List<CashFlow> indirectCosts() {
        return _parent.indirectCostsFor(_id);
    }

    // Overridden Object Methods -----------------------------------------------

    @Override
    public String toString() {
        return _id.toString();
    }

    // Protected Methods ---------------------------------------------------------

    /**
    *
    * @param centerId
    * @return the list of the reallocated indirect cost belonging to the
    * sub-center with id centerId.
    */
    protected List<CashFlow> indirectCostsFor(CenterId centerId) {
        assert _children.containsKey(centerId);

        return _strategy.indirectCostsFor(centerId);
    }

    protected CenterId getId() {
        return _id;
    }

    protected void setStrategy(Strategy strategy) {
        _strategy = strategy;
    }

    protected boolean hasChild(CenterId centerId) {
        return _children.containsKey(centerId);
    }

    protected Money childrenTotalDirectCosts() {
        Money result = new Money(BigDecimal.ZERO);
        for (Center center : _children.values()) {
            result = result.add(center.totalDirectCosts().getValue());
        }
        return result;
    }

    protected CashFlow childTotalDirectCosts(CenterId centerId) {
        assert _children.containsKey(centerId);

        Center child = _children.get(centerId);
        return child.totalDirectCosts();
    }

    // Private Methods ---------------------------------------------------------

    private CashFlow totalDirectCosts() {
        CashFlow result = directCosts();
        result = result.add(childrenTotalDirectCosts());
        return result;
    }

    /**
     * @return the list of direct costs for the sub-centers.
     */
    private List<CashFlow> childrenDirectCosts() {
        List<CashFlow> result = new ArrayList<CashFlow>();
        for (Center center : _children.values()) {
            result.add(center.directCosts());
            result.addAll(center.childrenDirectCosts());
        }
        return result;
    }

    private Report getReport() {
        ReportBuilder builder = new ReportBuilder(_id);
        builder.setDirectCosts(directCosts());
        builder.setChildrenDirectCosts(childrenDirectCosts());
        builder.setTotalDirectCosts(totalDirectCosts());
        builder.setIndirectCosts(indirectCosts());
        return builder.getReport();
    }

    private boolean passToChildren(Transaction transaction) {
        assert (_id.startWith(transaction.getId()) &&
                !_id.equals(transaction.getId()));

        for (Center child : _children.values()) {
            if (child.addRecord(transaction)) {
                return true;
            }
        }

        CenterId nextId = transaction.getId().trim(_id.size()+1);
        Center newChild = addNewChild(nextId);

        newChild.addRecord(transaction);

        return true;
    }

    private Center addNewChild(CenterId id) {
        assert (_id.startWith(id) &&
                _id.size() +1 == id.size() &&
                !hasChild(id));

        Center child = _factory.createCenter(id, this);
        addChild(child);
        return child;
    }

    private Center addChild(Center center) {
        assert (_id.startWith(center.getId()) &&
                _id.size() +1 == center.getId().size() &&
                !hasChild(center.getId()));

        _children.put(center._id, center);
        return center;
    }

}
