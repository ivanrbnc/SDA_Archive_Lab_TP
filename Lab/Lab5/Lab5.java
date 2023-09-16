import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.StringTokenizer;

public class Lab5 {

    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            // TODO: process inputs
            String playerName = in.next();
            int playerKey = in.nextInt();
            tree.root = tree.insertNode(tree.root, playerKey, playerName);
        }

        int numOfQueries = in.nextInt();
        for (int i = 0; i < numOfQueries; i++) {
            String cmd = in.next();
            if (cmd.equals("MASUK")) {
                String newPlayerName = in.next();
                int newPlayerKey = in.nextInt();
                tree.root = tree.insertNode(tree.root, newPlayerKey, newPlayerName);
                handleQueryMasuk(newPlayerKey);
            } else {
                int lowerBound = in.nextInt();
                int upperBound = in.nextInt();
                handleQueryDuo(lowerBound, upperBound);
            }
        }
        
        out.close();
    }

    static void handleQueryMasuk(int value) {
        // TODO

        // Refresh static counter & run it's function again..
        AVLTree.counterLowerThan = 0;
        out.println(tree.lowerThan(tree.root, value));
    }

    static void handleQueryDuo(int lowerBound, int upperBound) {
        // TODO

        // Get lowerBound & upperBound from root
        Node lowerBoundNode = tree.lowerBound(tree.root, lowerBound);
        Node upperBoundNode = tree.upperBound(tree.root, upperBound);

        // Initiate first & second person
        String firstPerson = " ";
        String secondPerson = " ";

        // If the LB & UB is refering the same node & it's inside is only one.
        if (lowerBoundNode.equals(upperBoundNode) && lowerBoundNode.inside.size() == 1) {
            out.println(-1 + " " + -1);
        } else {

            // Get the person for each bound
            firstPerson = lowerBoundNode.inside.pop();
            secondPerson = upperBoundNode.inside.pop();

            // If the node is empty after pop, delete it.
            if (lowerBoundNode.inside.isEmpty()) {
                tree.deleteNode(tree.root, lowerBoundNode.key);
            } 

            // If the node is empty after pop, delete it.
            if (upperBoundNode.inside.isEmpty()) {
                tree.deleteNode(tree.root, upperBoundNode.key);
            } 

            // Print format..
            if (firstPerson.compareTo(secondPerson) < 0) {
                out.println(firstPerson + " " + secondPerson);
            } else {
                out.println(secondPerson + " " + firstPerson);
            }
        }
    }

    // taken from https://codeforces.com/submissions/Petr
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

// TODO: modify as needed
class Node {
    int key, height;
    Node left, right;
    Stack<String> inside = new Stack<>();

    Node(int key, String playerName) {
        this.key = key;
        this.height = 1;
        inside.push(playerName);
    }
}

class AVLTree {

    Node root;

    // Counter for lowerThan function
    static int counterLowerThan = 0;

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node rightRotate(Node node) {
        // TODO: implement right rotate

        Node temp = node.left;  
        Node temp2 = temp.right;  

        // Perform rotation  
        temp.right = node;  
        node.left = temp2;  

        // Update heights 
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;  
        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;  

        // Return new root  
        return temp;
    }

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node leftRotate(Node node) {
        // TODO: implement left rotate

        Node temp = node.right;  
        Node temp2 = temp.left;  

        // Perform rotation  
        temp.left = node;  
        node.right = temp2;  

        // Update heights  
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;  
        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;  

        // Return new root  
        return temp; 
    }

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node insertNode(Node node, int key, String playerName) {
        // TODO: implement insert node

        // 1. Perform the normal BST rotation
        if (node == null) {
            return (new Node(key, playerName)); 
        }

        if (key < node.key) {
            node.left = insertNode(node.left, key, playerName);  
        } else if (key > node.key) {
            node.right = insertNode(node.right, key, playerName);  
        } else {
            // Equal keys are allowed
            node.inside.push(playerName);
            return node;
        }

        // 2. Update height of this ancestor node
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;  

        // 3. Get the balance factor of this ancestor node to check whether this node became unbalanced
        int balance = getBalance(node);  

        // If this node becomes unbalanced, then there are 4 cases 
        // Left Left Case  
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);  
        }
            
        // Right Right Case  
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node); 
        }
             
        // Left Right Case  
        if (balance > 1 && key > node.left.key) {  
            node.left = leftRotate(node.left);  
            return rightRotate(node);  
        }  

        // Right Left Case  
        if (balance < -1 && key < node.right.key) {  
            node.right = rightRotate(node.right);  
            return leftRotate(node);  
        }  

        // return the (unchanged) node pointer
        return node;
    }

    // https://www.geeksforgeeks.org/deletion-in-an-avl-tree/amp/
    Node deleteNode(Node node, int key) { // Node = Root
        // TODO: implement delete node

        // STEP 1: PERFORM STANDARD BST DELETE  
        if (node == null) {
            return node;
        }

        // If the key to be deleted is smaller than the root's key, then it lies in left subtree  
        if (key < node.key) {
            node.left = deleteNode(node.left, key);  
        } else if (key > node.key) { 
            // If the key to be deleted is greater than the root's key, then it lies in right subtree
            node.right = deleteNode(node.right, key);  
        } else {  
            // if key is same as root's key, then this is the node to be deleted  

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
                // node with two children: Get the inorder successor (smallest in the right subtree)  
                Node temp = minValueInNode(node.right);  

                // Copy the inorder successor's data to this node  
                node.key = temp.key;  

                // Delete the inorder successor  
                node.right = deleteNode(node.right, temp.key);  
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
    /* Given a non-empty binary search tree, return the node with minimum key value found in that tree.  
    Note that the entire tree does not need to be searched. */
    Node minValueInNode(Node node) {  
        // loop down to find the leftmost leaf
        while (node.left != null) {
            node = node.left;  
        }
        
        return node;
    }

    // https://www.geeksforgeeks.org/floor-and-ceil-from-a-bst/
    // Function to find floor of a given input in BST. If input is less than the min key in BST, return null
    Node lowerBound(Node node, int value) {
        // TODO: return node with the lowest key that is >= value
        // Base case
        if (node == null) {
            return null;
        }
 
        // We found equal key
        if (node.key == value) {
            return node;
        }
 
        // If root's key is larger,
        // floor must be in left subtree
        if (node.key > value) {

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
    // Function to find ceil of a given input in BST. If input is more than the max key in BST, return null
    Node upperBound(Node node, int value) {
        // TODO: return node with the greatest key that is <= value
        // Base case
        if (node == null) {
            return null;
        }
 
        // We found equal key
        if (node.key == value) {
            return node;
        }
 
        // If root's key is smaller,
        // ceil must be in right subtree
        if (node.key < value) {

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

    // Counting how many node that is lower than value by using pre-order traversal 
    int lowerThan(Node node, int value) {  
        if (node != null) { 
            if (node.key < value) {
                counterLowerThan += node.inside.size();
            }
            lowerThan(node.left, value);  
            lowerThan(node.right, value);  
        }

        return counterLowerThan;
    } 
}