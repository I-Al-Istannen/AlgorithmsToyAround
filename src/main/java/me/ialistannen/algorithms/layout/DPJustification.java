package me.ialistannen.algorithms.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class DPJustification {

  public static void main(String[] args) {
    System.out.println(
        String.join("\n", justify("Morgenstund hat Gold im Mund", 11))
    );
    System.out.println();
    System.out.println(
        String.join("\n",
            justify(
                "Returns the cost of a given line, if it consists of the words from start to end (inclusive)",
                20
            )
        )
    );
  }

  /**
   * Justifies some text. The input is split at "\\s*" and then rejoined with a single space.
   *
   * @param input the input text
   * @param lineWidth the width of a line
   * @return the resulting text, line by line
   */
  public static List<String> justify(String input, int lineWidth) {
    String[] words = input.split("\\s+");
    System.out.println(Arrays.toString(words));

    int wordCount = words.length;
    int[] minCostToEnd = new int[wordCount];
    int[] splitAfterIndex = new int[wordCount];

    // bestCostsToEnd  =     min( w(i, j) + bestCostsToEnd[j]) i <= j <= wordCount
    // splitAfterIndex = arg min( w(i, j) + bestCostsToEnd[j]) i <= j <= wordCount
    for (int i = wordCount - 1; i >= 0; i--) {
      int minIndex = wordCount - 1;
      int min = w(i, wordCount - 1, words, lineWidth);

      for (int j = wordCount - 2; j >= i; j--) {
        long sum = (long) w(i, j, words, lineWidth) + minCostToEnd[j + 1];

        if (sum < min) {
          min = (int) sum;
          minIndex = j;
        }
      }

      minCostToEnd[i] = min;
      splitAfterIndex[i] = minIndex;
    }

    List<String> result = new ArrayList<>();

    int current = 0;
    while (current < wordCount) {
      int endIndex = splitAfterIndex[current] + 1;

      StringJoiner line = new StringJoiner(" ");
      for (int i = current; i < endIndex; i++) {
        line.add(words[i]);
      }
      result.add(line.toString());
      current = endIndex;
    }

    return result;
  }


  /**
   * Returns the cost of a given line, if it consists of the words from start to end (inclusive).
   *
   * @param start the start word index
   * @param end the end word index
   * @param words the words
   * @param lineWidth the width of a line
   * @return the badness. {@link Integer#MAX_VALUE} for forbidden (does not fit)
   */
  private static int w(int start, int end, String[] words, int lineWidth) {
    if (start > end) {
      throw new IllegalArgumentException("AAAH");
    }
    // 2 words: 1 space = (j - (j + 1))
    // Start = end ==> one word -> zero spaces
    // line width minus inter word spacing
    int badness = lineWidth - (end - start);

    for (int i = start; i <= end; i++) {
      badness -= words[i].length();
    }

    if (badness >= 0) {
      return badness * badness;
    }
    // We are screwed, it does not fit
    return Integer.MAX_VALUE;
  }
}
