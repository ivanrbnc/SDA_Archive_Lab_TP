import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi class ini
class Saham implements Comparable<Saham> {
    public int id;
    public int harga;

    public Saham(int id, int harga) {
        this.id = id;
        this.harga = harga;
    }

    // Sort by serve, then 
    public int compareTo(Saham other) {
        if (this.harga != other.harga) { // Main sort : harga
            return this.harga - other.harga;
        } else { // Alternate sort : id
            return this.id - other.id;
        }
    }
}

public class Lab6 {

    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        ArrayList<Saham> bursa = new ArrayList<Saham>();

        // TODO
        for (int i = 1; i <= N; i++) {
            int harga = in.nextInt();

            bursa.add(new Saham(i, harga));
        }

        int Q = in.nextInt();

        // TODO
        for (int i = 0; i < Q; i++) {
            String q = in.next();

            if (q.equals("TAMBAH")) {
                int harga = in.nextInt();

                bursa.add(new Saham(bursa.size() + 1, harga));

                Collections.sort(bursa);

                if (bursa.size() % 2 == 0) {
                    out.println(bursa.get(bursa.size() / 2).id);
                } else {
                    out.println(bursa.get((int) Math.ceil(bursa.size() / 2)).id);
                }
                
            } else if (q.equals("UBAH")) {
                int nomorSeri = in.nextInt();
                int harga = in.nextInt();

                for (Saham saham : bursa) {
                    if (saham.id == nomorSeri) {
                        saham.harga = harga;
                        break;
                    }
                }

                Collections.sort(bursa);

                if (bursa.size() % 2 == 0) {
                    out.println(bursa.get(bursa.size() / 2).id);
                } else {
                    out.println(bursa.get((int) Math.ceil(bursa.size() / 2)).id);
                }
            }
        }
        out.flush();
    }

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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}