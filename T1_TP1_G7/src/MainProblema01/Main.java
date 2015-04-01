package MainProblema01;

import genclass.GenericIO;
import static genclass.GenericIO.*;
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
        GenericIO.writelnString("\n" + "      Problema - Artesanato de Aveiro\n");
        do {
            GenericIO.writeString("Nome do ficheiro de armazenamento da simulação? ");
            fName = GenericIO.readlnString();
            if (FileOp.exists(".", fName)) {
                do {
                    GenericIO.writeString("Já existe um directório/ficheiro com esse nome. Quer apagá-lo (s - sim; n - não)? ");
                    opt = GenericIO.readlnChar();
                } while ((opt != 's') && (opt != 'n'));
                if (opt == 's') success = true;
                else success = false;
            } else if(fName == null) {
                fName = "log.txt";
                success = true;
            } else success = true;
        } while (!success);

        /* Inicializar e criar intervenientes */
        int nCraftsman = 3;									// Numero de Artesaos
        int nCustomers = 3;									// Numero de Clientes
        MonInfo sharedInfo;							// Repositorio de informacao partilhada
        MonShop shop;										// Loja
        MonFactory factory;									// Fabrica
        MonStorage storage;									// Armazem
        Owner owner;										// Dona da loja
        Craftman[] craftman = new Craftman[nCraftsman];		// Array de threads de Artesaos
        Customer[] customer = new Customer[nCustomers];		// Array de threads de Clientes
        int nInitialPrimeMaterialsInStorage = 20;			// Materias Primas no armazem inicialmente
        int nPrimeMaterialsInFactory = 10;					// Materias Primas na oficina inicialmente
        int nInitialProductsInShop = 0;						// Produtos na Loja inicialmente
        int nPrimeMaterialsByProduct = 2;					// Materia Prima por produto
        int nPrimeOwnerCarry = 10;          // Número de matérias primas que o Owner transporta de cada vez
        int nMinPrimeMaterialsForRestock = 10;				// Minimo de Materias Primas para o Restock
        int nMaxProductsCollect = 5;                       // Maximo de produtos que o Owner pode trazer de cada vez da oficina
        int nLimitOfProductsInFactory = 10;					// Limite de produtos na oficina

        writeString("Usar valores predefinidos?(s/n) ");
        if(readlnString().equalsIgnoreCase("n")){
            writeString("Nº inicial de materias primas no Armazem: ");
            nInitialPrimeMaterialsInStorage = Integer.parseInt(readlnString());
            writeString("Nº de materias primas na Oficina: ");
            nPrimeMaterialsInFactory = Integer.parseInt(readlnString());
            writeString("Nº inicial de produtos na Loja: ");
            nInitialProductsInShop = Integer.parseInt(readlnString());
            writeString("Nº de materias primas por Produto: ");
            nPrimeMaterialsByProduct = Integer.parseInt(readlnString());
            writeString("Nº minimo de materia prima para Restock: ");
            nMinPrimeMaterialsForRestock = Integer.parseInt(readlnString());
            writeString("Nº maximo de bens que a dona levanta de cada vez da oficina: ");
            nMaxProductsCollect = Integer.parseInt(readlnString());
            writeString("Nº maximo de bens na Oficina: ");
            nLimitOfProductsInFactory = Integer.parseInt(readlnString());
        }

        int totalProducts = ((nPrimeMaterialsInFactory + nInitialPrimeMaterialsInStorage) / nPrimeMaterialsByProduct) + nInitialProductsInShop;
        sharedInfo = new MonInfo(
                nCraftsman,
                nCustomers,
                fName,
                nPrimeMaterialsInFactory
        ); // Create general repository
        shop = new MonShop(nInitialProductsInShop, nCustomers, sharedInfo, totalProducts); // Creating shop
        factory = new MonFactory(
                sharedInfo,
                nPrimeMaterialsInFactory,
                nInitialPrimeMaterialsInStorage,
                nPrimeMaterialsByProduct,
                nMinPrimeMaterialsForRestock,
                nMaxProductsCollect,
                nLimitOfProductsInFactory
        ); // Creating Factory
        storage = new MonStorage(nInitialPrimeMaterialsInStorage, nPrimeOwnerCarry); // Creating Storage
        owner = new Owner(sharedInfo, factory, shop, storage); // Create Owner

        for (int i = 0; i < nCraftsman; i++)
            craftman[i] = new Craftman(i, factory, shop, sharedInfo); // Create Craftsmans
        for (int i = 0; i < nCustomers; i++)
            customer[i] = new Customer(sharedInfo, i, shop); // Create Customers
        
        /* Arranque da simulacao */
        for (int i = 0; i < nCraftsman; i++)
            craftman[i].start();
        for (int i = 0; i < nCustomers; i++)
            customer[i].start();
        
        owner.start();
    }
}
