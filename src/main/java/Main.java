import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String outputDir = ".";
        String prefix = "";
        boolean appendMode = false;
        boolean shortStats = false;
        boolean fullStats = false;
        List<String> inputFiles = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i+1 < args.length && !args[i + 1].startsWith("-")) {
                        outputDir = args[++i];
                    } else {
                        System.err.println("Вы использовали опцию -o, но не указали путь для результатов. Будет использована директория по умолчанию (текущая)");
                                           }
                    break;
                case "-p":
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        prefix = args[++i];
                    } else {
                        System.err.println("Вы использовали опцию -p, но не указали префикс имен выходных файлов. Будет использован префикс имен файлов по умолчанию (пустой)");
                    }
                    break;
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    shortStats = true;
                    break;
                case "-f":
                    fullStats = true;
                    break;
                default:
                    if (args[i].startsWith("-")) {
                        System.err.println("Ошибка. Вы указали несуществующую опцию " + args[i]);
                        System.exit(1);
                    }
                    inputFiles.add(args[i]);
            }
        }

        if (inputFiles.isEmpty()) {
            System.err.println("Ошибка! Укажите файлы для обработки.");
            System.exit(1);
        }

        Path outputPath = Paths.get(outputDir);

        try {
            Files.createDirectories(outputPath);
        } catch (IOException e) {
            System.err.println("При создании директории для исходящих файлов возникла ошибка: " + e.getMessage());
            System.exit(1);
        } catch (SecurityException e) {
            System.err.println("Отсутствуют права на создание директории для исходящих файлов");
            System.exit(1);
        }


        if (!Files.isDirectory(outputPath)) {
            System.err.println("Ошибка. Указанный путь " + outputDir + " не директория");
            System.exit(1);
        }


        Statistics statistics = new Statistics();

        String intFile = new File(outputDir, prefix + "integers.txt").getPath();
        String floatFile = new File(outputDir, prefix + "floats.txt").getPath();
        String stringFile = new File(outputDir, prefix + "strings.txt").getPath();

        Writer intWriter = null;
        Writer floatWriter = null;
        Writer stringWriter = null;

        try {
            for (String inputFile : inputFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String withoutSpace = line.trim();
                        try {
                            long intValue = Long.parseLong(withoutSpace);
                            statistics.addInteger(intValue);
                            if (intWriter == null) {
                                intWriter = createWriter(intFile, appendMode);
                            }
                            writeLine(intWriter, line);
                        } catch (NumberFormatException e1) {

                            try {
                                double floatValue = Double.parseDouble(withoutSpace);
                                statistics.addFloat(floatValue);
                                if (floatWriter == null) {
                                    floatWriter = createWriter(floatFile, appendMode);
                                }
                                writeLine(floatWriter, line);
                            } catch (NumberFormatException e2) {
                                statistics.addString(line);
                                if (stringWriter == null) {
                                    stringWriter = createWriter(stringFile, appendMode);
                                }
                                writeLine(stringWriter, line);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Возникла ошибка при обработке файла " + inputFile + " - " + e.getMessage());
                }
            }
        } finally {
            closeWriter(intWriter);
            closeWriter(floatWriter);
            closeWriter(stringWriter);
        }

        if (fullStats) {
            statistics.printFullStats();
        } else if (shortStats) {
            statistics.printShortStats();
        }
    }

     private static Writer createWriter(String path, boolean append) throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
          return new BufferedWriter(new FileWriter(file, append));
    }

    private static void writeLine(Writer writer, String line) throws IOException {
        if (writer != null) {
            writer.write(line);
            writer.write(System.lineSeparator());
        }
    }

     private static void closeWriter(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии потока записи в файл - " + e.getMessage());
            }
        }
    }
}


