package sat.sat2solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
        //Parsing is adapted from 2D Infosys
        ArrayList<String> storedLines = new ArrayList<>();
        ArrayList<String> storedClauses = new ArrayList<>();
        ArrayList<String> storedLines2 = new ArrayList<>();
        ArrayList<ArrayList> formula = new ArrayList<>();
        String path = args[0];
        int var = 0;
        int clauses = 0;
        //help from https://www.javatpoint.com/how-to-read-file-line-by-line-in-java
        try {
            //reading the file
            File file=new File(path);
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            String line;
            Boolean cnfChecker = false;
            while((line=br.readLine())!=null)
            {
                if (line.length() > 0) {
                    if(cnfChecker){
                        storedLines.add(line);
                    }    //line feed

                    if(line.substring(0,1).equals("p")){
                        String[] cnfLine = line.split("\\s+");
                        var = Integer.parseInt(cnfLine[2]);
                        clauses = Integer.parseInt(cnfLine[3]);
                        cnfChecker = true;
                    }
                }

            }
            fr.close(); //closes the file

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        //Making the clauses into individual words
        for(String line : storedLines){
            //https://javarevisited.blogspot.com/2016/10/how-to-check-if-string-contains-another-substring-in-java-indexof-example.html#axzz7MePsa4uU
            String[] strList = line.split("\\s+");
            for(String str : strList){
                storedClauses.add(str);
            }
        }
        //Reordering to make sure the line is before 0, then remove 0.
        String newLine = "";
        for(String line : storedClauses){
            if(newLine.equals("")){
                newLine = line;
            }
            else if (line.equals("0")){
                storedLines2.add(newLine);
                newLine = "";
            }
            else{
                newLine = newLine + " " + line;
            }

        }

        for(String line : storedLines2){
            String[] litList = line.split("\\s+");
            ArrayList<Integer> clause = new ArrayList<>();

            int j = 0;
            for (String lit : litList) {
                clause.add(Integer.parseInt(lit));//

                j += 1;

            }
            formula.add(clause);
        }

        int[] a = new int[formula.size()];
        int[] b = new int[formula.size()];

        for(int i = 0; i < formula.size(); i++){
            a[i] = (Integer) formula.get(i).get(0);
            b[i] = (Integer) formula.get(i).get(1);
            System.out.println(a[i] + "  " + b[i]);
        }

        //solving
        sat2solver s = new sat2solver();

        s.isSatisfiable(var,clauses,a,b);
//        s.getSolution(n,m,a,b);

    }
}
