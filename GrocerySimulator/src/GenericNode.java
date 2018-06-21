public class GenericNode<T> {

    private T data;
    private GenericNode<T> nextNode;

    public GenericNode(T data, GenericNode<T> node) {
        this.data = data;
        this.nextNode = node;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public GenericNode<T> getNextNode() {
        return nextNode;
    }

    public void setNextNode(GenericNode<T> nextNode) {
        this.nextNode = nextNode;
    }

    public void addNodeAfter(T element) {
        nextNode = new GenericNode(element, nextNode);
    }

    public void removeNodeAfter() {
        nextNode = nextNode.nextNode;
    }

}
