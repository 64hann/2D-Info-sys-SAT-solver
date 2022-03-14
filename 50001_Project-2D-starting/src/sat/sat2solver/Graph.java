package sat.sat2solver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Graph {
    static final int MAX = 100000;
    private int V; // no of vertices
    private LinkedList<Integer> adj[]; //Adjacency list to show vertices and edges
//    static ArrayList<ArrayList> scc = new ArrayList<>();
    static int counter = 1;
    static int[] scc_array = new int[MAX];

    // Constructor --> create empty graph with vertices but no edges
    Graph(int v){
        V = v;
        adj = new LinkedList[v+1];
        for (int i = 0; i <= v ; i++){
            adj[i] = new LinkedList();
        }

    }
    public void addEdge(int v,int w){
        adj[v].add(w); // v -> w
    }
    void DFS_recursive(int v,boolean visited[]){
        // Once it is visited -> true
        visited[v] = true;
        System.out.println(v + " ");

        int n;

        Iterator<Integer> i = adj[v].iterator();
        while (i.hasNext()){
            n  = i.next();
            if (!visited[n]){
                DFS_recursive(n,visited);

            }

        }
    }
    void DFS_recursive2(int v, boolean visited[]){
        visited[v] = true;
        //System.out.println(v + " ");
//        scc_list.add(v);

        int n;

        Iterator<Integer> i = adj[v].iterator();
        while (i.hasNext()){
            n  = i.next();
            if (!visited[n]){
                DFS_recursive2(n,visited);
            }

        }
        // counter the same -> in the same SCC
        scc_array[v] = counter;
//        return scc_list;
    }
    Graph getReverse(){
        Graph g = new Graph(V);
        for (int v = 0; v < V; v++)
        {
            // Create a reverse of the original graph
            // from v -> i to i-> v
            Iterator<Integer> i =adj[v].listIterator();
            while(i.hasNext())
                g.adj[i.next()].add(v);
        }
        return g;
    }
    // fill stack according to their finishing times
    void orderFinishingTimes(int v, boolean visited[], Stack stack)
    {
        // node is visited
        visited[v] = true;

        // Recur for all the vertices adjacent to this vertex
        Iterator<Integer> i = adj[v].iterator();
        while (i.hasNext())
        {
            int n = i.next();
            if(!visited[n])
                orderFinishingTimes(n, visited, stack);
        }

        // All its child vertices have been explored
        // push v to stack
        stack.push(v);
    }
    // The main function that finds and prints all strongly
    // connected components
    Stack returnStack(){
        Stack stack = new Stack();

        // Mark all the vertices as not visited
        boolean visited[] = new boolean[V];
        for(int i = 0; i < V; i++)
            visited[i] = false;

        // Fill vertices in stack according to their finishing times
        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                orderFinishingTimes(i, visited, stack);
        return stack;
    }
    int[] returnSCCs()
    {
//        // initialise empty stack
//        Stack stack = new Stack();
//
//        // Mark all the vertices as not visited
//        boolean visited[] = new boolean[V];
//        for(int i = 0; i < V; i++)
//            visited[i] = false;
//
//        // Fill vertices in stack according to their finishing times
//        for (int i = 0; i < V; i++)
//            if (visited[i] == false)
//                orderFinishingTimes(i, visited, stack);
        Stack stack = returnStack();
        // Create a reversed graph
        Graph gr = getReverse();

        boolean visited[] = new boolean[V];
        for(int i = 0; i < V; i++)
            visited[i] = false;

        // Now process all vertices in order defined by Stack
        while (!stack.empty())
        {
            // Pop a vertex from stack
            int v = (int)stack.pop();

            // get strongly connected component of the popped vertex
            if (visited[v] == false)
            {
                gr.DFS_recursive2(v, visited);
                //System.out.println();
//                scc.add(scc_list);
                // increase counter as scc is completed
                counter++;
            }
        }
        return scc_array;
    }

    public static void main(String[] args) {
        Graph g = new Graph(8);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(2, 4);
        g.addEdge(3, 0);
        g.addEdge(4, 5);
        g.addEdge(5, 6);
        g.addEdge(6, 4);
        g.addEdge(6, 7);

        System.out.println("Strongly Connected Components:");
        g.returnSCCs();
//        System.out.println(scc);

    }
    }


