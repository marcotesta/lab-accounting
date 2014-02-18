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
 * - _costReallocator:Strategy. A strategy which knows how to compute the
 *   indirect costs for a given child.
 */
public class Center {

    /**
     * Represent a null Center object.
     */
    public static final Center NULL = new Center(CenterId.EMPTY, null, null) {
        @Override
        protected List<CashFlow> indirectCostsFor(CenterId centerId) {
            return new ArrayList<CashFlow>();
        }
    };

    /**
     * The unique identifier of an instance.
     */
    private final CenterId _id;

    /**
     * collaborator. The parent center for which this center is a sub-center
     * to whom the instance delegates the 'indirect costs' request.
     */
    private final Center _parent;

    /**
     * collaborator. The sub-centers to which the instance delegates the
     * 'sub-center direct costs' request.
     */
    private final Map<CenterId, Center> _children = new HashMap<CenterId, Center>();

    /**
     * The records belonging to the center.
     */
    private final Collection<Transaction> _records = new ArrayList<Transaction>();

    /**
     * collaborator: A strategy which knows how to compute the
     * indirect costs reallocation for a given child.
     */
    private Strategy _costReallocator;

    /**
     * collaborator: create the sub-centers.
     */
    private final Factory _factory;

    // Constructor -------------------------------------------------------------

    public Center(CenterId id, Center parent, Factory factory) {
        super();
        _id = id;
        _parent = parent;
        _factory = factory;
        _costReallocator = Strategy.EMPTY;
    }

    // Public Methods ----------------------------------------------------------

    /**
     *
     * @param transaction
     * @return True if the transaction has been added to this center instance
     * or one of its children. If the transaction belongs to a sub-center, but
     * the sub-center is not actually a child of the instance, than the center
     * is created and added as a child. False if the transaction does not
     * belong to this branch i.e. this instance or any of its children.
     */
    public boolean add(Transaction transaction) {

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

    /**
     * Chain of responsibility. Returns the Report if the instance is able to
     * generate one for the given center id, or pass the request to its
     * children. Returns a null Report if it
     * @param centerId
     * @return
     */
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

        return _costReallocator.indirectCostsFor(centerId);
    }

    protected CenterId getId() {
        return _id;
    }

    protected void setStrategy(Strategy strategy) {
        _costReallocator = strategy;
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
        assert (transaction.getId().startWith(_id) &&
                !transaction.getId().equals(_id));

        for (Center child : _children.values()) {
            if (child.add(transaction)) {
                return true;
            }
        }

        CenterId nextId = transaction.getId().trim(_id.size()+1);
        Center newChild = addNewChild(nextId);

        newChild.add(transaction);

        return true;
    }

    private Center addNewChild(CenterId id) {
        assert (id.startWith(_id) &&
                _id.size() +1 == id.size() &&
                !hasChild(id));

        Center child = _factory.createCenter(id, this);
        addChild(child);
        return child;
    }

    private Center addChild(Center center) {
        assert (center.getId().startWith(_id) &&
                _id.size() +1 == center.getId().size() &&
                !hasChild(center.getId()));

        _children.put(center._id, center);
        return center;
    }

}
