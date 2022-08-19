package jackli.HttpSender.config;

import java.io.Serializable;

public class Pair<T,D> implements Serializable {
    public T first;
    public D second;

    public Pair(T first, D second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(D second) {
        this.second = second;
    }

    public D getSecond() {
        return second;
    }

    public T getFirst() {
        return first;
    }

    @Override
    public int hashCode() {
        return first.hashCode() * 13 + (second == null ? 0 : second.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            if (first != null ? !first.equals(pair.first) : pair.first != null) return false;
            if (second != null ? !second.equals(pair.second) : pair.second != null) return false;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pair<" + first +
                ", " + second +
                '>';
    }
}
