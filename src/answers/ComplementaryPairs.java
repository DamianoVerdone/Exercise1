package answers;

import com.sun.tools.javac.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The README.txt file contains a description of the algorithm
 */
public class ComplementaryPairs {

    public ComplementaryPairs() {
    }

  /**
   * Time: O(n)
   * Space: O(n)
   * @param input
   * @param k
   * @return
   */
    public List<Pair<Integer, Integer>> kComplementary(final Integer[] input, int k) {
        Map<Integer, List<Integer>> occurrence = IntStream.range(0, input.length).mapToObj(x -> x)
                .collect(Collectors.toMap(x -> input[x], y -> new ArrayList<>(Collections.singletonList(y)), (z, u) -> {
                    z.addAll(u);
                    return z;
                }));

        List<Pair<Integer, Integer>> result = new ArrayList<>();

        for (int i = 0; i != input.length; i++) {
            List<Integer> got = occurrence.get(k - input[i]);
            if (got != null) {
                for (Integer j : got) {
                    if (j > i) {
                        result.add(new Pair<>(input[i], input[j]));
                    }
                }
            }
        }
        return result;

    }

    public static void main(String[] args) {
        ComplementaryPairs complementaryPairs = new ComplementaryPairs();

        System.out.println(complementaryPairs.kComplementary(new Integer[] { 1, 4, 7, 5, 0, 1, 4, 7, 5, 0, 8, 10 }, 5));
        System.out.println(complementaryPairs.kComplementary(new Integer[] { -5, 1, 4, 7, 5, 0, 1, 4, -5, 7, 5, 0, 8, 10 }, -1));
        System.out.println(complementaryPairs.kComplementary(new Integer[] { 5, 0, 0 }, 5));
        System.out.println(complementaryPairs.kComplementary(new Integer[] { 5, 0, 0 }, 2));

    }

}
