package toolbox;

public interface Ownable<A extends Owner<? extends Ownable<A>>> {
    public default String getRecursiveView() {
        return "{" + this.getClass().getName() + ":" + this.toString() + "}";
    }
    public default void give(A o) {
        give(o, false);
    }
    public void give(A o, boolean echo);
    public boolean kill();
    
}
