package race.condition.atomic.operations;

public class Test {

        public static void main(String[] args) {
//            int x = 5;
//            int result = 0;
//            result = x++ + x-- - --x;

            int x=5;
            int y=10;
            int result = x + y*2 - x/2;
            System.out.println(result);
        }

}
