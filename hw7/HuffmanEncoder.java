import javax.sound.midi.Sequence;
import java.util.*;

public class HuffmanEncoder {
    /**
     * Count all characters and return the frequency map.
     *
     * @param inputSymbols: all characters.
     * @return the frequency map.
     */
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> counter = new HashMap<>();
        for (char symbol : inputSymbols) {
            counter.put(symbol, counter.getOrDefault(symbol, 0) + 1);
        }

        return counter;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please input the file name.");
        }

        // read the target file
        char[] symbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> counter = buildFrequencyTable(symbols);
        BinaryTrie trie = new BinaryTrie(counter);

        // write decoding trie to the file
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(trie);
        ow.writeObject(symbols.length);

        // start encoding
        Map<Character, BitSequence> encoding = trie.buildLookupTable();
        List<BitSequence> sequenceList = new ArrayList<>();
        for (char symbol : symbols) {
            sequenceList.add(encoding.get(symbol));
        }

        // assemble all bit sequences into one bit sequence.
        ow.writeObject(BitSequence.assemble(sequenceList));
    }
}
