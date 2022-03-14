package sat.sat2solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class sat2solver {
    @SuppressWarnings("unchecked")
    static final int MAX = 100000;
//    static List<List<Integer>> adj = new ArrayList();
//
//    @SuppressWarnings("unchecked")
//    static List<List<Integer> > adjInv = new ArrayList();
//    static boolean[] visited = new boolean[MAX];
//    static boolean[] visitedInv = new boolean[MAX];
//    static Stack<Integer> s = new Stack<Integer>();

    // This array will store the SCC that the
// particular node belongs to
    static int[] scc = new int[MAX];

    // counter maintains the number of the SCC
    static int counter = 1;

//    // Adds edges to form the original graph void
//    static void addEdges(int a, int b)
//    {
//        adj.get(a).add(b);
//    }
//
//    // Add edges to form the inverse graph
//    static void addEdgesInverse(int a, int b)
//    {
//        adjInv.get(b).add(a);
//    }


    static boolean isSatisfiable(int n, int m, int a[], int b[]) {
        // n -> no of variables
        // m -> no of clauses
        // create implies statement A v B = ( notA -> B) ^ (notB -> A)
        Graph g = new Graph(m + n);
        for (int i = 0; i < m; i++) {
            // x is mapped to x
            // -x is mapped to n + x (this is to make it easier to check)
            if (a[i] > 0 && b[i] > 0) {
                g.addEdge(a[i] + n, b[i]);
                g.addEdge(b[i] + n, a[i]);
            } else if (a[i] > 0 && b[i] < 0) {
                g.addEdge(a[i] + n, n - b[i]);
                g.addEdge(-b[i], a[i]);
            } else if (a[i] < 0 && b[i] > 0) {
                g.addEdge(-a[i], b[i]);
                g.addEdge(b[i] + n, n - a[i]);
            } else {
                g.addEdge(-a[i], n - b[i]);
                g.addEdge(-b[i], n - a[i]);
            }

        }
//        Graph gr = g.getReverse();
        scc = g.returnSCCs();
//        System.out.println(g.scc);
        for (int i = 1; i <= n; i++) {

            // For any 2 variable x and -x lie in
            // same SCC
            if (scc[i] == scc[i + n]) {
                System.out.println("The given expression" +
                        "is unsatisfiable.");
                return false;
            }
        }
        // No such variables x and -x exist which lie
        // in same SCC
        System.out.println("The given expression " +
                "is satisfiable.");
        return true;

    }
    public void getSolution(int n, int m, int a[], int b[]){
        // n -> no of variables
        // m -> no of clauses
        // create implies statement A v B = ( notA -> B) ^ (notB -> A)
        Graph g = new Graph(m + n);
        for (int i = 0; i < m; i++) {
            // x is mapped to x
            // -x is mapped to n + x (this is to make it easier to check)
            if (a[i] > 0 && b[i] > 0) {
                g.addEdge(a[i] + n, b[i]);
                g.addEdge(b[i] + n, a[i]);
            } else if (a[i] > 0 && b[i] < 0) {
                g.addEdge(a[i] + n, n - b[i]);
                g.addEdge(-b[i], a[i]);
            } else if (a[i] < 0 && b[i] > 0) {
                g.addEdge(-a[i], b[i]);
                g.addEdge(b[i] + n, n - a[i]);
            } else {
                g.addEdge(-a[i], n - b[i]);
                g.addEdge(-b[i], n - a[i]);
            }
//             Mark all the vertices as not visited
//            Stack stack = new Stack();
//            int V = m+n;
//            boolean visited[] = new boolean[V];
//            for(int j = 0; j < V; i++)
//                visited[j] = false;
//            g.orderFinishingTimes(m+n,visited,stack);
//            // order is reversed , topological sort
//            System.out.println(stack);
//            ArrayList<Integer> list = new ArrayList(stack);
//            System.out.println(list);
//            // if index(x) < index(x + n)
            // x = False
            // if index(x) > index(x + n)
            // x = True
//            for (int k : list){
//                if (list.indexOf(k) < list.indexOf(k+n)){
//                    System.out.println(0);
//                }
//            }



        }
        // Return solution
        // initialise empty stack
        // reverse the stack to get topological sort
        // put in ArrayList
        // if index(V) <= index(U)
        // there is path from v to u
        // if index(x) < index(notX)
        // x = False
        // if index(x) > index(notX)
        // x = True

    }

    public static void main(String[] args) {
        int a[] = { 1, -2, -1, 3, -3, -4, -3 };
        int b[] = { 2, 3, -2, 4, 5, -5, 4 };

        int n = 5, m = 7;
//        n = 2; m = 3;
//        int a[] = {1, 2, -1};
//        int b[] = {2, -1, -2};
//         n = 2; m = 4;
//        int a[] = {1, -1, 1, -1};
//        int b[] = {2, 2, -2, -2};
        sat2solver s = new sat2solver();
        s.isSatisfiable(n,m,a,b);
        s.getSolution(n,m,a,b);

    }
}
