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
	 * Owner identity
	 *
	 * @serialField ownerId
	 */
	private int ownerId;

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
	public Owner(int ownerId, MonFactory factory, MonShop shop) {
		this.ownerId = ownerId;
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
			prepareToWork();
			out = false;
			while (!out) {
				sit = appraiseSit();
				switch (sit) {
					case 1:
						cid = addressACustomer();
						serviceCustomer();
						sayGoodbyeToCustomer(cid);
						break;
					case 2:
					case 3:
						closeTheDoor();
						out = !customersInTheShop();
						break;
					case 4:
						out = true;
						break;
				}
			}
			prepareToLeave();
			if (sit == 3) {
				goToWorkshop();
			} else if (sit == 2) {
				int q = visitSuppliers();
				replenishStock(q);
			}
			returnToShop();
		} while (true); // falta condicao de paragem no while!
	}

	/*
	
			ALGUMAS DESTAS TERAO DE IR PARA OS MONITORES
	
	*/
	private void prepareToWork() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private int appraiseSit() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private int addressACustomer() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private void serviceCustomer() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private void sayGoodbyeToCustomer(int cid) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private void closeTheDoor() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private boolean customersInTheShop() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private void prepareToLeave() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private void goToWorkshop() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private int visitSuppliers() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private void replenishStock(int q) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	/**
	 * Return to shop
	 */
	public void returnToShop() {
		try {
			sleep((long) (1 + 10 * Math.random()));
		} catch (InterruptedException e) {
		}
	}
}
