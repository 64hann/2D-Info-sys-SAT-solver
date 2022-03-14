package sat.sat2solver;

import java.util.*;


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


    static void isSatisfiable(int n, int m, int a[], int b[]) {
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
                System.out.println("UNSATISFIABLE");

            }
        }
        // No such variables x and -x exist which lie
        // in same SCC
        System.out.println("SATISFIABLE");
        getSolution(n,m,a,b,g);

    }
    public static void getSolution(int n, int m, int a[], int b[], Graph g){
        // n -> no of variables
        // m -> no of clauses
        // create implies statement A v B = ( notA -> B) ^ (notB -> A)

//             Mark all the vertices as not visited
//            Stack stack = new Stack();
//
//            int V = m+n;
//            boolean visited[] = new boolean[V];
//            for(int j = 0; j < V; j++){
//                visited[j] = false;
//                g.orderFinishingTimes(j,visited,stack);
//            }
            Stack stack = g.returnStack();
//            // reverse topological sort
//            System.out.println(stack);
//            System.out.println(stack);
            ArrayList<Integer> list = new ArrayList(stack);
//            System.out.println(list);
//            Collections.reverse (list);
//            System.out.println(list);
//            // if index(x) < index(x + n)
            // x = False
            // if index(x) > index(x + n)
            // x = True
            // Create Hashmap to store the 0s and 1s
            HashMap <Integer,String> map = new HashMap<Integer, String>();
            for (int i=1;i<=n;i++){
                if (list.indexOf(i) > list.indexOf(i+n)){
//                    System.out.println(0);
                    map.put(i,"0");

                }
                else{
//                    System.out.println(1);
                    map.put(i,"1");
                }
            }

        String string ="";
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            //System.out.println(entry.getKey() + " = " + entry.getValue());
            string = string.concat(entry.getValue());
        }
        System.out.println(string);

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


    public static void main(String[] args) {
//        int a[] = { 1, 2, 3, -1, -2};
//        int b[] = { 2, 3, 4, -3, -4};
//
        int n = 4, m = 5;
//        int n = 2;  int m = 3;
//        int a[] = {1, 2, -1};
//        int b[] = {2, -1, -2};
//         n = 2; m = 4;
//        int a[] = {1, -1, 1, -1};
//        int b[] = {2, 2, -2, -2};
//        int a[]= {1,2};
//        int b[] = {-1,-3};
        n = 3 ; m= 2;
        // non satifistable
        int a[]= {1,2};
        int b[] = {-1,-3};
        n = 3 ; m= 2;
        sat2solver s = new sat2solver();

        s.isSatisfiable(n,m,a,b);
//        s.getSolution(n,m,a,b);

    }
}
