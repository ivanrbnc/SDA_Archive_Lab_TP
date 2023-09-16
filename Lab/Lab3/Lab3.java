import java.io.*;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;

    public static int[][] recursiveMemory;

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

        recursiveMemory = new int[N + 1][N];

        // Menganggap elemen tiap recursiveMemory = -1 agar bisa dibedakan dengan nilai 0
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                recursiveMemory[i][j] = -1;
            }
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

        // Base case ketika rekursi tersebut pernah dipanggil
        if (recursiveMemory[start][end] != -1) {
            return recursiveMemory[start][end];
        }

        // Base case ketika "pembatas" melebihi elemen terakhir
        if (start > end) {
            return 0;
        }

        // Base case ketika "pembatas" menyentuh elemen terakhir
        if (start == end) {
            if (A[start] == 'R') {
                return 1;
            } else {
                return 0;
            }
        }

        // Inisiasi variabel pembantu
        int redCounter = 0;
        int blueCounter = 0;
        int maxRow = 0;
        int maxFinal = 0;

        for (int i = start; i <= end; i++) {

            // Reset nilai variabel pembantu
            maxRow = 0;
            redCounter = 0;
            blueCounter = 0;

            // Perhitungan red dan blue hingga "pembatas"
            for (int j = start; j <= i; j++) {
                if (A[j] == 'R') {
                    redCounter += 1;
                } else {
                    blueCounter += 1;
                }
            }
    
            // Menghitung maxRow ketika R > B
            if (redCounter > blueCounter) {
                maxRow += redCounter + blueCounter;
            }

            // Menyimpan memori dari fungsi ini
            recursiveMemory[i + 1][end] = getMaxRedVotes(i + 1, end);

            // Memasukkan memori tersebut ke maxRow untuk perbandingan
            maxRow += recursiveMemory[i + 1][end];
            
            // Comparison maxRow dengan maxFinal
            if (maxRow > maxFinal) {
                maxFinal = maxRow;
            }
        }

        return maxFinal;
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