package locks.semaphore;

public enum BeerType {
    BLONDE_ALE("Blonde Ale"),
    BROWN_ALE("Brown Ale"),
    PALE_ALE("Pale Ale"),
    SOUR_ALE("Sour Ale"),
    WHEAT("Wheat"),
    STOUT("Stout"),
    PORTER("Porter"),
    PILSNER("Pilsner");
    private String type;

    BeerType(String s) {
        this.type = s;
    }

    public String getType() {
        return type;
    }
}
