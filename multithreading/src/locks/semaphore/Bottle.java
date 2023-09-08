package locks.semaphore;

public class Bottle {
    private String name;
    private double price;
    private float ipa;
    private int size = 12;

    @Override
    public String toString() {
        return "🍺 of " +
                "'" + name + "'" +
                ", $" + String.format("%.2f", price) + "USD" +
                ", " + String.format("%.2f", ipa) + "% IPA" +
                ", vol::" + size + " oz";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getIpa() {
        return ipa;
    }

    public void setIpa(float ipa) {
        this.ipa = ipa;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
