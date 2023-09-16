import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TP3 {
    private static InputReader in;
    private static PrintWriter out;
    static int postAmount;
    static int[][] tunnelList;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Start
        postAmount = in.nextInt();
        int tunnelAmount = in.nextInt();

        // [from][to]
        int[][] graphLength = new int[postAmount][postAmount];

        tunnelList = new int[tunnelAmount][3];

        for (int i = 0; i < tunnelAmount; i++) {
            int pairOne = in.nextInt();
            int pairTwo = in.nextInt();
            int edgeLength = in.nextInt();
            int edgeSize = in.nextInt();
            
            // Graph starts from 0
            graphLength[pairOne - 1][pairTwo - 1] = edgeLength;
            graphLength[pairTwo - 1][pairOne - 1] = edgeLength;

            tunnelList[i][0] = pairOne - 1;
            tunnelList[i][1] = pairTwo - 1;
            tunnelList[i][2] = edgeSize;
        }

        // Initiate kruskal
        int[][] kruskalMST = KruskalMST();
        
        int totalDwarfAmount = in.nextInt();

        int[] dwarfPost = new int[totalDwarfAmount];

        for (int i = 0; i < totalDwarfAmount; i++) {
            int postDwarfIncluded = in.nextInt();
            dwarfPost[i] = postDwarfIncluded - 1;
        }

        // Simulate shortest path for each dwarf post to save more time
        // [from][to]
        int[][] dwarfShortestPath = new int[postAmount][postAmount];

        for (int dwarf : dwarfPost) {
            dwarfShortestPath[dwarf] = algoDijkstra(graphLength, dwarf);
        }

        int queryAmount = in.nextInt();

        for (int i = 0; i < queryAmount; i++) {
            String command = in.next();

            if (command.equals("KABUR")) {
                int postOne = in.nextInt(); // From
                int postTwo = in.nextInt(); // To

                out.println(widestPathProblem(kruskalMST, postTwo - 1, postOne - 1));

            } else if (command.equals("SIMULASI")) {
                int exitAmount = in.nextInt();

                int[] exitContainer = new int[exitAmount];

                // Save exit assumption
                for (int j = 0; j < exitAmount; j++) {
                    int exitAssumption = in.nextInt();
                    exitContainer[j] = exitAssumption - 1;
                }

                // Maximum time for all dwarf to runaway
                int maxTimeExitAllDwarf = 0;
                for (int dwarf : dwarfPost) {

                    // 2.000 Edges, 1.000.000 Max edges. MAX : 2.000.000.000
                    int minTimeExitEachDwarf = 2000000000;
                    
                    // Getting minimum time for dwarf to each exit assumption
                    for (int j = 0; j < exitContainer.length; j++) {
                        int tempTime = dwarfShortestPath[dwarf][exitContainer[j]];

                        if (tempTime < minTimeExitEachDwarf) {
                            minTimeExitEachDwarf = tempTime;
                        }
                    }
                    
                    if (minTimeExitEachDwarf > maxTimeExitAllDwarf) {
                        maxTimeExitAllDwarf = minTimeExitEachDwarf;
                    }
                }

                out.println(maxTimeExitAllDwarf);

            } else if (command.equals("SUPER")) { // postOne - postTwo - postThree
                int postOne = in.nextInt();
                int postTwo = in.nextInt();
                int postThree = in.nextInt();

                out.println("SUPER QUERY");
            }
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

    // https://www.softwaretestinghelp.com/dijkstras-algorithm-in-java/
    // find a vertex with minimum distance
    static int minDistance(int pathArray[], Boolean flag[])   { 
        // Initialize min value 
        int min = Integer.MAX_VALUE, min_index = -1; 
        for (int v = 0; v < postAmount; v++) 
            if (flag[v] == false && pathArray[v] <= min) { 
                min = pathArray[v]; 
                min_index = v; 
            } 
   
        return min_index; 
    }
    
    // https://www.softwaretestinghelp.com/dijkstras-algorithm-in-java/
    // Implementation of Dijkstra's algorithm for graph (adjacency matrix) 
    static int[] algoDijkstra(int graph[][], int source)  { 
        int pathArray[] = new int[postAmount]; // The output array. dist[i] will hold 

        // the shortest distance from src to i 
        // flag for shortest path set contains vertices that have shortest path 
        Boolean[] flag = new Boolean[postAmount]; 
   
        // Initially all the distances are (below) and flag[] is set to false 
        // 2.000 Edges, 1.000.000 Max edges. MAX : 2.000.000.000
        for (int i = 0; i < postAmount; i++) { 
            pathArray[i] = 2000000000; 
            flag[i] = false; 
        } 
   
        // Path between vertex and itself is always 0 
        pathArray[source] = 0;

        // now find shortest path for all vertices  
        for (int count = 0; count < postAmount - 1; count++) { 
            // call minDistance method to find the vertex with min distance
            int u = minDistance(pathArray, flag);

            // the current vertex u is processed
            flag[u] = true; 

            // process adjacent nodes of the current vertex
            for (int v = 0; v < postAmount; v++) {
                // if vertex v not in flag then update it  
                if (!flag[v] && graph[u][v] != 0 && pathArray[u] + graph[u][v] < pathArray[v]) {
                        pathArray[v] = pathArray[u] + graph[u][v]; 
                    }
            }            
        }

        return pathArray;
    }

    // https://www.geeksforgeeks.org/widest-path-problem-practical-application-of-dijkstras-algorithm/
    static int widestPathProblem (int[][] Graph, int src, int target) {
        int[] widest = new int[postAmount];

        MinHeap<Integer> container = new MinHeap<Integer>();
        container.insert(src);

        // Max each edge is 1.000.000
        widest[src] = 1000000;

        while (!container.data.isEmpty()) {
            int currentSrc = container.remove();
            
            // Finding the widest distance to the vertex using current_source vertex's widest distance
            // and its widest distance so far
            for (int i = 0; i < Graph[currentSrc].length; i++) {
                int[] post = Graph[currentSrc];

                int distance = Math.max(widest[i], Math.min(widest[currentSrc], post[i]));

                if (distance > widest[i]) {
                    // Updating bottle-neck distance
                    widest[i] = distance;

                    // Adding the relaxed edge in the priority queue
                    container.insert(i);
                }
            }
        }

        return widest[target];
    }

    // https://www.geeksforgeeks.org/merge-sort/
    // Merges two subarrays of arr[]. First subarray is arr[l..m]. Second subarray is arr[m+1..r]
    static void merge(int arr[][], int l, int m, int r) {

        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        // Create temp arrays
        int L[][] = new int[n1][3];
        int R[][] = new int[n2][3];
 
        // Copy data to temp arrays
        for (int i = 0; i < n1; ++i) {
            L[i] = arr[l + i];
        }
            
        for (int j = 0; j < n2; ++j) {
            R[j] = arr[m + 1 + j];
        }
            
 
        // Merge the temp arrays
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i][2] > R[j][2]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }

            k++;
        }
 
        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
 
        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
 
    // https://www.geeksforgeeks.org/merge-sort/
    // Main function that sorts arr[l..r] using merge()
    static void sort(int arr[][], int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;
 
            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);
 
            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    // https://www.geeksforgeeks.org/kruskals-minimum-spanning-tree-algorithm-greedy-algo-2/
    // A utility function to find set of an element i (uses path compression technique)
    static int find(subset subsets[], int i) {
        // find root and make root as parent of i
        // (path compression)
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }

        return subsets[i].parent;
    }
 
    // https://www.geeksforgeeks.org/kruskals-minimum-spanning-tree-algorithm-greedy-algo-2/
    // A function that does union of two sets of x and y (uses union by rank)
    static void Union(subset subsets[], int x, int y) {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);
 
        // Attach smaller rank tree under root of high rank tree (Union by Rank)
        if (subsets[xroot].rank > subsets[yroot].rank) {
            subsets[xroot].parent = yroot;
        } else if (subsets[xroot].rank < subsets[yroot].rank) {
            subsets[yroot].parent = xroot;
        } else { // If ranks are same, then make one as root and increment its rank by one
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }
 
    // https://www.geeksforgeeks.org/kruskals-minimum-spanning-tree-algorithm-greedy-algo-2/
    // The main function to construct MST using Kruskal's algorithm
    static int[][] KruskalMST() {
        // This will store the resultant MST 
        int[][] result = new int[postAmount][postAmount];
 
        // An index variable, used for result[]
        int e = 0;
        int i = 0;
 
        // Step 1:  Sort all the edges in non-decreasing order of their weight.  
        // If we are not allowed to change the given graph, we can create a copy of array of edges
        sort(tunnelList, 0, tunnelList.length - 1);
 
        // Allocate memory for creating V subsets
        subset subsets[] = new subset[postAmount];
        for (i = 0; i < postAmount; ++i) {
            subsets[i] = new subset();
        }
            
        // Create V subsets with single elements
        for (int v = 0; v < postAmount; ++v) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }
 
        i = 0; // Index used to pick next edge
 
        // Number of edges to be taken is equal to V-1
        while (e < postAmount - 1) {
            // Step 2: Pick the smallest edge. And increment the index for next iteration
            int[] next_edge = tunnelList[i++];
 
            int x = find(subsets, next_edge[0]);
            int y = find(subsets, next_edge[1]);
 
            // If including this edge doesn't cause cycle, include it in result and increment the index
            // of result for next edge
            if (x != y) {
                result[next_edge[0]][next_edge[1]] = next_edge[2];
                result[next_edge[1]][next_edge[0]] = next_edge[2];
                Union(subsets, x, y);
                e++;
            }

            // Else discard the next_edge
        }

        return result;
    }
}

