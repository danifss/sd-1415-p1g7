package MonitorsProblema1;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public interface MemFIFOInterface {

    /**
     * Checks if the queue is empty.
     * @return State of the queue (Empty or not)
     */
    boolean isEmpty();

    /**
     * Reads a value without removing it. 
     * @return Object at the top of the queue (without removing it)
     */
    Object peek();

    /**
     * FIFO out -- reading a value.
     * @return value read
     */
    Object read();

    /**
     * FIFO in -- writing a value.
     * @param val value that will be writing
     */
    void write(Object val);
    
}
