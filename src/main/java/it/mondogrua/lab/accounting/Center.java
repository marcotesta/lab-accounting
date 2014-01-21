package it.mondogrua.lab.accounting;

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

    private final CenterId centerId;

    private final Center parent;

    private final Map<CenterId, Center> children = new HashMap<CenterId, Center>();

    private final Collection<Record> records = new ArrayList<Record>();

    private Strategy strategy;

    // Constructor -------------------------------------------------------------

    public Center(CenterId id, Center parent, Strategy strategy) {
        super();
        this.centerId = id;
        this.parent = parent;
        this.strategy = strategy;
    }

    // Public Methods ----------------------------------------------------------

    public boolean addRecord(Record record) {

        if (!record.getId().startWith(centerId)) {
            return false;
        }

        if (record.getId().equals(centerId)) {
            records.add(record);
            return true;
        } else {
            return passToChildren(record);
        }
    }

    public Cost directCosts() {
        Cost costAmount = new Cost(centerId);
        for (Record record : records) {
            record.addCost(costAmount);
        }
        return costAmount;
    }

    public List<Cost> childrenCosts() {
        List<Cost> result = new ArrayList<Cost>();
        for (Center center : children.values()) {
            result.add(center.directCosts());
            result.addAll(center.childrenCosts());
        }
        return result;
    }

    public Money childrenTotalDirectCosts() {
        Money result = new Money(0);
        for (Center center : children.values()) {
            result = result.add(center.totalDirectCosts());
        }
        return result;
    }

    public Money totalDirectCosts() {
        Money result = directCosts().getValue();
        result = result.add(childrenTotalDirectCosts());
        return result;
    }

    public Money strictlyCosts() {
        Money strictlyDirectCosts = directCosts().getValue();
        Money indirectCosts = ancestorIndirectCosts();

        Money strictlyCosts = strictlyDirectCosts.add(indirectCosts);

        return strictlyCosts;
    }

    public Money parentIndirectCosts() {
        return parent.strictlyIndirectCostsFor(centerId);
    }

    public Money ancestorIndirectCosts() {
        return parent.totalIndirectCostsFor(centerId);
    }

    @Override
    public String toString() {
        return centerId.toString();
    }

    // Package Methods ---------------------------------------------------------

    boolean hasChild(CenterId centerId) {
        return children.containsKey(centerId);
    }

    Center get(CenterId centerId) {
        return children.get(centerId);
    }

    // Private Methods ---------------------------------------------------------

    private Money strictlyIndirectCostsFor(CenterId centerId) {
        return strategy.computeStrictlyIndirectCosts(this, centerId);
    }

    private Money totalIndirectCostsFor(CenterId centerId) {
        return strategy.computeTotalIndirectCosts(this, centerId);
    }

//    private void addTotalCostsTo(Cost costAmount) {
//        costAmount.add(directCosts().getValue());
//
//        for (Center center : children.values()) {
//            center.addTotalCostsTo(costAmount);
//        }
//    }

    private boolean passToChildren(Record record) {
        for (Center child : children.values()) {
            if (child.addRecord(record)) {
                return true;
            }
        }

        Center newChild = addNewChild(record.getId());
        newChild.addRecord(record);

        return false;
    }

    private Center addNewChild(CenterId id) {
        CenterId nextId = id.trim(this.centerId.size());
        Center center = new Center(nextId, this, strategy);
        children.put(center.centerId, center);
        return center;
    }



}
