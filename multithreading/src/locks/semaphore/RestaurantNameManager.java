package locks.semaphore;

import java.util.*;

public class RestaurantNameManager {
    private static Map<Integer, RestaurantBrand> list;
    private static List<Integer> assignedList;
    private Random random;

    public RestaurantNameManager() {
        random = new Random();
        this.list = new HashMap<>();
        assignedList = new ArrayList<>();
        int start = 0;
        for (RestaurantBrand name : RestaurantBrand.values()) {
            list.put(start++, name);
        }
    }

    public String getName() {
        int index;
        while (true) {
            index = random.nextInt(RestaurantBrand.values().length);
            if(!assignedList.contains(index)) {
                assignedList.add(index);
                return list.get(index).toString();
            }
        }
    }
}
