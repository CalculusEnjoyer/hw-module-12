import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class FuzzBuzz {
    private final List<Integer> initialQueue = Collections.synchronizedList(new ArrayList<>());
    private final List<String> resultQueue = Collections.synchronizedList(new ArrayList<>());

    private int checkedA = 0;
    private int checkedB = 0;
    private int checkedC = 0;
    private int printed = 0;

    private static boolean fuzz(int input) {
        return input % 3 == 0 && input % 5 != 0;
    }

    private static boolean buzz(int input) {
        return input % 5 == 0 && input % 3 != 0;
    }

    private static boolean fizzbuzz(int input) {
        return input % 15 == 0;
    }


    private Optional<String> number() {
        if (printed < Math.min(Math.min(checkedA, checkedB), checkedC)) {
            return Optional.of(resultQueue.get(printed));
        } else if (printed < initialQueue.size()) {
            return Optional.empty();
        } else {
            Thread.currentThread().interrupt();
        }
        return Optional.empty();
    }

    private boolean isInResult(int index) {
        return index < resultQueue.size();
    }

    public void soutNumbers(int n) {
        initialQueue.clear();
        resultQueue.clear();
        checkedA = 0;
        checkedB = 0;
        checkedC = 0;
        printed = 0;
        IntStream.range(1, n + 1).forEach(initialQueue::add);

        Thread A = new Thread(() -> {
            while (checkedA < n) {
                if (fuzz(initialQueue.get(checkedA))) {
                    if (isInResult(checkedA)) resultQueue.set(checkedA, "fuzz");
                    else {
                        resultQueue.add("fuzz");
                    }

                }
                if (!isInResult(checkedA)) resultQueue.add(Integer.toString(initialQueue.get(checkedA)));
                checkedA++;
            }
        });

        Thread B = new Thread(() -> {
            while (checkedB < n) {
                if (buzz(initialQueue.get(checkedB))) {
                    if (isInResult(checkedB)) resultQueue.set(checkedB, "buzz");
                    else {
                        resultQueue.add("buzz");
                    }
                }
                if (!isInResult(checkedB)) resultQueue.add(Integer.toString(initialQueue.get(checkedB)));
                checkedB++;
            }
        });

        Thread C = new Thread(() -> {
            while (checkedC < n) {
                if (fizzbuzz(initialQueue.get(checkedC))) {
                    if (isInResult(checkedC)) resultQueue.set(checkedC, "fuzzbuzz");
                    else {
                        resultQueue.add("fuzzbuzz");
                    }
                }
                if (!isInResult(checkedC)) resultQueue.add(Integer.toString(initialQueue.get(checkedC)));
                checkedC++;
            }
        });

        Thread D = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Optional<String> temp = number();
                temp.ifPresent(s -> {
                    System.out.println(s + " ");
                    printed++;
                });
            }
        });

        A.start();
        B.start();
        C.start();
        D.start();
    }
}

