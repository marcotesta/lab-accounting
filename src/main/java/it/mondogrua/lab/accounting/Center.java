package it.mondogrua.lab.accounting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

    private final CenterId id;

    private final Center parent;

    private final Map<CenterId, Center> children = new HashMap<CenterId, Center>();

    private final Collection<Record> records = new ArrayList<Record>();

    private Strategy strategy;

    // Constructor -------------------------------------------------------------

    public Center(CenterId id, Center parent, Strategy strategy) {
        super();
        this.id = id;
        this.parent = parent;
        this.strategy = strategy;
    }

    // Public Methods ----------------------------------------------------------

    public boolean addRecord(Record record) {

        if (!record.getId().startWith(id)) {
            return false;
        }

        if (record.getId().equals(id)) {
            records.add(record);
            return true;
        } else {
            return passToChildren(record);
        }
    }

    public Money strictlyDirectCosts() {
        Amount costAmount = new Amount();
        for (Record record : records) {
            record.addCost(costAmount);
        }
        return costAmount.getValue();
    }

    public Money childrenTotalDirectCosts() {
        Amount costAmount = new Amount();
        for (Center center : children.values()) {
            center.addTotalCostsTo(costAmount);
        }
        return costAmount.getValue();
    }

    public Money totalDirectCosts() {
        Amount costAmount = new Amount();
        addTotalCostsTo(costAmount);
        return costAmount.getValue();
    }

    public Money strictlyCosts() {
        Money strictlyDirectCosts = strictlyDirectCosts();
        Money indirectCosts = ancestorIndirectCosts();

        Money strictlyCosts = strictlyDirectCosts.add(indirectCosts);

        return strictlyCosts;
    }

    public Money parentIndirectCosts() {
        return parent.strictlyIndirectCostsFor(id);
    }

    public Money ancestorIndirectCosts() {
        return parent.totalIndirectCostsFor(id);
    }

    public boolean isChild(CenterId centerId) {
        for (CenterId childId : children.keySet()) {
            if (childId.equals(centerId)) {
                return true;
            }
        }
        return false;
    }

    public Center get(CenterId centerId) {
        return children.get(centerId);
    }

    @Override
    public String toString() {
        return id.toString();
    }

    // Private Methods ---------------------------------------------------------

    private Money strictlyIndirectCostsFor(CenterId centerId) {
        return strategy.computeStrictlyIndirectCosts(this, centerId);
    }

    private Money totalIndirectCostsFor(CenterId centerId) {
        return strategy.computeTotalIndirectCosts(this, centerId);
    }

    private void addTotalCostsTo(Amount costAmount) {
        costAmount.add(strictlyDirectCosts());

        for (Center center : children.values()) {
            center.addTotalCostsTo(costAmount);
        }
    }

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
        CenterId nextId = id.trim(this.id.size());
        Center center = new Center(nextId, this, strategy);
        children.put(center.id, center);
        return center;
    }



}
