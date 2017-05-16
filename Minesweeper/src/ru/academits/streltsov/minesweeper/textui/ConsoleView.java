package ru.academits.streltsov.minesweeper.textui;

import ru.academits.streltsov.minesweeper.common.View;
import ru.academits.streltsov.minesweeper.conroller.Controller;
import ru.academits.streltsov.minesweeper.model.*;
import ru.academits.streltsov.minesweeper.model.exceptions.GameOverException;
import ru.academits.streltsov.minesweeper.model.exceptions.VictoryException;

import javax.naming.OperationNotSupportedException;
import java.io.FileNotFoundException;
import java.util.*;

public class ConsoleView implements View {
    private Controller controller;
    private final Scanner scanner = new Scanner(System.in);

    public void startApplication() throws FileNotFoundException {
        while (true) {
            System.out.println("1 - новая игра");
            System.out.println("2 - таблица рекордов");
            System.out.println("3 - об игре;");
            System.out.println("0 - выход");

            while (true) {
                String choice = scanner.next();
                switch (choice) {
                    case "1": {
                        newGame();
                        break;
                    }

                    case "2": {
                        highScores();
                        break;
                    }

                    case "3": {
                        about();
                        break;
                    }

                    case "0": {
                        System.exit(0);
                    }

                    default: {
                        System.out.println("Неверный ввод");
                        continue;
                    }
                }

                break;
            }
        }
    }

    public void addController(Controller controller) {
        this.controller = controller;
    }

    public void printField(Cell[][] cells) {
        for (Cell[] row : cells) {
            for (int j = 0; j < cells[0].length; ++j) {
                if (row[j].isOpened() && row[j].getValue() == 0) {
                    System.out.print("  ");
                } else if (row[j].isOpened()) {
                    System.out.printf("%d ", row[j].getValue());
                } else if (row[j].isMarked()) {
                    System.out.print("P ");
                } else if (row[j].isQuestioned()) {
                    System.out.print("? ");
                } else {
                    System.out.print("X ");
                }

                if (j == cells[0].length - 1) {
                    System.out.println();
                }
            }
        }
    }

    public void printOpenedField(Cell[][] cells) {
        for (Cell[] row : cells) {
            for (int j = 0; j < cells[0].length; ++j) {
                if (row[j].getValue() == -1) {
                    System.out.print("M ");
                } else if (row[j].getValue() == 0) {
                    System.out.print("  ");
                } else {
                    System.out.printf("%d ", row[j].getValue());
                }

                if (j == cells[0].length - 1) {
                    System.out.println();
                }
            }
        }
    }

    private int getEnteredRow() {
        while (true) {
            System.out.print("Введите номер строки: ");
            try {
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Должно быть число!");
            }
        }
    }

    private int getEnteredColumn() {
        while (true) {
            System.out.print("Введите номер столбца: ");
            try {
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Должно быть целое число!");
            }
        }
    }

