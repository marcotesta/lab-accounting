package it.mondogrua.lab.accounting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unique identifier of a Center.
 * Example: #pomodorotechnique:libri:inglese
 * Is immutable
 */
final public class CenterId {

    static final String SEPARATOR = ":";
    static final String ROOT_NAME = "#";
    private static final String EMPTY_NAME = "---";

    public static final CenterId ROOT =
            new CenterId(new ArrayList<String>(Arrays.asList(new String[]{ROOT_NAME})));

    public static final CenterId EMPTY =
            new CenterId(new ArrayList<String>(Arrays.asList(new String[]{EMPTY_NAME})));;

    // Instance Member Fields --------------------------------------------------

    private final List<String> _chunks;

    // Constructor -------------------------------------------------------------

    public CenterId(List<String> chunks) {
        if (chunks.size() < 1 || !(chunks.get(0).equals(ROOT_NAME) || chunks.get(0).equals(EMPTY_NAME))) {
            throw new IllegalArgumentException("Center id should have at least the root node");
        }
        _chunks = Collections.unmodifiableList(new ArrayList<String>(chunks));
    }

    // Overridden Object methods -----------------------------------------------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_chunks == null) ? 0 : _chunks.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CenterId other = (CenterId) obj;
        if (_chunks == null) {
            if (other._chunks != null) {
                return false;
            } else {
                return true;
            }
        } else if (!_chunks.equals(other._chunks)) {

            if (_chunks.size() != other._chunks.size()) {
                return false;
            }

            for (int i=0; i < _chunks.size(); ++i) {
                if (! _chunks.get(i).equals(other._chunks.get(i))) {
                    return false;
                }
            }

            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(_chunks.get(0));

        if (_chunks.size()>1) {
            res.append(_chunks.get(1));
        }
        for (int i=2; i<_chunks.size(); ++i) {
            res.append(SEPARATOR).append(_chunks.get(i));
        }

        return res.toString();
    }

    // Public Methods ----------------------------------------------------------

    public int size() {
        return _chunks.size();
    }

    public boolean startWith(CenterId that) {

        if (that._chunks.size() > this._chunks.size()) {
            return false;
        }

        for (int i = 0; i< that._chunks.size(); ++i) {
            if (!that._chunks.get(i).equals(this._chunks.get(i))) {
                return false;
            }
        }
        return true;
    }

    public CenterId trim(int size) {
        if (_chunks.size() < size) {
            throw new IndexOutOfBoundsException();
        }
        return new CenterId(_chunks.subList(0, size));
    }

    public CenterId add(String chunk) {
        if (chunk == null) {
            throw new NullPointerException("Invalid Center ID chunk: cannot be null");
        }
        if (chunk.equals(ROOT_NAME)) {
            throw new IllegalArgumentException("Invalid Center ID chunk: cannot be "+ ROOT_NAME);
        }

        List<String> thatChunks = new ArrayList<String>(_chunks);
        thatChunks.add(chunk);
        return new CenterId(thatChunks);
    }


}
