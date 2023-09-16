import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TP2 {

    private static InputReader in;
    private static PrintWriter out;
    public static int jumlahMesin;
    public static FunZone funZone;
    public static Budi budi;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        jumlahMesin = in.nextInt();
        funZone = new FunZone();

        for (int i = 1; i <= jumlahMesin; i++) {
            Mesin newMesin = new Mesin(i);
            funZone.insertLast(newMesin);

            int banyakSkorAwal = in.nextInt();
            for (int j = 0; j < banyakSkorAwal; j++) {
                int score = in.nextInt();
                newMesin.addNewScore(score);
            }
        }

        budi = new Budi(funZone);

        int Q = in.nextInt();

        for (int i = 0; i < Q; i++) {

            String command = in.next();

            if (command.equals("MAIN")) {
                _main();
            } else if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HAPUS")) {
                hapus();
            } else if (command.equals("LIHAT")) {
                lihat();
            } else if (command.equals("EVALUASI")) {
                evaluasi();
            }
        }

        out.close();
    }

    static void _main() {
        int newScore = in.nextInt();

        Mesin currMesin = budi.current;
        currMesin.scoreList.currentCount = 0;
        currMesin.addNewScore(newScore);

        out.println(currMesin.scoreList.currentCount + 1);
    }

    static void gerak() {
        String direction = in.next();
        if (direction.equals("KIRI")) {
            budi.moveLeft();
        } else {
            budi.moveRight();
        }
        out.println(budi.getCurrentId());
    }

    static void hapus() {
        int X = in.nextInt();

        long sumOfRemovedScores = 0;
        Mesin currMesin = budi.current;
        if (X >= currMesin.numOfScores()) {
            sumOfRemovedScores = currMesin.sumOfAllScores;
            currMesin.clearAllScores();
            if (currMesin == funZone.tail) {
                budi.moveRight();
            } else {
                budi.moveCurrentMesinToRightmost();
            }
        } else {
            int currDeletedAmount = 0;
            for (int i = 0; i < X; i = currDeletedAmount) {
                Node toBeDeleted = currMesin.getHighestScore();

                if (toBeDeleted.count + currDeletedAmount <= X) {
                    sumOfRemovedScores += ((long) toBeDeleted.count * toBeDeleted.score);
                    currMesin.deleteScores(toBeDeleted.score, toBeDeleted.count);
                    currDeletedAmount += toBeDeleted.count;
                } else {
                    int deletedAmount = X - currDeletedAmount;
                    sumOfRemovedScores += ((long) deletedAmount  * toBeDeleted.score);
                    currMesin.deleteScores(toBeDeleted.score, deletedAmount);
                    currDeletedAmount += deletedAmount;                
                }
            }
        }
        out.println(sumOfRemovedScores);
    }

    static void lihat() {
        int L = in.nextInt();
        int H = in.nextInt();

        Mesin currMesin = budi.current;
        Node root = currMesin.scoreList.root;
        if (root != null) {
            out.println(currMesin.numOfScores() - (currMesin.scoreList.countLowerThan(root, L) + currMesin.scoreList.countGreaterThan(root, H)));
        } else {
            out.println(0);
        }
    }

    static void evaluasi() {
        funZone.head = funZone.mergeSort(funZone.head);
        Mesin temp = budi.current;
        int count = 0;
        while (temp.next != null) {
            count++;
            temp = temp.next;
        }
        funZone.tail = temp;
        out.println(jumlahMesin - count);
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
    }
}

class Node {
    int score, height, count, ownChild;
    Node left, right;

    Node(int score) {
        this.score = score;
        this.height = 0;
        this.count = 1;
    }

    int getChildCount() {
        int count = 0;
        if (left != null) {
            count += left.count + left.ownChild;
        }
        if (right != null) {
            count += right.count + right.ownChild;
        }
        return count;
    }   
}

class AVLTree {

