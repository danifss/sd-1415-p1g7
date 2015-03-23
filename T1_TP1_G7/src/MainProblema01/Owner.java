package MainProblema01;

import MonitorsProblema1.*;

/**
 *
 * @author Daniel 51908
 * @author Raphael 64044
 * @version 1.0
 */
public class Owner extends Thread {
	
	/**
	 * General Repository
	 * 
	 * @serialField sharedInfo
	 */
	private final MonInfo sharedInfo;

	/**
	 * Factory
	 *
	 * @serialField factory
	 */
	private MonFactory factory;

	/**
	 * Shop
	 *
	 * @serialField shop
	 */
	private MonShop shop;

	/**
	 * Create owner thread
	 *
	 * @param ownerId Owner identity
	 * @param factory Factory
	 * @param shop Shop
	 */
	public Owner(MonInfo sharedInfo, MonFactory factory, MonShop shop) {
		this.sharedInfo = sharedInfo;
		this.factory = factory;
		this.shop = shop;
	}

	/**
	 * Life cycle of the owner
	 */
	@Override
	public void run() {
		boolean out;
		int cid, sit = 0;
		do {
			prepareToWork(); // fica a dormir
			out = false;
			while (!out) {
				sit = appraiseSit();
				switch (sit) {
					case 1: // atender cliente
						cid = this.shop.addressACustomer();
						serviceCustomer();
						sayGoodbyeToCustomer(cid);
						break;
					case 2: // buscar materias primas do armazem para a oficina
					case 3: // buscar produtos terminados a oficina
						closeTheDoor();
						out = !customersInTheShop(); // verifica primeiro se ha  clientes para atender
						break;
					case 4: // nada para fazer
						out = true;
						break;
				}
			}
			prepareToLeave(); // sair da loja?
			if (sit == 3) {
				goToWorkshop(); // vai buscar produtos a oficina e levar para a loja
			} else if (sit == 2) {
				int q = visitSuppliers();
				replenishStock(q);
			}
			returnToShop();
		} while (true); // falta condicao de paragem no while!
	}


	private void prepareToWork() {
		this.sharedInfo.setStateOwner(MonInfo.WAITING_FOR_NEXT_TASK);
		
		try {
			sleep((long) (1 + 20 * Math.random()));
		} catch (InterruptedException e) {}
	}

	private int appraiseSit() {
		// verifica se ha clientes para serem atendidos
		if(!this.shop.isSitCustomerEmpty())
			return 1;
		// verifica se foi notificada por um artesao pedir materias primas
		if(this.sharedInfo.isToSupplyMaterialsToFactory())
			return 2;
		// verifica se foi notificada por um artesao que ha produtos para ir para a loja
		if(this.sharedInfo.isToTranfsProductsToShop())
			return 3;
		return 4; // nada para fazer
	}

	private void serviceCustomer() {
		this.sharedInfo.setStateOwner(MonInfo.ATTENDING_A_CUSTOMER);
		
		try {
			sleep((long) (1 + 10 * Math.random()));
		} catch (InterruptedException e) {}
	}

	private void sayGoodbyeToCustomer(int cid) {
		this.shop.removeSitCustomer(cid);
		prepareToWork();
	}

	private void closeTheDoor() {
		this.sharedInfo.setStateShop(MonInfo.CLOSED);
	}

	private boolean customersInTheShop() {
		return this.shop.isSitCustomerEmpty() == false;
	}

	private void prepareToLeave() { // nao sei se e assim
		this.sharedInfo.setStateShop(MonInfo.STILL_OPEN);
		this.sharedInfo.setStateOwner(MonInfo.CLOSING_THE_SHOP);
	}

	private void goToWorkshop() {
		this.sharedInfo.setStateOwner(MonInfo.COLLECTING_A_BATCH_OF_PRODUCTS);
	}

	private int visitSuppliers() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void replenishStock(int q) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Return to shop
	 */
	public void returnToShop() {
		try {
			sleep((long) (1 + 10 * Math.random()));
		} catch (InterruptedException e) {}
		
		this.sharedInfo.setStateOwner(MonInfo.OPENING_THE_SHOP);
		this.sharedInfo.setStateShop(MonInfo.OPEN);
	}
}
