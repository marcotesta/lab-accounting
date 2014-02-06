package it.mondogrua.lab.accounting;

import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {

    private final CenterId _centerId;

    private CashFlow _directCosts;

    private List<CashFlow> _childrenDirectCosts;

    private CashFlow _totalDirectCosts;

    private List<CashFlow> _indirectCosts;

    // Constructor -------------------------------------------------------------

    public ReportBuilder(CenterId centerId) {
        _centerId = centerId;
    }

    // Public Methods ----------------------------------------------------------

    public void setDirectCosts(CashFlow directCosts) {
        _directCosts = directCosts;
    }

    public void setChildrenDirectCosts(List<CashFlow> childrenDirectCosts) {
        _childrenDirectCosts = childrenDirectCosts;
    }

    public void setTotalDirectCosts(CashFlow totalDirectCosts) {
        _totalDirectCosts = totalDirectCosts;
    }

    public void setIndirectCosts(List<CashFlow> indirectCosts) {
        _indirectCosts = indirectCosts;
    }

    public Report getReport() {
        return new Report(this);
    }

    // Package Protected Methods -----------------------------------------------

    CashFlow getDirectCosts() {
        if (_directCosts != null) {
            return _directCosts;
        } else {
            return new CashFlow(_centerId);
        }
    }

    CenterId getCenterId() {
        return _centerId;
    }

    List<CashFlow> getChildrenDirectCosts() {
        if (_directCosts != null) {
            return _childrenDirectCosts;
        } else {
            return new ArrayList<CashFlow>();
        }
    }

    List<CashFlow> getIndirectCosts() {
        if (_indirectCosts != null) {
            return _indirectCosts;
        } else {
            return new ArrayList<CashFlow>();
        }
    }

    CashFlow getTotalDirectCosts() {
        if (_totalDirectCosts != null) {
            return _totalDirectCosts;
        } else {
            return new CashFlow(_centerId);
        }
    }


}
