package Example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String[] input = new Scanner(System.in).nextLine().split(" ");
    private static final ArrayList<Integer> usedIndexes = new ArrayList<>();
    private static final int needParts = 6;

    public static void main(String[] args) throws IOException {
        try {
            checkParts();

            try {
                String[] data = new String[]{getDate(), getPhone(), getSex()}, nameData = getNameData();
                String family = nameData[1];

                if (!new File(family + ".txt").exists()) {
                    Files.createFile(Path.of(family + ".txt"));
                }

                try (FileWriter writer = new FileWriter(family + ".txt", true)) {
                    for (String s : nameData) {
                        writer.write(s + " ");
                    }
                    for (String s : data) {
                        writer.write(s + " ");
                    }
                    writer.write("\n");
                    writer.flush();
                } catch (IOException e) {
                    System.err.println("Ошибка при записи в файл");
                    e.printStackTrace();
                }
            } catch (DateNotFoundException e) {
                System.err.println("Ошибочный формат ввода даты рождения");
            } catch (PhoneNotFoundException e) {
                System.err.println("Ошибочный формат ввода номера телефона");
            } catch (SexNotFoundException e) {
                System.err.println("Ошибочный формат ввода пола");
            } catch (NameDataNotFoundException e) {
                System.err.println("Ошибочный формат ввода ФИО");
            }
        } catch (PartsException e) {
            if (e.getParts() < needParts) {
                System.err.println("Введено меньше данных, чем требуется");
            } else if (e.getParts() > needParts) {
                System.err.println("Введено больше данных, чем требуется");
            }
        }
    }

    private static void checkParts() throws PartsException {
        if (input.length != needParts) {
            throw new PartsException(input.length);
        }
    }

    private static String getDate() throws DateNotFoundException {
        for (int i = 0; i < input.length; i++) {
            String s = input[i];

            String[] split = s.split("\\.");
            if (split.length == 3) {
                try {
                    for (String part : split) {
                        Integer.parseInt(part);
                    }
                    usedIndexes.add(i);
                    return s;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        throw new DateNotFoundException();
    }

    private static String getPhone() throws PhoneNotFoundException {
        for (int i = 0; i < input.length; i++) {
            String s = input[i];

            try {
                Integer.parseInt(s);
                usedIndexes.add(i);
                return s;
            } catch (NumberFormatException ignored) {
            }
        }
        throw new PhoneNotFoundException();
    }

    private static String getSex() throws SexNotFoundException {
        for (int i = 0; i < input.length; i++) {
            String s = input[i];

            if (s.equals("f") || s.equals("m")) {
                usedIndexes.add(i);
                return s;
            }
        }
        throw new SexNotFoundException();
    }

    private static String[] getNameData() throws NameDataNotFoundException {
        for (int i = 0; i < input.length - 2; i++) {
            if (!usedIndexes.contains(i) && !usedIndexes.contains(i + 1) && !usedIndexes.contains(i + 2)) {
                return new String[]{input[i], input[i + 1], input[i + 2]};
            }
        }
        throw new NameDataNotFoundException();
    }

    static class PartsException extends Exception {
        private final int parts;

        public PartsException(int parts) {
            super();
            this.parts = parts;
        }

        public int getParts() {
            return parts;
        }
    }

    static class SexNotFoundException extends Exception {
    }

    static class DateNotFoundException extends Exception {
    }

    static class PhoneNotFoundException extends Exception {
    }

    static class NameDataNotFoundException extends Exception {
    }
}
