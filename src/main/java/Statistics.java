public class Statistics {

    private long intCount = 0;
    private long floatCount = 0;
    private long stringCount = 0;

    private long intMin = Long.MAX_VALUE;
    private long intMax = Long.MIN_VALUE;
    private long intSum = 0;

    private double floatMin = Double.MAX_VALUE;
    private double floatMax = -Double.MAX_VALUE;
    private double floatSum = 0;

    private int minStringLength = Integer.MAX_VALUE;
    private int maxStringLength = 0;

    public void addInteger(long value) {
        intCount++;
        if (value < intMin) intMin = value;
        if (value > intMax) intMax = value;
        intSum += value;
    }

    public void addFloat(double value) {
        floatCount++;
        if (value < floatMin) floatMin = value;
        if (value > floatMax) floatMax = value;
        floatSum += value;
    }


    public void addString(String value) {
        stringCount++;
        int length = value.length();
        if (length < minStringLength) minStringLength = length;
        if (length > maxStringLength) maxStringLength = length;
    }

    public void printShortStats() {
        System.out.println("Integers: " + intCount);
        System.out.println("Floats: " + floatCount);
        System.out.println("Strings: " + stringCount);
    }

    public void printFullStats() {
        if (intCount > 0) {
            System.out.printf("Integers: count=%d, min=%d, max=%d, sum=%d, average=%.2f%n",
                    intCount, intMin, intMax, intSum, (double) intSum / intCount);
        } else {
            System.out.println("Integers: count=0");
        }

        if (floatCount > 0) {
            System.out.printf("Floats: count=%d, min=%g, max=%g, sum=%g, average=%g%n",
                    floatCount, floatMin, floatMax, floatSum, floatSum / floatCount);
        } else {
            System.out.println("Floats: count=0");
        }

        if (stringCount > 0) {
            System.out.printf("Strings: count=%d, minLength=%d, maxLength=%d%n",
                    stringCount, minStringLength, maxStringLength);
        } else {
            System.out.println("Strings: count=0");
        }
    }
}

