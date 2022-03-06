package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/
import java.io.*;
import java.util.ArrayList;

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
        ArrayList<String> storedLines = new ArrayList<>();
        ArrayList<String> storedClauses = new ArrayList<>();
        ArrayList<String> storedLines2 = new ArrayList<>();
        String path = args[0];
        Formula f2 = new Formula();
        //System.out.println(path);
        String var = "";
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
                        var = cnfLine[2];
                        cnfChecker = true;
                    }
                }

            }
            fr.close(); //closes the file
            //System.out.println("file content: ");
            
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
        //checking clauses for posliteral or negliteral and adding to clauselist
        //then adding each clauselist to formula list
        for(String line : storedLines2){
            Clause clause = new Clause();
            String[] litList = line.split("\\s+");

            for (String lit : litList) {
                boolean checker = true;
                if (lit.substring(0, 1).equals("-")) {
                    if(checker){
                        clause = clause.add(NegLiteral.make(lit.substring(1)));
                    }
                }
                else {
                    if(checker){
                        clause = clause.add(PosLiteral.make(lit));
                    }
                }

            }
            f2 = f2.addClause(clause);
        }
        //Starting to solve
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Environment e = SATSolver.solve(f2);
        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
        if (e == null) {
            System.out.println("Not Satisfiable");
        }
        else {
            System.out.println("Satisfiable");
            System.out.println(e);
            try {
                //Sample from https://www.homeandlearn.co.uk/java/write_to_textfile.html
                String path2 = "BoolAssignment.txt";
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
