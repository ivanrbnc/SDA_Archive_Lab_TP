import java.io.*;
import java.util.StringTokenizer;

public class Lab3v2 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        // Run Solusi
        int solution = getMaxRedVotes(0, N - 1);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    // Referensi : Dafi Nafidz Radhiyya
    public static int getMaxRedVotes(int start, int end) {
        // TODO : Implementasikan solusi rekursif untuk mendapatkan skor vote maksimal
        // untuk RED pada subarray A[start ... end] (inklusif)

        // Inisiasi nilai awal
        int firstR = -1;
        int secondR = -1;
        int counterR = 0;

        // Counting R from the most left and right
        for (int i = start; i <= end; i++) {
            if (A[i] == 'R') {
                counterR += 1;
                if (counterR == 1) {
                    firstR = i;
                } else {
                    secondR = i;
                }
            }
        }

        // Base case when R < 1
        if (counterR <= 1) {
            return counterR;
        }

        // Base case when R > B
        if (counterR * 2 > end - start + 1) {
            return end - start + 1;
        }

        // Initiate votes every recursive
        int votes = 0;

        // When the most right R and left R clash together
        while (secondR >= firstR && counterR != 0) {
            if (counterR * 2 > secondR - firstR + 1) {
                votes = secondR - firstR + 1;

                while (firstR > start && counterR * 2 > votes + 1) {
                    votes += 1;
                    firstR -= 1;
                } 

                while (secondR < end && counterR * 2 > votes + 1) {
                    votes += 1;
                    secondR += 1;
                } 

                break;
            } else {
                while (secondR-- >= firstR && A[secondR] != 'R');
                counterR -= 1;
            }
        }

        // Sums up everything
        return votes + getMaxRedVotes(secondR + 1, end);
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}