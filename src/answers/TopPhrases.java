package answers;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Time: O(n Log k + k)  n(n. of phrases) k(n. of top occurrences)
 * Space: O(d + k) d(distinct phrase) k(n. of top occurrences)
 * The README.txt file contains a description of the algorithm
 */
public class TopPhrases {

    private static final int TOP_OCCURRENCE = 100;
    private static String filename = "/Users/dverdone/wallethub/src/tests/TopPhrases.txt";
    private MessageDigest md5;

    public TopPhrases() throws NoSuchAlgorithmException {
        this.md5 = MessageDigest.getInstance("MD5");
    }

    private List<String> findTopNOccurrence(int nOccurrence, String filePath) throws IOException, NoSuchAlgorithmException {
        TopOccurrence topOccurrence = new TopOccurrence(nOccurrence);

        try (RandomFileIterator rli = new RandomFileIterator(new RandomAccessFile(filePath, "r"))) {

            final long[] rowByColumn = {
                0,//offset at the beginning  line
                0,//offset at the end of the line
                -1 // column number
                 };

            rli.lines().peek(x -> {
                rowByColumn[0] = rowByColumn[1];
                rowByColumn[1] = rli.getCurrentPos();
            }).

            flatMap(line -> {
                rowByColumn[2]=-1;
                return Stream.of(line.split("\\|"));}).peek(x -> ++rowByColumn[2] )
                    .collect(Collectors.toMap(this::calculateMd5, w -> new Counter(rowByColumn[0], (byte) rowByColumn[2]), Counter::combine))
                    .forEach((x, y) -> topOccurrence.add(y));

            return topOccurrence.getData().stream().map(x -> {
                rli.moveToLine(x.getLineNumber());
                String[] v = rli.next().split("\\|");
                return v[x.getColumn()];
            }).collect(Collectors.toList());

        }
    }

    private MapKey calculateMd5(String s) {
        md5.reset();
        md5.update(s.getBytes());
        return new MapKey(md5.digest());
    }

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {

        TopPhrases topPhrases = new TopPhrases();
        List<String> topOccurrence = topPhrases.findTopNOccurrence(TOP_OCCURRENCE, filename);
        System.out.println(topOccurrence);

    }
}

class TopOccurrence {

    private final PriorityQueue<Counter> minHeap;
    private final int maxSize;

    public TopOccurrence(int maxSize) {
        this.maxSize = maxSize;
        this.minHeap = new PriorityQueue<>(Comparator.comparingInt(Counter::getOccurrences));
    }

    public void add(Counter data) {
        if (minHeap.size() < maxSize) {
            minHeap.offer(data);
        } else if (minHeap.peek().getOccurrences() < data.getOccurrences()) {
            minHeap.poll();
            minHeap.offer(data);
        }
    }

    public PriorityQueue<Counter> getData() {
        return minHeap;
    }

    @Override
    public String toString() {
        return "TopOccurrence{" + "minHeap=" + minHeap + ", maxSize=" + maxSize + '}';
    }
}

class Counter implements Comparable<Counter> {

    private long lineNumber; //the offset from the beginning of the file of the beginning of the line
    private byte column;
    private int occurrences;

    public Counter(long lineNumber, byte column) {
        this.lineNumber = lineNumber;
        this.column = column;
        this.occurrences = 1;
    }

    public byte getColumn() {
        return column;
    }

    public Counter combine(Counter c) {
        occurrences += c.getOccurrences();
        return this;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Counter{");
        sb.append("lineNumber=").append(lineNumber);
        sb.append(", column=").append(column);
        sb.append(", occurrences=").append(occurrences);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Counter o) {
        return Integer.compare(occurrences, o.occurrences);
    }
}

class MapKey { //BigInteger class could be used but will consume more memory

    private final byte[] mag;

    public MapKey(byte[] mag) {
        this.mag = mag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapKey mapKey = (MapKey) o;
        return Arrays.equals(mag, mapKey.mag);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mag);
    }
}

class RandomFileIterator implements Iterator<String>, Closeable {
    long currentPos;
    private String nextLine = null;
    private final RandomAccessFile raf;

    public RandomFileIterator(RandomAccessFile raf) throws IOException {
        this.raf = raf;
        currentPos = raf.getFilePointer();
        nextLine = raf.readLine();
    }

    @Override
    public void close() throws IOException {
        raf.close();

    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public String next() {
        try {
            currentPos = raf.getFilePointer();
            String line = nextLine;
            nextLine = raf.readLine();
            return line;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long getCurrentPos() {
        return currentPos;
    }

    public void moveToLine(long linePos) {
        try {
            raf.seek(linePos);
            currentPos = raf.getFilePointer();
            nextLine = raf.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Stream<String> lines() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED | Spliterator.NONNULL), false);
    }
}
