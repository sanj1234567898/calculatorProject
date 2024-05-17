package org.example;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Calculator {
    private static final Pattern ROMAN_PATTERN = Pattern.compile("^(I|II|III|IV|V|VI|VII|VIII|IX|X)$");

    public static String calc(String input) {
        String[] strSplit = input.split(" ");

        if (input.split(" ").length == 1) {
            throw new RuntimeException("строка не является математической операцией");
        }

        if (strSplit.length != 3) {
            throw new RuntimeException("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }

        String operand1 = strSplit[0];
        String operand2 = strSplit[2];
        String operation = strSplit[1];

        boolean isRoman = ROMAN_PATTERN.matcher(operand1).matches();
        boolean isRoman1 = ROMAN_PATTERN.matcher(operand2).matches();

        if (isRoman != isRoman1) {
            throw new RuntimeException("Используются разные системы счисления");
        }

        int number1, number2;

        if (isRoman) {
            number1 = RomeNumber.fromString(operand1).getValue();
            number2 = RomeNumber.fromString(operand2).getValue();
        } else {
            try {
                number1 = Integer.parseInt(operand1);
                number2 = Integer.parseInt(operand2);
                if (number1 < 1 || number1 > 10 || number2 < 1 || number2 > 10) {
                    throw new IllegalArgumentException("Числа должны быть от 1 до 10 включительно.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Используются одновременно разные системы счисления");
            }
        }


        int res = getResult(operation, number1, number2);

        if (isRoman) {
            if (res < 1) {
                throw new IllegalArgumentException("в римской системе нет отрицательных чисел");
            }
            return String.valueOf(RomeNumber.fromValue(res));
        } else {
            return Integer.toString(res);
        }
    }

    private static int getResult(String operation, int number1, int number2) {
        int res = 0;

        switch (operation) {
            case "+" -> res = number1 + number2;
            case "-" -> res = number1 - number2;
            case "*" -> res = number1 * number2;
            case "/" -> {
                if (number2 == 0) {
                    throw new ArithmeticException("Деление на ноль не допускается.");
                }
                res = number1 / number2;
            }

            default -> {
                throw new RuntimeException("Неизвестная операция");
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение (например, 3 + 2 или V - III):");
        String inputString = scanner.nextLine();

        try {
            String result = calc(inputString);
            System.out.println("Результат выражения: " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}