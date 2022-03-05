package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.*;
import sat.formula.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();




    // TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String[] args){
        ArrayList<String> storedClauses = new ArrayList<>();
        ImList<Integer> l = new EmptyImList<Integer>();
        String path = args[0];
        Formula f2 = new Formula();
        System.out.println(path);
        String var = "";
        //https://www.javatpoint.com/how-to-read-file-line-by-line-in-java
        try {
            File file=new File(path);
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr); //constructs a string buffer with no characters
            String line;
            Boolean cnfChecker = false;
            while((line=br.readLine())!=null)
            {

                if (line.length() > 0) {
                    if(cnfChecker){
                        storedClauses.add(line);
                    }    //line feed

                    if(line.substring(0,1).equals("p")){
                        String[] cnfLine = line.split("\\s+");
                        var = cnfLine[2];
                        cnfChecker = true;
                    }
                }

            }
            fr.close(); //closes the file
            System.out.println("file content: ");
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        for(String line : storedClauses) {
            Clause clause = new Clause();
            String[] litList = line.split("\\s+");

            for (String lit : litList) {
                boolean checker = true;
                if (lit.substring(0, 1).equals("-")) {
                    for(String i : litList){
                        if(lit.substring(1).equals(i)){
                            checker = false;
                        }
                    }
                    if(checker){

                        String negLit = lit.substring(1);
                        String negLit2 = negLit;
                        clause = clause.add(NegLiteral.make(negLit2));
                        System.out.print(lit + " ");
                        System.out.print(clause);
                    }
                }
                else {
                    for(String i : litList){
                        if(lit.equals(i.substring(1))){
                            checker = false;
                        }
                    }
                    if(checker){


                        clause = clause.add(PosLiteral.make(lit));
                        System.out.print(lit + " ");
                        System.out.print(clause);
                    }
                }

            }
            //System.out.println();
            f2 = f2.addClause(clause);
        }
        //System.out.println(f2);
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Environment e = SATSolver.solve(f2);
        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
        if (e == null) {
            System.out.println("not satisfiable");
        }
        else {
            System.out.println("satisfiable\n"+e);
            try {
                //Sample from https://www.homeandlearn.co.uk/java/write_to_textfile.html
                String path2 = "/Users/nicho/Desktop/BoolAssignment.txt";
                PrintWriter out = new PrintWriter(new FileWriter(path2));
                int i = 1;
                while(i <= Integer.parseInt(var)){
                    Bool result = e.get(new Variable(Integer.toString(i)));
                    if (result == Bool.TRUE) {
                        out.println(i + ":TRUE");
                    }
                    else {
                        out.println(i + ":FALSE");
                    }
                    i += 1;
                }
                out.close();

            } catch (IOException env) {
                env.printStackTrace();
            }
        }
    }


	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}
