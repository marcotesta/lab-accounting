package it.mondogrua.lab.accounting;

import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

/**
 * Responsibility:
 * - hold the data to be printed in the repo
 * - print the repo
 *
 */
public class Report {

    public static final Report EMPTY = new Report(new ReportBuilder(CenterId.EMPTY));

    // Instance Member Fields --------------------------------------------------
    private final CenterId _centerId;

    private CashFlow _directCosts;

    private CashFlow _totalDirectCosts;

    private List<CashFlow> _childrenDirectCosts;

    private List<CashFlow> _inidirectCosts;

    // Constructor -------------------------------------------------------------
    public Report(ReportBuilder builder) {
        _centerId = builder.getCenterId();
        _directCosts = builder.getDirectCosts();
        _childrenDirectCosts = builder.getChildrenDirectCosts();
        _totalDirectCosts = builder.getTotalDirectCosts();
        _inidirectCosts = builder.getIndirectCosts();
    }

    // Public Method -----------------------------------------------------------

    public boolean isEmpty() {
        return _centerId.equals(CenterId.EMPTY);
    }

    public CashFlow getDirectCosts() {
        return _directCosts;
    }

    public List<CashFlow> getChildrenDirectCosts() {
        List<CashFlow> notZeroCosts = select(_childrenDirectCosts, having(on(CashFlow.class).getValue(),
                not(equalTo(Money.ZERO))));
        return notZeroCosts;
    }

    public CashFlow getTotalDirectCosts() {
        return _totalDirectCosts;
    }

    public List<CashFlow> getIndirectCosts() {
        List<CashFlow> notZeroCosts = select(_inidirectCosts, having(on(CashFlow.class).getValue(),
                not(equalTo(Money.ZERO))));
        return notZeroCosts;
    }

    // Overridden Object Methods -----------------------------------------------

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Cost Center: ").append(_centerId).append("\n\n");
        builder.append("Direct Costs: ").append(_directCosts).append("\n");
        builder.append("Sub-Center Direct Costs: ").append("\n");
        if (_childrenDirectCosts.size() == 0) {
            builder.append("  ---").append("\n");
        }
        for (CashFlow childDirectCost : _childrenDirectCosts) {
            builder.append("  ").append(childDirectCost.getId()).append(": ").append(childDirectCost.getValue());
        }

        return builder.toString();
    }

}
