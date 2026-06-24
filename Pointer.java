package toolbox;

import java.io.Serializable;

public class Pointer<T> implements java.lang.Cloneable, Serializable {
    public T value;
    public Pointer(T value) {
        this.value = value;
    }
    public Pointer(){
    }
    @Override
    public String toString() {
        return "<" + value.toString() + ">";
    }
    @SuppressWarnings("unchecked")
    public Pointer<T> clone() {
        try {
            return (Pointer<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen
        }
    }
    @Override
    public boolean equals(Object obj) {
        return value==obj;
    }
}
