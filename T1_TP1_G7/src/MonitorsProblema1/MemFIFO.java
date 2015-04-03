package MonitorsProblema1;

/**
 * General Description: This data type defines a FIFO memory type derived 
 * from a generic memory.
 */
public class MemFIFO extends MemObject implements MemFIFOInterface {

    /**
     * Setting the FIFO memory type.
     */
    private int inPnt, outPnt;                   // insert and removal pointers of a value
    private boolean empty;                       // empty memory signaling

    /**
     * Variables builder.
     * @param nElem number of elements
     */
    public MemFIFO(int nElem) {
        super(nElem);
        inPnt = outPnt = 0;
        empty = true;
    }

    /**
     * FIFO in -- writing a value.
     * @param val value that will be writing
     */
    @Override
    public void write(Object val) {
        if ((inPnt != outPnt) || empty) {
            mem[inPnt] = val;
            inPnt = (inPnt + 1) % mem.length;
            empty = false;
        }
    }

    /**
     * FIFO out -- reading a value.
     * @return value read
     */
    @Override
    public Object read() {
        Object val = null;

        if (!empty) {
            val = mem[outPnt];
            outPnt = (outPnt + 1) % mem.length;
            empty = (inPnt == outPnt);
        }
        return val;
    }

    /**
     * Reads a value without removing it. 
     * @return Object at the top of the queue (without removing it)
     */
    @Override
    public Object peek() {
        if(isEmpty())
            return null;
        return mem[outPnt];
    }
	
    /**
     * Checks if the queue is empty.
     * @return State of the queue (Empty or not)
     */
    @Override
    public boolean isEmpty() {
        return empty;
    }
}
