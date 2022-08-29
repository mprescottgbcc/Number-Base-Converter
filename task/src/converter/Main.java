package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    private static String convertFractionToDecimal(String s, int base) {
        double fraction = 0.0;

        char c;
        double factor;
        int digit;

        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            digit = c - (c > 57 ? 55 : 48);
            factor = 1.0 / Math.pow(base, (i + 1));
            fraction += digit * factor;
        }

        return new BigDecimal(String.valueOf(fraction)).toPlainString();
    }

    private static String convertDecimalFraction(String fraction, int base) {
        StringBuilder sb = new StringBuilder(".");
        String quotientString;
        double current = Double.parseDouble(fraction);
        double temp;
        int quotient;

        do {
            temp = current * base;
            quotient = (int) temp;
            current = temp - quotient;
            quotientString = new BigInteger(String.valueOf(quotient), 10).toString(base);
            sb.append(quotientString);
        } while (current > 0 && sb.length() < 6);

        while (sb.length() < 6) {
            sb.append("0");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        String[] values;
        StringBuilder result;
        int sourceBase;
        int targetBase;

        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            input = scanner.nextLine();

            if (input.equals("/exit")) {
                break;
            }

            try {
                values = input.split(" ");
                sourceBase = Integer.parseInt(values[0]);
                targetBase = Integer.parseInt(values[1]);
            } catch (Exception e) {
                System.out.println("Input requires two integers separated by a space.");
                continue;
            }

            while (true) {
                System.out.println("Enter number in base " +
                                   sourceBase + " to convert to base " + targetBase +
                                   " (To go back type /back) ");

                input = scanner.nextLine();

                if (input.equals("/back")) {
                    break;
                }

                try {
                    result = new StringBuilder();
                    String[] parts = input.toUpperCase().split("\\.");

                    if (parts.length > 2) {
                        throw new Exception("There are too many decimal points in the input.");
                    }

                    result.append(new BigInteger(parts[0], sourceBase).toString(targetBase));

                    if (parts.length == 2) {
                        String decimalFraction;

                        if (sourceBase != 10) {
                            decimalFraction = convertFractionToDecimal(parts[1], sourceBase);
                        } else {
                            decimalFraction = "0." + parts[1];
                        }

                        result.append(convertDecimalFraction(decimalFraction, targetBase));
                    }

                    System.out.println("Conversion result: " + result + "\n");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    System.out.println("Invalid input value. Please try again!");
                }
            }
        }
    }
}