    Node root;
    int currentCount;

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node rightRotate(Node node) {
        // TODO: implement right rotate

        Node temp = node.left;      // temp = x, node = y, temp2 = z
        Node temp2 = temp.right;  

        // Perform rotation  
        temp.right = node;  
        node.left = temp2;  

        // Update heights 
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;  
        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;  

        //Update child count
        node.ownChild = node.getChildCount();
        temp.ownChild = temp.getChildCount();

        // Return new root  
        return temp;
    }

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node leftRotate(Node node) {
        // TODO: implement left rotate

        Node temp = node.right;  // node = x, temp = y, temp2 = z
        Node temp2 = temp.left;  

        // Perform rotation  
        temp.left = node;  
        node.right = temp2;  

        // Update heights  
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;  
        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;  

        //Update child count
        node.ownChild = node.getChildCount();
        temp.ownChild = temp.getChildCount();

        // Return new root  
        return temp; 
    }

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node insertNode(Node node, int score) {
        // TODO: implement insert node

        // 1. Perform the normal BST rotation
        if (node == null) {
            return (new Node(score)); 
        }

        if (score < node.score) {
            if (node.right != null) {
                currentCount += node.right.count + node.right.ownChild;
            }
            currentCount += node.count;
            node.ownChild++;
            node.left = insertNode(node.left, score);  
        } else if (score > node.score) {
            node.ownChild++;
            node.right = insertNode(node.right, score);  
        } else {
            // Equal scores are allowed
            if (node.right != null) {
                currentCount += node.right.count + node.right.ownChild;
            }
            node.count++;
            return node;
        }

        // 2. Update height of this ancestor node
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;  

        // 3. Get the balance factor of this ancestor node to check whether this node became unbalanced
        int balance = getBalance(node);  

        // If this node becomes unbalanced, then there are 4 cases 
        // Left Left Case  
        if (balance > 1 && score < node.left.score) {
            return rightRotate(node);  
        }
            
        // Right Right Case  
        if (balance < -1 && score > node.right.score) {
            return leftRotate(node); 
        }
             
        // Left Right Case  
        if (balance > 1 && score > node.left.score) {  
            node.left = leftRotate(node.left);  
            return rightRotate(node);  
        }  

        // Right Left Case  
        if (balance < -1 && score < node.right.score) {  
            node.right = rightRotate(node.right);  
            return leftRotate(node);  
        }  

        // return the (unchanged) node pointer
        return node;
    }

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node deleteNode(Node node, int score, int deletedAmount) { // Node = Root
        // TODO: implement delete node

        // STEP 1: PERFORM STANDARD BST DELETE  
        if (node == null) {
            return node;
        }

        // If the score to be deleted is smaller than the root's score, then it lies in left subtree  
        if (score < node.score) {
            node.ownChild -= deletedAmount;
            node.left = deleteNode(node.left, score, deletedAmount);  
        } else if (score > node.score) { 
            // If the score to be deleted is greater than the root's score, then it lies in right subtree
            node.ownChild -= deletedAmount;
            node.right = deleteNode(node.right, score, deletedAmount);  
        } else {  
            // if score is same as root's score, then this is the node to be deleted  
            
            // If key is present more than once, simply decrement
            // count and return
            if (deletedAmount < node.count) {
                node.count -= deletedAmount;
                return node;
            }
            // ElSE, delete the node

            // node with only one child or no child  
            if ((node.left == null) || (node.right == null)) {  
                Node temp = null;  
                if (temp == node.left) {
                    temp = node.right;  
                } else {
                    temp = node.left; 
                }

                // No child case  
                if (temp == null) {  
                    temp = node;  
                    node = null;  
                } else { // One child case
                    node = temp; // Copy the contents of the non-empty child  
                }

            } else {  
                // node with two children: Get the inorder predecessor (greatest in the left subtree)  
                Node temp = findMax(node.left);  

                // Copy the inorder predecessor's data to this node  
                node.score = temp.score;  
                node.count = temp.count;

                // Delete the inorder predecessor  
                node.left = deleteNode(node.left, temp.score, temp.count);
            }  
        } 
        

        // If the tree had only one node then return  
        if (node == null) {
            return node;  
        }

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE  
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;  

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether this node became unbalanced)  
        int balance = getBalance(node);  

        // If this node becomes unbalanced, then there are 4 cases  
        // Left Left Case  
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node); 
        }

        // Right Right Case  
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);  
        }
             
        // Left Right Case  
        if (balance > 1 && getBalance(node.left) < 0) {  
            node.left = leftRotate(node.left);  
            return rightRotate(node);  
        }  

        // Right Left Case  
        if (balance < -1 && getBalance(node.right) > 0) {  
            node.right = rightRotate(node.right);  
            return leftRotate(node);  
        }  

        return node; 
    }

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node findMax(Node node) {  
        // loop down to find the rightmost leaf
        while (node.right != null) {
            node = node.right;  
        }
        
        return node;
    }

    // https://www.geeksforgeeks.org/floor-and-ceil-from-a-bst/
    // Function to find floor of a given input in BST. If input is less than the min score in BST, return null
    Node lowerBound(Node node, int value) {
        // TODO: return node with the lowest score that is >= value
        // Base case
        if (node == null) {
            return null;
        }
 
        // We found equal score
        if (node.score == value) {
            return node;
        }
 
        // If root's score is larger,
        // floor must be in left subtree
        if (node.score > value) {

            // Saving it's past node
            Node pastNode = node;
            if (node.left == null) {
                return node;
            }

            node = lowerBound(node.left, value);

            if (node == null) {
                node = pastNode;
            }

            return node;
        } else {
            // Else, either right subtree or root
            // has the floor value
            return lowerBound(node.right, value);
        }
    }

    // https://www.geeksforgeeks.org/floor-and-ceil-from-a-bst/
    // Function to find ceil of a given input in BST. If input is more than the max score in BST, return null
    Node upperBound(Node node, int value) {
        // TODO: return node with the greatest score that is <= value
        // Base case
        if (node == null) {
            return null;
        }
 
        // We found equal score
        if (node.score == value) {
            return node;
        }
 
        // If root's score is smaller,
        // ceil must be in right subtree
        if (node.score < value) {

            // Saving it's past node
            Node pastNode = node;
            if (node.right == null) {
                return node;
            }

            node = upperBound(node.right, value);

            if (node == null) {
                node = pastNode;
            }

            return node;
        } else {
            // Else, either left subtree or root
            // has the ceil value
            return upperBound(node.left, value);
        }
    }

    // Utility function to get height of node
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    // https://www.geeksforgeeks.org/count-greater-nodes-in-avl-tree/
    int countGreaterThan(Node node, int x) {
        int res = 0;
 
        // Search for x. While searching, keep
        // updating res if x is greater than
        // current node.
        while (node != null) {    
            if (node.score > x) {
                res += node.count;
                if (node.right != null) {
                    res += node.right.count + node.right.ownChild;
                } 
                node = node.left;
            } else if (node.score < x) {
                node = node.right;
            } else {
                if (node.right != null) {
                    res += node.right.count + node.right.ownChild;
                }
                break;
            }
        }
        return res;
    }

    int countLowerThan(Node node, int x) {
        int res = 0;
 
        // Search for x. While searching, keep
        // updating res if x is greater than
        // current node.
        while (node != null) {    
            if (node.score < x) {
                res += node.count;
                if (node.left != null) {
                    res += node.left.count + node.left.ownChild;
                }
                node = node.right;
            } else if (node.score > x){
                node = node.left;
            } else {
                if (node.left != null) {
                    res += node.left.count + node.left.ownChild;
                }
                break;
            }
        }
        return res;
    }
}

