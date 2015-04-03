package MonitorsProblema1;

/**
 * General Description: This data type defines a generic memory.
 */
public abstract class MemObject {

    /**
     * Definition of generic memory.
     */
    protected Object[] mem;     // storage area

    /**
     * Variables builder.
     * @param nElem number of elements
     */
    protected MemObject(int nElem) {
        if (nElem > 0) {
            mem = new Object[nElem];
        }
    }

    /**
     * Writing a value. -- virtual method
     * @param val value that will be writing
     */
    protected abstract void write(Object val);

    /**
     * leitura de um valor -- método virtual
     */
    /**
     * Reading a value. -- virtual method
     * @return value read
     */
    protected abstract Object read();

    /**
     * Reads a value without removing it. -- virtual method
     * @return Object at the top of the queue (without removing it)
     */
    protected abstract Object peek();

    /**
     * Checks if the queue is empty. -- virtual method
     * @return State of the queue (Empty or not)
     */
    protected abstract boolean isEmpty();
}
