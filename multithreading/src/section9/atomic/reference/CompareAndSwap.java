package section9.atomic.reference;

import java.util.concurrent.atomic.AtomicReference;

public class CompareAndSwap {
    public static void main(String[] args) {
        String oldName = "Old name";
        String newName = "New name";
        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);
        System.out.println("Compare:: 1st time");
        runTest(oldName, newName, atomicReference);
        atomicReference.set("Unexpected value");
        System.out.println("Set new reference and run test again");
        runTest(oldName, newName, atomicReference);
    }

    private static void runTest(String oldName, String newName, AtomicReference<String> atomicReference) {
        if(atomicReference.compareAndSet(oldName, newName)) {
            System.out.println("New value is:: " + atomicReference.get());
        } else {
            System.out.println("Nothing happened");
        }
    }
}
