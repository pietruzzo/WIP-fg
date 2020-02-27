package shared;

import client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.misc.Unsafe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Utils {

    /**
     * Support seconds (s), minutes (m), hours (h)
     */
    public static final String TIME_DELIMETER_REGEX = "([smh]|ms)";

    public static int getPartition(String name, int numPartitions) {
        return Math.abs(name.hashCode() % numPartitions);
    }

    public static void parallelizeAndWait (ThreadPoolExecutor executors, DuplicableRunnable task) throws ExecutionException, InterruptedException {

        Collection<Future> executions = new LinkedList<>();

        //Parallel execution
        for (int i = 0; i < executors.getMaximumPoolSize()-1; i++) {
            executions.add(executors.submit(task.getCopy()));
        }
        executions.add(executors.submit(task));

        //Wait executors end
        for (Future<?> future: executions) {
            future.get();
        }
    }

    /**
     * Convert time from string to millis according to convention
     * es: 23m2h12s -> 8592000
     * @return
     */
    public static long solveTime(String time){
        String[] tokens = time.replace("-", "").split(TIME_DELIMETER_REGEX);
        int tokenIndex = 0;
        long result = 0;

        for (int i = 0; i < time.length(); i++) {
            char c = time.toCharArray()[i];

            if (c == 'm' && i < time.length()-1 && time.toCharArray()[i+1] == 's') {
                result = result + (Long.parseLong(tokens[tokenIndex]));
                tokenIndex = tokenIndex +1;
                i++;
            } else if (c == 's'){
                result = result + (Long.parseLong(tokens[tokenIndex])*1000);
                tokenIndex = tokenIndex +1;
            } else if (c == 'm'){
                result = result + (Long.parseLong(tokens[tokenIndex])*60000);
                tokenIndex = tokenIndex +1;
            } else if (c == 'h'){
                result = result + (Long.parseLong(tokens[tokenIndex])*3600000);
                tokenIndex = tokenIndex +1;
            }
        }

        char last = time.charAt(time.length()-1);
        if ( last >= '0' && last <= '9' ) {
            result = result + Long.parseLong(tokens[tokenIndex]);
        }
        return result;
    }

    public interface DuplicableRunnable extends Runnable {
        DuplicableRunnable getCopy();
    }

    @NotNull
    public static PrintWriter getFileWriter(String outputPath) throws FileNotFoundException {

        File file = new File(outputPath);
        if (file.exists()){
            file.delete();
        }

        return new PrintWriter(outputPath);
    }

    public static int countLines(String filename) throws IOException {
        int noOfLines = 1;
        try (FileChannel channel = FileChannel.open(Paths.get(filename), StandardOpenOption.READ)) {
            ByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            while (byteBuffer.hasRemaining()) {
                byte currentByte = byteBuffer.get();
                if (currentByte == '\n')
                    noOfLines++;
            }
        }
        return noOfLines;
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String getLocalIP() {

        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr.getHostAddress();
                        }
                        else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress.getHostAddress();
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                return null;
            }
            return jdkSuppliedAddress.getHostAddress();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String getJarFolder() {
        try {
            return new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath().getParent().toString() + File.separator;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * look in resources or in folder containing jar
     * @return
     */
    public static String getAkkaConfPath(String file) {

        String protocol = Utils.class.getResource("").getProtocol();
        if(Objects.equals(protocol, "jar")){
            // run in jar
            return Paths.get(getJarFolder(), file).toString();
        } else if(Objects.equals(protocol, "file")) {
            // run in ide
            return Client.class.getResource("/" + file).getPath();
        }
        else throw new RuntimeException("not ide nor jar detected...");
    }

    /**
     *
     * @param ipAddress
     * @param port
     * @param retrials
     * @return true if collnection has been established
     */
    static synchronized public boolean waitConnection(String ipAddress, int port, @Nullable Integer retrials) {

        if (retrials == null) {
            retrials = 50;
        }

        for (int i = 0; i < retrials ; i++) {

            try {
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec("nc -vz "+ ipAddress + " " + port);

                String result = new String(proc.getErrorStream().readAllBytes());
                result = result + new String(proc.getInputStream().readAllBytes());

                if (result.contains("succeeded") || result.contains("Connected")) {
                    return true;
                } else {
                    Utils.class.wait(500);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }


        return false;
    }

    public static void disableWarning() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore
        }
    }

}
