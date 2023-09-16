import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;
    private static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        N = in.nextInt();
        int M = in.nextInt();

        ArrayList<ArrayList<AdjListVertex>> graph = new ArrayList<>();
        int[] attackedFortress = new int[M];

        for (int i = 1; i <= N; i++) {
            // TODO: Inisialisasi setiap benteng
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            int F = in.nextInt();
            // TODO: Tandai benteng F sebagai benteng diserang
            attackedFortress[i] = F - 1;
        }

        int E = in.nextInt();
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            // TODO: Inisialisasi jalan berarah dari benteng A ke B dengan W musuh
            graph.get(A - 1).add(new AdjListVertex(B - 1, W));
        }

        int Q = in.nextInt();
        while (Q-- > 0) {
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
            int[] distance = dijkstra(graph, S - 1);

            int temp = Integer.MAX_VALUE;

            // Get minimum distance from S to Attacked Fortress
            for (Integer attacked : attackedFortress) {
                if (distance[attacked] < temp) {
                    temp = distance[attacked];
                }
            }

            // Algorithm tester
            int counter = 0;
            for (int test : distance) {
                out.println("Vertex: " + counter + " - Distance from " + (S - 1) + " source: " + test);
                counter++;
            }

            out.println(K > temp ? "YES" : "NO");
        }

        out.close();
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

    }

    // https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/
    static class AdjListVertex {
        int vertex, weight;
 
        AdjListVertex(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    // https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/
    // Function to find the shortest distance of all the vertices from the source vertex S.
    static int[] dijkstra(ArrayList<ArrayList<AdjListVertex>> graph, int source) {

        // Initiate distance with maximum value of Integer
        int[] distance = new int[N];
        for (int i = 0; i < N; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        
        // Distance from vertex to themselves are 0 obviously..
        distance[source] = 0;
 
        // Save it's AdjList (edges)
        PriorityQueue<AdjListVertex> pq = new PriorityQueue<>((v1, v2) -> v1.weight - v2.weight);
        pq.add(new AdjListVertex(source, 0));
 
        // Count every possible way from source to every vertex in graph
        while (pq.size() > 0) {
            AdjListVertex current = pq.poll();
 
            for (AdjListVertex n : graph.get(current.vertex)) {
                if (distance[current.vertex] + n.weight < distance[n.vertex]) {
                    distance[n.vertex] = n.weight + distance[current.vertex];
                    pq.add(new AdjListVertex(n.vertex, distance[n.vertex]));
                }
            }
        }

        return distance;
    }
}