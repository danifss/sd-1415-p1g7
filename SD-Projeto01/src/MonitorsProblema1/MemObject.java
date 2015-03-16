package MonitorsProblema1;

/**
 * Descrição geral: este tipo de dados define uma memória genérica.
 */
public abstract class MemObject {

	/**
	 * Definição da memória genérica
	 */
	protected Object[] mem;     // área de armazenamento

	/**
	 * Construtor de variáveis
	 */
	protected MemObject(int nElem) {
		if (nElem > 0) {
			mem = new Object[nElem];
		}
	}

	/**
	 * escrita de um valor -- método virtual
	 */
	protected abstract void write(Object val);

	/**
	 * leitura de um valor -- método virtual
	 */
	protected abstract Object read();
}
