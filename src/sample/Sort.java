package sample;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class Sort {
    // Array of random values that will be sorted
    private int[] array;
    // Canvas that will be used for visualization
    private Canvas c;
    // Used for documenting the process of sorting
    private String output;
    // Canvas size
    private static final int max = 550;
    private static final int width = 650;
    private static final int borders = 75;
    // Render delay
    private static final int sleepTime = 25;
    private static final Random rnd = new Random();


    Sort(int cols, Canvas can) {
        array = new int[cols];
        c = can;
        for (int i = 0; i < cols; i++) {
            array[i] = rnd.nextInt(max) + 1;
        }
    }


    /**
     * <p>
     *   Set all pixels black, generate columns of random height and calculated width.
     *   Then draw the columns, highlight them if their position has changed.
     * <p>
     *
     * @param  changed1 Index of first column that has been swapped
     * @param  changed2 Index of second column that has been swapped
     */
    private void draw(int changed1, int changed2) {
        c.getGraphicsContext2D().setFill(Color.BLACK);
        c.getGraphicsContext2D().fillRect(0, 0, c.getWidth(), c.getHeight());
        int colWidth = (width / array.length) - 1;
        for (int j = borders, i = 0; i < array.length; j += colWidth + 1, i++) {
            if (i == changed1 | i == changed2)
                c.getGraphicsContext2D().setFill(Color.GREEN);
            else
                c.getGraphicsContext2D().setFill(Color.WHITE);
            c.getGraphicsContext2D().fillRect(j, 0, colWidth, array[i]);
        }
    }

    // Implementation of sorting algorithms -> Two columns are swapped, canvas is refreshed, thread waits for x ms.
    // This process repeats until the array is sorted
    void execBubbleSort() throws Exception {
        output = "Sorting using bubble sort\n";
        while (true) {
            boolean change = false;
            for (int j = 0; j < array.length - 1; j++) {
                if (array[j] < array[j + 1]) {
                    int cache = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = cache;
                    change = true;

                    int finalJ = j;
                    Platform.runLater(() -> {
                        draw(finalJ, finalJ + 1);
                    });
                    try {
                        Thread.sleep(sleepTime);
                        output += "Swapped number " + array[j] + " (on position: " + j + ") and number " + array[j + 1] + " (on position: " + (j + 1) + ")\n";
                    } catch (Exception e) {
                        System.out.println("Bubble sort thread/write exception -> " + e.getMessage());
                    }
                }
            }
            if (!change) break;
        }
    }

    void execSelectionSort() throws Exception {
        output = "Sorting using selection sort\n";
        for (int i = 0; i < array.length; i++) {
            int biggest = array[i];
            int cacheIndex = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] > biggest) {
                    biggest = array[j];
                    cacheIndex = j;
                }
            }
            int cacheValue = array[i];
            array[i] = array[cacheIndex];
            array[cacheIndex] = cacheValue;
            // Lambda vars
            int finalI = i;
            int finalCacheIndex = cacheIndex;

            Platform.runLater(() -> {
                draw(finalI, finalCacheIndex);
            });
            try {
                Thread.sleep(sleepTime * 10);
                output += "Swapped number " + array[i] + " (on position: " + i + ") and number " + array[cacheIndex] + " (on position: " + cacheIndex + ")\n";
            } catch (InterruptedException e) {
                System.out.println("Selection sort thread/write exception -> " + e.getMessage());
            }
        }
    }

    void execInsertionSort() throws Exception {
        output = "Sorting using insertion sort\n";
        for (int i = 1; i < array.length; ++i) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] < key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;

            // Lambda vars
            int finalI = i;
            int finalJ = j;
            Platform.runLater(() -> {
                draw(finalI, finalJ);
            });
            try {
                Thread.sleep(sleepTime * 4);
                output += "Swapped number " + array[finalI] + " (on position: " + finalI + ") and number " + array[finalJ + 1] + " (on position: " + (finalJ + 1) + ")\n";
            } catch (Exception e) {
                System.out.println("Insertion sort thread/write exception -> " + e.getMessage() + "\t " + finalI + "\t" + finalJ);
            }
        }
    }

    void outputFile(File myFile) {
        try {
            output += "FINISHED";
            FileWriter fw = new FileWriter(myFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(output);
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("Output error ->" + e.getMessage());
        }
    }

}
