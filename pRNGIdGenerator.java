import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

public class pRNGIdGenerator {

    /**
     * The count for the total number of IDs to generate while testing.
     */
    private static final int TOTAL_NUMBER_OF_TEST_IDS_TO_GENERATE = 100000;

    /**
     * Character stock for generating ids.  Use any values you deem necessary.
     */
    private static final char[] ENGLISH_CHARACTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'W', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * List of random values.
     */
    private static String[] list = new String[TOTAL_NUMBER_OF_TEST_IDS_TO_GENERATE];

    /**
     * Counter for duplicates found.
     */
    private static int duplicateCount = 0;


    /**
     * Test matching of streams, generate random values and test those.
     * <p>
     * Generate list of random values.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting to generate idsa.");
        long startTime = System.currentTimeMillis();
        int idCounter = 0;
        list[0] = "aaaaa";
        String randomId = "aaaaa";

        System.out.println("Should return true: " + Arrays.stream(list).anyMatch(randomId::equals));

        randomId = "no";

        System.out.println("Should return false: " + Arrays.stream(list).anyMatch(randomId::equals));

        for (int i = 0; i < TOTAL_NUMBER_OF_TEST_IDS_TO_GENERATE; i++) {
            randomId = getRandomAlphanumericString(10);
            idCounter++;

            if (i % 1000 == 0 && i != 0) {
                System.out.println("1000 more ids generated. Total generated: [" + idCounter + "]  Last id generated: ".concat(randomId));
                System.out.println("Total duplicate values generated: " + duplicateCount);
            }

            if (Arrays.stream(list).anyMatch(randomId::equals)) {
                duplicateCount++;
                System.err.println("Duplicate Id Generated: GeneratedValue[" + randomId + "]");
            }

            list[i] = randomId;
        }

        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Generated [" + idCounter + "] ids in [" + totalTime + "] seconds.");
    }

    /**
     * Get a random string of alphanumeric characters of length > 10
     *
     * @param length - The length of the alphanumeric {@link String} to return.
     * @return - A psuedo-random alphanumeric {@link String}
     */
    private static String getRandomAlphanumericString(int length) {
        String value = new String("");

        for (int index = 0; index < length; index++) {
            value = value + getRandomChar(System.currentTimeMillis() + getRandomBits());
        }

        return value;
    }

    /**
     * Get a random character from the supplied list of characters.
     *
     * @return {@link Character}
     */
    private static char getRandomChar(long pRNGSeed) {
        Random pRNG = new Random(pRNGSeed);

        return ENGLISH_CHARACTERS[pRNG.nextInt(ENGLISH_CHARACTERS.length)];
    }

    /**
     * Get random bits using time.
     *
     * @return A {@link Long} pseudorandom value.
     */
    private static long getRandomBits() {
        LocalDateTime time = LocalDateTime.of(1492, 01, 02, 12, 01, 02);
        long seconds = Duration.between(time, LocalDateTime.now()).getSeconds();
        long nanos = Duration.between(time, LocalDateTime.now()).getNano();
        long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        long least12SignificantBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        long version = 1 << 12;
        return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + get64LeastSignificantBits();
    }

    /**
     * Least significant random value generation.
     *
     * @return - {@link Long} pseudorandom number.
     */
    private static long get64LeastSignificantBits() {
        long random63BitLong = new Random().nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong + variant3BitFlag;
    }
}