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
            int nInitialPrimeMaterialsInStorage = 20;			// Materias Primas no armazem inicialmente
            int nPrimeMaterialsInFactory = 10;					// Materias Primas na oficina inicialmente
            int nInitialProductsInShop = 0;						// Produtos na Loja inicialmente
            int nPrimeMaterialsByProduct = 1;					// Materia Prima por produto
            int nMinPrimeMaterialsForRestock = 10;				// Minimo de Materias Primas para o Restock
            int nProductsCollect = 10;                                      // Minimo de produtos para o Owner ir buscar
            int nLimitOfProductsInFactory = 50;					// Limite de produtos na oficina
            int nMaxPrimeMaterialsToDeliver = 40;               // Maximo de Materias Primas que pode ser entregue a oficina

            
            repositorioGeral = new MonInfo(										
                            nCraftsman,
                            nCustomers,
                            fName,
                            nPrimeMaterialsInFactory
                            ); // Create general repository
            shop = new MonShop(nInitialProductsInShop); // Creating shop
            factory = new MonFactory(
                            nPrimeMaterialsInFactory,
                            nPrimeMaterialsByProduct,
                            nMinPrimeMaterialsForRestock,
                            nLimitOfProductsInFactory,
                            nProductsCollect); // Creating Factory
            storage = new MonStorage(nInitialPrimeMaterialsInStorage, nMaxPrimeMaterialsToDeliver);																// Creating Storage
            owner = new Owner(repositorioGeral, factory, shop, storage); // Create Owner
            
            for(int i=0;i<nCraftsman;i++)
                craftman[i] = new Craftman(i,factory, shop, repositorioGeral); // Create Craftsmans
            for(int i=0;i<nCustomers;i++)
                customer[i] = new Customer(repositorioGeral, i, shop); // Create Customers
            
            /* Arranque da simulacao */
            owner.start();
            
            for(int i=0;i<nCraftsman;i++)
                craftman[i].start();
            for(int i=0;i<nCustomers;i++)
                customer[i].start();
    }
}
