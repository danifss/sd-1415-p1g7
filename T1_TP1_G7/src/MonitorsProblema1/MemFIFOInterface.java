/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonitorsProblema1;

/**
 *
 * @author Daniel
 */
public interface MemFIFOInterface {

    /**
     * @return Estado da fila. (Vazia ou nao)
     */
    boolean isEmpty();

    /**
     * @return Objeto no topo da fila. (sem o tirar)
     */
    Object peek();

    /**
     * fifo out -- leitura de um valor
     */
    Object read();

    /**
     * remove da fila um dado objeto
     */
    boolean remove(Object object);

    /**
     * fifo in -- escrita de um valor
     */
    void write(Object val);
    
}
