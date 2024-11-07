import java.util.Random;

public class ArrayGenerator<T> {

    private int size;
    private String structure;
    private String type;
    private static final double shuffleFraction = 0.5;

    /**
     * Constructor to initialize array generator
     * Accept size, structure, and type
     * Validates parameters and sets instance variables
     */
    public ArrayGenerator(int size, String structure, String type) {
        if (size <= 0) throw new IllegalArgumentException("Size must be positive.");
        if (!structure.matches("bestCase|worstCase|averageCase"))
            throw new IllegalArgumentException("Unknown structure: " + structure);
        if (!type.matches("int|long|float|double|char|string"))
            throw new IllegalArgumentException("Unknown type: " + type);

        this.size = size;
        this.structure = structure;
        this.type = type;
    }

    /**
     * Generates an array based on provided type and structure
     * Returns the generated array
     */
    public Object generate() {
        Random rand = new Random();

        switch (type) {
            case "int":
                Integer[] intArray = new Integer[size];
                populateArray(intArray, structure, rand, Integer.class);
                return intArray;

            case "long":
                Long[] longArray = new Long[size];
                populateArray(longArray, structure, rand, Long.class);
                return longArray;

            case "float":
                Float[] floatArray = new Float[size];
                populateArray(floatArray, structure, rand, Float.class);
                return floatArray;

            case "double":
                Double[] doubleArray = new Double[size];
                populateArray(doubleArray, structure, rand, Double.class);
                return doubleArray;

            case "char":
                Character[] charArray = new Character[size];
                populateArray(charArray, structure, rand, Character.class);
                return charArray;

            case "string":
                String[] stringArray = new String[size];
                populateArray(stringArray, structure, rand, String.class);
                return stringArray;

            default:
                throw new IllegalArgumentException("Unsupported array type.");
        }
    }

    /**
     * Populates the array with values according to specified structure
     * Accept structure which can be "bestCase", "worstCase", or "averageCase", rand object, and type class which is used in next methods
     */
    private <T> void populateArray(T[] array, String structure, Random rand, Class<T> typeClass) {
        for (int i = 0; i < array.length; i++) {
            switch (structure) {
                case "bestCase":
                    array[i] = generateSequentialValue(i, typeClass);
                    break;
                case "worstCase":
                    array[i] = generateSequentialValue(array.length - i - 1, typeClass);
                    break;
                case "averageCase":
                    array[i] = generateSequentialValue(i, typeClass);
                    break;
            }
        }
        if ("averageCase".equals(structure)) shuffleArray(array, rand);
    }

    /**
     * Generates a random value of specified type
     * Accept type which can be Integer, Long, Float, Double, Character, and String
     */
    private <T> T generateRandomValue(Random rand, Class<T> typeClass) {
        if (typeClass == Integer.class) return typeClass.cast(rand.nextInt(100));
        if (typeClass == Long.class) return typeClass.cast(rand.nextLong() % 100);
        if (typeClass == Float.class) return typeClass.cast(rand.nextFloat() * 100);
        if (typeClass == Double.class) return typeClass.cast(rand.nextDouble() * 100);
        if (typeClass == Character.class) return typeClass.cast((char) (rand.nextInt(26) + 'a'));
        if (typeClass == String.class) return typeClass.cast(Character.toString((char) (rand.nextInt(26) + 'a')));
        throw new IllegalArgumentException("Unsupported type class.");
    }

    /**
     * Generates a sequential value of specified type, based on index
     */
    private <T> T generateSequentialValue(int value, Class<T> typeClass) {
        if (typeClass == Integer.class) return typeClass.cast(value);
        if (typeClass == Long.class) return typeClass.cast((long) value);
        if (typeClass == Float.class) return typeClass.cast((float) value);
        if (typeClass == Double.class) return typeClass.cast((double) value);
        if (typeClass == Character.class) return typeClass.cast((char) ('a' + value % 26));
        if (typeClass == String.class) return typeClass.cast(Character.toString((char) ('a' + value % 26)));
        throw new IllegalArgumentException("Unsupported type class.");
    }

    /**
     * Shuffles elements in the array
     * Used to simulate an "average case" for sorting algorithms
     */
    private <T> void shuffleArray(T[] array, Random rand) {
        int numShuffles = (int) (array.length * shuffleFraction);
        for (int i = 0; i < numShuffles; i++) {
            int idx1 = rand.nextInt(array.length);
            int idx2 = rand.nextInt(array.length);
            T temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
    }
}
