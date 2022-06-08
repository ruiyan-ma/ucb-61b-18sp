import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Random;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();

        StringBuilder logs = new StringBuilder("\n");
        Random random = new Random();

        for (int i = 0; i < 1000; ++i) {
            if (random.nextBoolean()) {
                // addFirst() or addLast()
                int addNum = random.nextInt(100000);
                if (random.nextBoolean()) {
                    logs.append("addFirst(").append(addNum).append(")\n");
                    student.addFirst(addNum);
                    solution.addFirst(addNum);
                } else {
                    logs.append("addLast(").append(addNum).append(")\n");
                    student.addLast(addNum);
                    solution.addLast(addNum);
                }
            } else if (!solution.isEmpty() && !student.isEmpty()) {
                // removeFirst() or removeLast()
                if (random.nextBoolean()) {
                    logs.append("removeFirst()\n");
                    assertEquals(logs.toString(), solution.removeFirst(), student.removeFirst());
                } else {
                    logs.append("removeLast()\n");
                    assertEquals(logs.toString(), solution.removeLast(), student.removeLast());
                }
            }
        }
    }
}
