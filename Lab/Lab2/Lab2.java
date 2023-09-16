import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.StringTokenizer;

public class Lab2 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;
    private static Deque<Stack<Integer>> conveyorBelt;

    static int geserKanan() {
        // TODO : Implementasi fitur geser kanan conveyor belt

        // Push back the most far jar into the closest with Sofita
        conveyorBelt.push(conveyorBelt.pollLast());

        // Return condition
        if (conveyorBelt.peekFirst().empty()) {
            return -1;
        } else {
            return conveyorBelt.peekFirst().peek();
        }
    }

    static int beliRasa(int rasa) {
        // TODO : Implementasi fitur beli rasa, manfaatkan fitur geser kanan

        // Distance between Sofita and the jar
        int counter = 0;

        for (Stack<Integer> jar : conveyorBelt) {

            // When it reaches a jar where the flavour at the top of the jar is the same as parameter
            if (jar.peek() == rasa) {
                for (int i = 0; i < conveyorBelt.size() - counter; i++) {
                    geserKanan(); // Move that jar until near Sofita
                }

                // Pop the cookie !
                conveyorBelt.peekFirst().pop();
                return counter;
            }

            counter += 1;
        }

        return -1;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        
        int N = in.nextInt();
        int X = in.nextInt();
        int C = in.nextInt();

        conveyorBelt = new ArrayDeque<Stack<Integer>>();

        for (int i = 0; i < N; ++i) {

            // TODO: Inisiasi toples ke-i
            Stack<Integer> jar = new Stack<Integer>();

            for (int j = 0; j < X; j++) {

                int rasaKeJ = in.nextInt();

                // TODO: Inisiasi kue ke-j ke dalam toples ke-i
                jar.push(rasaKeJ);
            }

            conveyorBelt.push(jar);
        }

        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
        }
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