    private void newGame() throws FileNotFoundException {
        levelSelection(controller.needLevels());
        controller.needPrintField();

        long start;
        long finish;
        while (true) {
            System.out.println("1 - открыть ячейку");
            System.out.println("0 - закончить игру");
            String choice = scanner.next();
            switch (choice) {
                case "1": {
                    controller.needOpenFirstCell(getEnteredRow(), getEnteredColumn());
                    start = System.currentTimeMillis();
                    break;
                }

                case "0": {
                    return;
                }

                default: {
                    System.out.println("Неверный ввод.");
                    continue;
                }
            }
            break;
        }

        while (true) {
            controller.needPrintField();
            System.out.println("1 - открыть ячейку");
            System.out.println("2 - пометить ячейку");
            System.out.println("3 - снять пометку");
            System.out.println("4 - поставить вопрос");
            System.out.println("5 - убрать вопрос");
            System.out.println("6 - открыть все соседние ячейки");
            System.out.println("0 - закончить игру");

            String choice = scanner.next();
            switch (choice) {
                case "1": {
                    try {
                        controller.needOpenCell(getEnteredRow(), getEnteredColumn());
                        break;
                    } catch (OperationNotSupportedException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (GameOverException e) {
                        System.out.println(e.getMessage());
                        controller.needPrintOpenedField();
                        return;
                    }  catch (VictoryException e) {
                        finish = System.currentTimeMillis();
                        long time = finish - start;

                        System.out.println(e.getMessage());
                        victoryCatch(time);
                        return;
                    }
                }

                case "2": {
                    try {
                        controller.needSetMark(getEnteredRow(), getEnteredColumn());
                        break;
                    } catch (OperationNotSupportedException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }

                case "3": {
                    try {
                        controller.needDeleteMark(getEnteredRow(), getEnteredColumn());
                        break;
                    } catch (OperationNotSupportedException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }

                case "4": {
                    try {
                        controller.needSetQuestion(getEnteredRow(), getEnteredColumn());
                        break;
                    } catch (OperationNotSupportedException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }

                case "5": {
                    try {
                        controller.needDeleteQuestion(getEnteredRow(), getEnteredColumn());
                        break;
                    } catch (OperationNotSupportedException |IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }

                case "6": {
                    try {
                        controller.needFastOpen(getEnteredRow(), getEnteredColumn());
                        break;
                    } catch (OperationNotSupportedException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (VictoryException e) {
                        finish = System.currentTimeMillis();
                        long time = finish - start;

                        System.out.println(e.getMessage());
                        victoryCatch(time);
                        return;
                    } catch (GameOverException e) {
                        System.out.println(e.getMessage());
                        controller.needPrintOpenedField();
                        return;
                    }
                }

                case "0": {
                    return;
                }

                default: {
                    System.out.println("Неверный ввод.");
                }
            }
        }
    }

    private void victoryCatch(long time) throws FileNotFoundException {
        System.out.println("Ваше время: " + millisecondsToDate(time));

        ArrayList<Winner> winners = controller.needHighScores();
        if (winners.isEmpty() || winners.get(winners.size() - 1).getTime() >= time) {
            System.out.print("Введите ваше имя: ");
            controller.needAddWinner(scanner.next(), time, winners);
        }
    }

    private void highScores() throws FileNotFoundException {
        ArrayList<Winner> winners = Minesweeper.getWinners();
        int i = 1;
        for (Winner winner : winners) {
            System.out.println(i + ". " + winner.getName() + " " + millisecondsToDate(winner.getTime()));
            ++i;
        }
    }

    private String millisecondsToDate(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    private void about() {
        System.out.println("Об игре:)");
    }

    public void levelSelection(String[] levels) {
        int i = 1;
        for (String level : levels) {
            System.out.println(i + " - " + level);
            ++i;
        }


        while (true) {
            String choice = scanner.next();
            switch (choice) {
                case "1": {
                    controller.needInitField(levels[0]);
                    return;
                }

                case "2": {
                    controller.needInitField(levels[1]);
                    return;
                }

                case "3": {
                    controller.needInitField(levels[2]);
                    return;
                }

                case "4": {
                    while (true) {
                        int rowsNumber;
                        while (true) {
                            try {
                                System.out.println("Введите число строк: ");
                                rowsNumber = Integer.parseInt(scanner.next());
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Число строк должно быть положительным целым числом");
                            }
                        }

                        int columnsNumber;
                        while (true) {
                            try {
                                System.out.println("Введите число столбцов: ");
                                columnsNumber = Integer.parseInt(scanner.next());
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Число столбцов должно быть положительным целым числом");
                            }
                        }

                        int minesNumber;
                        while (true) {
                            try {
                                System.out.println("Введите число мин: ");
                                minesNumber = Integer.parseInt(scanner.next());
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Число мин должно быть положительным целым числом");
                            }
                        }

                        try{
                            controller.needInitField(rowsNumber, columnsNumber, minesNumber);
                            return;
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                default: {
                    System.out.println("Неверный ввод.");
                }
            }
        }
    }
}
