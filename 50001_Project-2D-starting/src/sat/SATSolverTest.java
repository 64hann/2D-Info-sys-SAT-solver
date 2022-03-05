package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        FileInputStream cnf= null;     //opens a connection to an actual file
        try {
            cnf = new FileInputStream(path);
            System.out.println("file content: ");
            int r=0;
            String cnfCheck = "";
            boolean lChecker = false;
            boolean cnfChecker = false;
            boolean negChecker = false;
            String fullString = "";
            String storeNum = "";
            while((r=cnf.read())!=-1)
            {
                cnfCheck = cnfCheck + (char)r;
                if(cnfCheck.length() > 3){
                    cnfCheck = cnfCheck.substring(1);
                    //System.out.println(cnfCheck);
                    if(cnfCheck.equals("cnf")){
                        cnfChecker = true;
                        //System.out.println(cnfCheck + "hi");
                    }
                }
                if(!lChecker && r == 10 && cnfChecker){
                    lChecker = true;
                }
                if(!Character.isAlphabetic((char)r) && cnfChecker && lChecker){
                    String store = Character.toString((char)r);

                    if(r != 32 && r != 10 && (char)r != '-'){
                        store = storeNum + store;
                    }

                    //if(){
                        //if(r == 10 || r == 32){
                            //l = l.add(Integer.parseInt(store));
                        //}
                    //}
                    if(r == 32 || r == 10 || (char)r == '-'){
                        storeNum = "";
                    }
                    if(negChecker){
                        store = "-" + store;
                        negChecker = false;
                    }
                    if((char)r == '-'){
                        negChecker = true;
                    }

                    if(r != 32 && r != 10 && (char)r != '-'){
                        //System.out.print((char)r+ " ");
                        storeNum = store;
                        fullString = fullString +" " + store;
                        System.out.print(store + " ");
                    }

                }      //prints the content of the file
            }
            System.out.println(fullString);
            String[] splitStr = fullString.trim().split("\\s+");
            String tempStr = "";
            String space = " ";
            for(int i = 0; i < splitStr.length; i++){
                System.out.println(i + " " + splitStr[i]);
                //System.out.println(Integer.parseInt(splitStr[i]));
                String tempNum = splitStr[i];
                if(tempStr.equals("")){
                    tempStr = tempStr + tempNum;
                }
                else{
                    tempStr = tempStr + space + tempNum;
                }
                if(tempNum.equals("0")){
                    storedClauses.add(tempStr.substring(0, tempStr.length() - 2));
                    tempStr = "";
                }
                //System.out.println(tempNum + "tempNum");
                //System.out.println(l);
            }
            System.out.println();
            System.out.println(storedClauses);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : storedClauses) {
            System.out.println(line);
        }

        for (String line : storedClauses) {
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

                        System.out.print(lit + " ");
                        System.out.print(clause);
                        String negLit = lit.substring(1);
                        String negLit2 = negLit;
                        clause = clause.add(NegLiteral.make(negLit2));
                    }
                }
                else {
                    for(String i : litList){
                        if(lit.equals(i.substring(1))){
                            checker = false;
                        }
                    }
                    if(checker){

                        System.out.print(lit + " ");
                        System.out.print(clause);

                        clause = clause.add(PosLiteral.make(lit));
                    }
                }

            }
            System.out.println();
            f2 = f2.addClause(clause);
        }
        System.out.println(f2);
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Environment e = SATSolver.solve(f2);
        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
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