class Budi {
    FunZone funZone;
    Mesin current;

    Budi (FunZone funZone) {
        this.funZone = funZone;
        this.current = funZone.head;
    }

    int getCurrentId() {
        return current.id;
    }

    void moveRight() {
        if (current.next != null) {
            current = current.next;
        } else {
            current = funZone.head;
        }
    }

    void moveLeft() {
        if (current.prev != null) {
            current = current.prev;
        } else {
            current = funZone.tail;
        }
    }

    void moveCurrentMesinToRightmost() {
        Mesin toBeMoved = current;
        moveRight();

        if (toBeMoved != funZone.head) {
            // Delete current dari slide
            toBeMoved.prev.next = toBeMoved.next;
            toBeMoved.next.prev = toBeMoved.prev;
        } else {
            current.prev = null;
            funZone.head = current;
        }

        toBeMoved.prev = null;
        toBeMoved.next = null;

        funZone.insertLast(toBeMoved);
    }
}

class FunZone {
    Mesin head;
    Mesin tail;

    // Add new mesin at rightmost position
    void insertLast(Mesin newMesin) {
        if (head == null) {
            head = newMesin;
            tail = newMesin;
        } else {
            tail.next = newMesin;
            newMesin.prev = tail;
            tail = newMesin;
        }
    }

    // https://www.geeksforgeeks.org/merge-sort-for-doubly-linked-list/
    // Split a doubly linked list (DLL) into 2 DLLs of
    // half sizes
    Mesin split(Mesin head) {
        Mesin fast = head, slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        Mesin temp = slow.next;
        slow.next = null;
        return temp;
    }
  
    Mesin mergeSort(Mesin node) {
        if (node == null || node.next == null) {
            return node;
        }
        Mesin second = split(node);
  
        // Recur for left and right halves
        node = mergeSort(node);
        second = mergeSort(second);
  
        // Merge the two sorted halves
        return merge(node, second);
    }
  
    // Function to merge two linked lists
    Mesin merge(Mesin first, Mesin second) {
        // If first linked list is empty
        if (first == null) {
            return second;
        }
  
        // If second linked list is empty
        if (second == null) {
            return first;
        }
  
        // Pick the smaller value
        if (first.compareTo(second) < 0) {
            first.next = merge(first.next, second);
            first.next.prev = first;
            first.prev = null;
            return first;
        } else {
            second.next = merge(first, second.next);
            second.next.prev = second;
            second.prev = null;
            return second;
        }
    }
}

class Mesin implements Comparable<Mesin>{
    int id;
    Mesin prev;
    Mesin next;
    AVLTree scoreList;
    long sumOfAllScores;

    Mesin (int id) {
        this.id = id;
        scoreList = new AVLTree();
    }

    Node getRoot() {
        return scoreList.root;
    }

    Node getHighestScore() {
        return scoreList.findMax(scoreList.root);
    }

    int numOfScores() {
        if (scoreList.root == null) {
            return 0;
        } else {
            return scoreList.root.count + scoreList.root.ownChild;
        }
    }

    void addNewScore(int score) {
        scoreList.root = scoreList.insertNode(scoreList.root, score);
        sumOfAllScores += score;
    }

    void deleteScores(int score, int amount) {
        scoreList.root = scoreList.deleteNode(scoreList.root, score, amount);
        sumOfAllScores -= ((long) amount * score);
    }

    void clearAllScores() {
        scoreList.root = null;
        sumOfAllScores = 0;
    }

    @Override
    public int compareTo(Mesin other) {
        // TODO Auto-generated method stub
        if (this.numOfScores() == other.numOfScores()) {
            return this.id - other.id;
        }
        return other.numOfScores() - this.numOfScores();
    }
}