// https://www.geeksforgeeks.org/kruskals-minimum-spanning-tree-algorithm-greedy-algo-2/
// A class to represent a subset for union-find
class subset {
    int parent, rank;
}

// Resource kelas D : https://drive.google.com/drive/folders/1jsMPzfmydPEY-wEAXjhhCTTVSxJDDDmU
class MinHeap<T extends Comparable<T>> {
	ArrayList<T> data;

	public MinHeap() {
		data = new ArrayList<T>();
	}

	public MinHeap(ArrayList<T> arr) {
		data = arr;
		heapify();
	}

	public T peek() {
		if (data.isEmpty())
			return null;
		return data.get(0);
	}

	public void insert(T value) {
		data.add(value);
		percolateUp(data.size() - 1);
	}

	public T remove() {
		T removedObject = peek();

		if (data.size() == 1)
			data.clear();
		else {
			data.set(0, data.get(data.size() - 1));
			data.remove(data.size() - 1);
			percolateDown(0);
		}

		return removedObject;
	}

	private void percolateDown(int idx) {
		T node = data.get(idx);
		int heapSize = data.size();

		while (true) {
			int leftIdx = getLeftChildIdx(idx);
			if (leftIdx >= heapSize) {
				data.set(idx, node);
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightChildIdx(idx);
				if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) < 0)
					minChildIdx = rightIdx;

				if (node.compareTo(data.get(minChildIdx)) > 0) {
					data.set(idx, data.get(minChildIdx));
					idx = minChildIdx;
				} else {
					data.set(idx, node);
					break;
				}
			}
		}
	}

	private void percolateUp(int idx) {
		T node = data.get(idx);
		int parentIdx = getParentIdx(idx);
		while (idx > 0 && node.compareTo(data.get(parentIdx)) < 0) {
			data.set(idx, data.get(parentIdx));
			idx = parentIdx;
			parentIdx = getParentIdx(idx);
		}

		data.set(idx, node);
	}

	private int getParentIdx(int i) {
		return (i - 1) / 2;
	}

	private int getLeftChildIdx(int i) {
		return 2 * i + 1;
	}

	private int getRightChildIdx(int i) {
		return 2 * i + 2;
	}

	private void heapify() {
		for (int i = data.size() / 2 - 1; i >= 0; i--)
			percolateDown(i);
	}

	public void sort() {
		int n = data.size();
		while (n > 1) {
			data.set(n - 1, remove(n));
			n--;
		}
	}

	public T remove(int n) {
		T removedObject = peek();

		if (n > 1) {
			data.set(0, data.get(n - 1));
			percolateDown(0, n - 1);
		}

		return removedObject;
	}

	private void percolateDown(int idx, int n) {
		T node = data.get(idx);
		int heapSize = n;

		while (true) {
			int leftIdx = getLeftChildIdx(idx);
			if (leftIdx >= heapSize) {
				data.set(idx, node);
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightChildIdx(idx);
				if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) < 0)
					minChildIdx = rightIdx;

				if (node.compareTo(data.get(minChildIdx)) > 0) {
					data.set(idx, data.get(minChildIdx));
					idx = minChildIdx;
				} else {
					data.set(idx, node);
					break;
				}
			}
		}
	}

}