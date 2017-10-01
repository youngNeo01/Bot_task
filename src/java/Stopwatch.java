package java;

public class Stopwatch {
    private static boolean status;
    static long totalTime;

    public static void setStatus(boolean status) {
        Stopwatch.status = status;
    }
    static void Watch(){
        while (status == true){
            try {
                Thread.sleep(1000);
                totalTime++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static long getTotalTime() {
        return totalTime;
    }

    public static void setTotalTime(long totalTime) {
        Stopwatch.totalTime = totalTime;
    }
}
