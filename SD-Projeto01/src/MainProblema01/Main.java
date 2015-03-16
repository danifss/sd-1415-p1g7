package MainProblema01;

import genclass.GenericIO;
import genclass.FileOp;
import MonitorsProblema1.*;

/**
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		int nCraftsman = 3;									// Numero de Artesaos
		int nClients = 3;									// Numero de Clientes
		int nShops = 1;										// Numero de Lojas
		MonShop shop;										// Loja
		MonFactory factory;									// Fabrica
		Owner owner;										// Dona da loja
		Craftman[] craftman = new Craftman[nCraftsman];		// Array de threads de Artesaos
		Customer[] customer = new Customer[nClients];		// Array de threads de Clientes
		int nIter;											// Numero de iteracoes do ciclo de vida dos clientes
		String fName;										// Nome do ficheiro de log
		boolean success;									// Validacao dos dados de entrada
		char opt;											// opcao

		/* Inicializacao */
		
		GenericIO.writelnString("\n" + "      Problema do Artesanato de Aveiro\n");
		GenericIO.writeString("Numero de iterações dos clientes? ");
		nIter = GenericIO.readlnInt();
		do {
			GenericIO.writeString("Nome do ficheiro de armazenamento da simulação? ");
			fName = GenericIO.readlnString();
			if (FileOp.exists(".", fName)) {
				do {
					GenericIO.writeString("Já existe um directório/ficheiro com esse nome. Quer apagá-lo (s - sim; n - não)? ");
					opt = GenericIO.readlnChar();
				} while ((opt != 's') && (opt != 'n'));
				if (opt == 's') {
					success = true;
				} else {
					success = false;
				}
			} else {
				success = true;
			}
		} while (!success);

		/* Inicializar intervenientes */
		shop = new MonShop(nCraftsman, nClients, nShops, fName, nIter);
		factory = new MonFactory();
		owner = new Owner(0, factory, shop);
		for(int i=0;i<nCraftsman;i++)
			craftman[i] = new Craftman(i,factory);
		for(int i=0;i<nClients;i++)
			customer[i] = new Customer(i, shop, nIter);
		
		/* Arranque da simulacao */
		for(int i=0;i<nCraftsman;i++)
			craftman[i].start();
		for(int i=0;i<nClients;i++)
			customer[i].start();
		owner.start();
		
	}

}
