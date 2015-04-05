package MainProblema01;

import genclass.GenericIO;
import genclass.FileOp;
import MonitorsProblema1.*;

/**
 * Main class.
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
        String fName = "log.txt";       // Logging file name
        boolean success;                // Validation of the input data
        char opt;                       // option
        /* Startup of the log */
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
            } else success = true;
        } while (!success);

        /* Initialize variables */
        int nCraftsman = 3;                                     // Number of Craftmans
        int nCustomers = 3;                                     // Number of Customers
        MonInfo sharedInfo;                                     // General Repository
        MonShop shop;                                           // Shop
        MonFactory factory;                                     // Factory
        MonStorage storage;                                     // Storage
        Owner owner;                                            // Owner
        Craftman[] craftman = new Craftman[nCraftsman];         // Threads array of Craftmans
        Customer[] customer = new Customer[nCustomers];         // Threads array of Customers
        int nInitialPrimeMaterialsInStorage = 20;               // Initial number of prime materials in the Storage
        int nPrimeMaterialsInFactory = 10;                      // Initial number of prime materials in the Factory
        int nInitialProductsInShop = 10;                        // Initial number of products in the Shop
        int nPrimeMaterialsByProduct = 2;                       // Prime materials needed per product
        int nPrimeOwnerCarry = 10;                              // Number of prime materials that the owner can carry
        int nMinPrimeMaterialsForRestock = 10;                  // Minimum number of prime materials for restock
        int nMaxProductsCollect = 5;                            // Maximum number of products that the owner can carry
        
        // Option to select values when the program starts.
        /*writeString("Usar valores predefinidos?(s/n) ");
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
        }*/

        int totalProducts = ((nPrimeMaterialsInFactory + nInitialPrimeMaterialsInStorage) / nPrimeMaterialsByProduct) + nInitialProductsInShop;
        sharedInfo = new MonInfo(
                nCraftsman,
                nCustomers,
                fName,
                nPrimeMaterialsInFactory
        );  // Create general repository
        shop = new MonShop(nInitialProductsInShop, nCustomers, sharedInfo, totalProducts);  // Creating shop
        factory = new MonFactory(
                sharedInfo,
                nPrimeMaterialsInFactory,
                nInitialPrimeMaterialsInStorage,
                nPrimeMaterialsByProduct,
                nMinPrimeMaterialsForRestock,
                nMaxProductsCollect
        );  // Creating Factory
        storage = new MonStorage(nInitialPrimeMaterialsInStorage, nPrimeOwnerCarry);        // Creating Storage
        
        owner = new Owner(sharedInfo, factory, shop, storage);              // Create Owner
        for (int i = 0; i < nCraftsman; i++)
            craftman[i] = new Craftman(i, factory, shop, sharedInfo);       // Create Craftsmans
        for (int i = 0; i < nCustomers; i++)
            customer[i] = new Customer(sharedInfo, i, shop);                // Create Customers
        
        /* Start of the simulation */
        for (int i = 0; i < nCraftsman; i++)
            craftman[i].start();
        for (int i = 0; i < nCustomers; i++)
            customer[i].start();
        
        owner.start();
    }
}
