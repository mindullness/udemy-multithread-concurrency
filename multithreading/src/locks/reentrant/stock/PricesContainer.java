package locks.reentrant.stock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PricesContainer {
    // Track the price of 5 cryptocurrencies
    private Lock lockObject = new ReentrantLock(true);
    private double bitcoin;
    private double ether;
    private double liteCoin;
    private double bitcoinCash;
    private double ripple;

    public Lock getLockObject() {
        return lockObject;
    }

    public void setLockObject(Lock lockObject) {
        this.lockObject = lockObject;
    }

    public double getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(double bitcoin) {
        this.bitcoin = bitcoin;
    }

    public double getEther() {
        return ether;
    }

    public void setEther(double ether) {
        this.ether = ether;
    }

    public double getLiteCoin() {
        return liteCoin;
    }

    public void setLiteCoin(double liteCoin) {
        this.liteCoin = liteCoin;
    }

    public double getBitcoinCash() {
        return bitcoinCash;
    }

    public void setBitcoinCash(double bitcoinCash) {
        this.bitcoinCash = bitcoinCash;
    }

    public double getRipple() {
        return ripple;
    }

    public void setRipple(double ripple) {
        this.ripple = ripple;
    }
}
