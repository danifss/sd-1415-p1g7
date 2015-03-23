package MainProblema01;

import genclass.GenericIO;
import genclass.FileOp;
import MonitorsProblema1.*;

/**
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fName;       // Nome do ficheiro de log
        boolean success;	// Validacao dos dados de entrada
        char opt;			// opcao
        /* Inicializacao do Log */
        GenericIO.writelnString("\n" + "      Problema do Artesanato de Aveiro\n");
        GenericIO.writeString("Numero de iterações dos clientes? ");
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

            /* Inicializar e criar intervenientes */

            int nCraftsman = 3;									// Numero de Artesaos
            int nCustomers = 3;									// Numero de Clientes
            MonInfo repositorioGeral;							// Repositorio de informacao partilhada
            MonShop shop;										// Loja
            MonFactory factory;									// Fabrica
            MonStorage storage;									// Armazem
            Owner owner;										// Dona da loja
            Craftman[] craftman = new Craftman[nCraftsman];		// Array de threads de Artesaos
            Customer[] customer = new Customer[nCustomers];		// Array de threads de Clientes
            int nInitialPrimeMaterialsInStorage = 20;			// 
            int nPrimeMaterialsInFactory = 10;					// 
            int nProductsInShop = 0;							// 
            int nPrimeMaterialsByProduct = 1;					// 
            int nPrimeMaterialsForRestock = 10;					//
            int nLimitOfProductsInFactory = 50;					// 

            repositorioGeral = new MonInfo(															// Create general repository
                            nCraftsman,
                            nCustomers,
                            fName,
                            nPrimeMaterialsInFactory,
                            nProductsInShop
                            );					
            shop = new MonShop(repositorioGeral);													// Creating shop
            factory = new MonFactory(
                            nPrimeMaterialsInFactory,
                            nPrimeMaterialsByProduct,
                            nPrimeMaterialsForRestock);										// Creating Factory
            storage = new MonStorage();																// Creating Storage
            owner = new Owner(0, repositorioGeral, factory, shop);													// Create Owner

            for(int i=0;i<nCraftsman;i++)
                craftman[i] = new Craftman(i,factory, repositorioGeral);							// Create Craftsmans
            for(int i=0;i<nCustomers;i++)
                customer[i] = new Customer(repositorioGeral, i, shop);								// Create Customers

            /* Arranque da simulacao */
            owner.start();

            for(int i=0;i<nCraftsman;i++)
                craftman[i].start();
            for(int i=0;i<nCustomers;i++)
                customer[i].start();
    }
}
