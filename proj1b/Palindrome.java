/**
 * Palindrome class
 *
 * @author ruiyan
 */

public class Palindrome {

    /**
     * Return a Deque where the characters appear in the
     * same order as in the given String.
     */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            deque.addLast(c);
        }
        return deque;
    }

    /**
     * Return true if the given word is a palindrome.
     */
    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        while (deque.size() > 1) {
            char first = deque.removeFirst();
            char last = deque.removeLast();
            if (first != last) {
                return false;
            }
        }
        return true;
    }

    /**
     * A method overloads isPalindrome.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        while (deque.size() > 1) {
            char first = deque.removeFirst();
            char last = deque.removeLast();
            if (!cc.equalChars(first, last)) {
                return false;
            }
        }
        return true;
    }
}
