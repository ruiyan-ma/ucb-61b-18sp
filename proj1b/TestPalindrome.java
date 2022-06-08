import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < "persiflage".length(); i++) {
            actual.append(d.removeFirst());
        }
        assertEquals("persiflage", actual.toString());
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("racecar"));
        assertFalse(palindrome.isPalindrome("cat"));
    }

    @Test
    public void testIsPalindromeWithComparator() {
        CharacterComparator cc = new OffByOne();
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertTrue(palindrome.isPalindrome("clamb", cc));
        assertFalse(palindrome.isPalindrome("fake", cc));
    }
}
