package toolbox;

import java.text.Collator;
import java.util.Collection;

public interface Owner<A extends Ownable<? extends Owner<A>>> {
    
    public default void recieve(A o) {
        recieve(o, false);
    }
    public void recieve(A o, boolean echo);
    public Collection<A> getOwned();
    public <T extends A> Collection<T> getOwnedOfType(Class<T> tClass);
    public <T extends A> Collection<T> getOwnedOfType(T refrence);
    public default String getRecursiveView() {
        StringBuilder builder = new StringBuilder("[");
        for (A o : getOwned()) {
            builder.append(o.getRecursiveView());
            builder.append(",");
        }
        if (builder.length() > 1) {
            builder.replace(builder.length() - 1, builder.length(), "]");
        } else {
            builder.append("]");
        }
        return builder.toString();
    }
    
}
