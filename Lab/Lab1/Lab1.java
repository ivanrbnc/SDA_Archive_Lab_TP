import java.io.*;
import java.util.StringTokenizer;

public class Lab1 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer

        // Referensi : Dafi Nafidz Radhiyya (2106701564)
        // Pembuatan storage temporal
        int tempS = 0;
        int tempO = 0;
        int tempF = 0;
        int tempI = 0;
        int tempT = 0;
        int tempA = 0;
        int deleted = 0;
        
        for (char c : x) {
            // Huruf S selalu bisa membuat 'SOFITA', tetapi lainnya perlu mengikuti banyaknya huruf sebelumnya
            if (c == 'S') { 
                tempS += 1;
            } else if ((c == 'O') && (tempO < tempS)) { 
                tempO += 1;
            } else if ((c == 'F') && (tempF < tempO)) {
                tempF += 1;
            } else if ((c == 'I') && (tempI < tempF)) {
                tempI += 1;
            } else if ((c == 'T') && (tempT < tempI)) {
                tempT += 1;
            } else if ((c == 'A') && (tempA < tempT)) {
                tempA += 1;
            } else {
                deleted += 1;
            }
        }

        // Pengosongan temp yang tidak dapat membuat kata 'SOFITA'
        deleted += (tempS - tempA) + (tempO - tempA) + (tempF - tempA) + (tempI - tempA) + (tempT - tempA);

        return deleted;
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        int ans = getTotalDeletedLetters(N, x);
        out.println(ans);

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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

    }
}