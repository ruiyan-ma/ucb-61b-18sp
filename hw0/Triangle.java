class Triangle {
    public static void main (String[] args) {
        drawTriangle(10);
    }

    private static void drawTriangle(int N) {
        for (int line = 1; line <= N; line++) {
            for (int i = 0; i < line; i++) {
                System.out.print('*');
            }
            System.out.println();
        }

    }
}
