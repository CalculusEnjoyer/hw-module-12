public class TimeCounter {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis() / 1000;

        Thread firstThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Пройшло від запуску програми: " + (System.currentTimeMillis() / 1000 - startTime) + " s");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread secondThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Пройшло 5 секунд.");
            }
        });

        firstThread.start();
        secondThread.start();
    }
}
