package MonitorsProblema1;

/**
 * Descrição geral: este tipo de dados define uma memória de tipo fifo derivada
 * a partir de uma memória genérica.
 */
public class MemFIFO extends MemObject {

    /**
     * Definição da memória de tipo fifo
     */
    private int inPnt, outPnt;                   // ponteiros de inserção e de retirada de um valor
    private boolean empty;                       // sinalização de memória vazia

    /**
     * Construtor de variáveis
     */
    public MemFIFO(int nElem) {
        super(nElem);
        inPnt = outPnt = 0;
        empty = true;
    }

    /**
     * fifo in -- escrita de um valor
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
     * fifo out -- leitura de um valor
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
     * @return Objeto no topo da fila. (sem o tirar)
     */
    @Override
    public Object peek() {
        if(isEmpty())
            return null;
        return mem[outPnt];
    }
	
	/**
	 * remove da fila um dado objeto
	 */
	@Override
	public boolean remove(Object object){ // nao sei se funciona
		int i = 0;
		boolean flag = false;
		for(i=0;i<mem.length;i++)
			if(mem[i] == object){
				flag = true;
				break;
			}
		if(flag){
			for(int j=i;j<mem.length-1;j++)
				mem[j] = mem[j+1];
			inPnt = (inPnt - 1) % mem.length;
			empty = (inPnt == outPnt);
		}
		return flag;
	}
	
    /**
     * @return Estado da fila. (Vazia ou nao)
     */
    @Override
    public boolean isEmpty() {
        return empty;
    }
}
