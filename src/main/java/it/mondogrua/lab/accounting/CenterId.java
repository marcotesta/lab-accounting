package it.mondogrua.lab.accounting;

import java.util.List;

public class CenterId {

    public static final String SEPARATOR = ":";
    public static final String ROOT_NAME = "#";

    private final List<String> chunks;

    // Constructor -------------------------------------------------------------

    public CenterId(List<String> chunks) {
        if (chunks.size() < 1 || !chunks.get(0).equals(ROOT_NAME)) {
            throw new IllegalArgumentException("Center id should have at least the root node");
        }
        this.chunks = chunks;
    }

    // Public Methods ----------------------------------------------------------


    public int size() {
        return chunks.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chunks == null) ? 0 : chunks.hashCode());
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
        if (chunks == null) {
            if (other.chunks != null) {
                return false;
            } else {
                return true;
            }
        } else if (!chunks.equals(other.chunks)) {

            if (chunks.size() != other.chunks.size()) {
                return false;
            }

            for (int i=0; i < chunks.size(); ++i) {
                if (! chunks.get(i).equals(other.chunks.get(i))) {
                    return false;
                }
            }

            return false;
        }
        return true;
    }

    public boolean startWith(CenterId that) {

        if (that.chunks.size() > this.chunks.size()) {
            return false;
        }

        for (int i = 0; i< that.chunks.size(); ++i) {
            if (!that.chunks.get(i).equals(this.chunks.get(i))) {
                return false;
            }
        }
        return true;
    }

    public CenterId trim(int size) {
        if (chunks.size() <= size) {
            throw new IndexOutOfBoundsException();
        }
        return new CenterId(chunks.subList(0, size));
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(chunks.get(0));

        if (chunks.size()>1) {
            res.append(chunks.get(1));
        }
        for (int i=2; i<chunks.size(); ++i) {
            res.append(SEPARATOR).append(chunks.get(i));
        }

        return res.toString();
    }
}
