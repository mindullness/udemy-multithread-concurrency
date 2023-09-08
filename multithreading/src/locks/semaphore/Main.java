package locks.semaphore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Semaphore full = new Semaphore(0);
        Semaphore empty = new Semaphore(5);
        DHLWareHouse queue = new DHLWareHouse(5);
        RestaurantNameManager restaurantNameManager = new RestaurantNameManager();
        List<Brewery> breweries = new ArrayList<>();
        List<Restaurant> restaurants = new ArrayList<>();
        final int NUMBER_OF_BREWERIES = 3;
        final int NUMBER_OF_RESTAURANTS = 5;

        for (int i = 0; i < NUMBER_OF_BREWERIES; i++) {
            Brewery brewery = new Brewery(full, empty, queue);
            brewery.setName("Brewery::" + i);
            breweries.add(brewery);
        }
        for (int i = 0; i < NUMBER_OF_RESTAURANTS; i++) {
            Restaurant restaurant = new Restaurant(full, empty, queue);
            restaurant.setName(restaurantNameManager.getName());
            restaurants.add(restaurant);
        }
        breweries.forEach(Thread::start);
        restaurants.forEach(Thread::start);
        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
        }
        for (Brewery b : breweries){
            b.join(10);
            b.interrupt();
        }
        for (Restaurant r : restaurants){
            r.join(10);
            r.interrupt();
        }
        Thread.interrupted();
    }

}
