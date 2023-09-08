package locks.semaphore;

public enum RestaurantBrand {
    ROGUE_SAIGON("Rogue Saigon"),
    PASTEUR_STREET("Pasteur Street Brewing"),
    THE_BEM("The Bem"),
    REHAB_STATION("Rehab Station"),
    MALT_BAR("Malt Bar"),
    EAST_WEST("East West Brewing"),
    BIA_CRAFT("Bia Craft"),
    HEART_OF_DARKNESS("Heart Of Darkness");

    private final String name;
    RestaurantBrand(String s) {
        this.name = s;
    }
    public String toString() {
        return name;
    }
}
