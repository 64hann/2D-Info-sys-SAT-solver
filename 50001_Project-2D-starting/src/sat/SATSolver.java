package sat;

import immutable.*;
import sat.env.*;
import sat.formula.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula)
    {
        Environment newEnv = new Environment(); //environment

        return solve(formula.getClauses(), newEnv); //recursion
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // find smallest clause
        // if number of literals = 1, call substitute()
        // else use the code here
        // choose a literal here, set it to be TRUE
        if (clauses.isEmpty()) //check if variable has a truth value
        {
            return env; //return SAT if there is none
        }
        // smallest clause
        Clause smallest = clauses.first();
        for (Clause c: clauses)
        {
            if (c.isEmpty())
            {
                return null;
            }
            else if (c.size() < smallest.size())
            {
                smallest = c;
            }
        }
        Literal l = smallest.chooseLiteral();
        Variable v = l.getVariable();
        //System.out.println(v.toString());
        if (smallest.isUnit())
        {
            Bool b;
            if (l instanceof PosLiteral)
            {
                b = Bool.TRUE;
            }
            else
            {
                b = Bool.FALSE;
            }
            env = env.put(v, b);
            //System.out.println(smallest.toString());
            return solve(substitute(clauses, l), env);
        }
        else
        {
            if (l instanceof NegLiteral)
            {
                l = l.getNegation();
            }
            Environment newenv = solve(substitute(clauses, l),env.put(v, Bool.TRUE));
            //System.out.println(smallest.toString());
            if (newenv == null)
            {
                return solve(substitute(clauses, l.getNegation()),env.put(v, Bool.FALSE));
            }
            return newenv;
        }
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        ImList<Clause> substituted = new EmptyImList<Clause>();
        for (Clause c : clauses)
        {
            Clause newc = c.reduce(l);
            if (newc != null)
            {
                substituted = substituted.add(newc);
            }
        }
        return substituted;
        // use if number of literals in a clause = 1
        // assign that literal value for the entire list
        // eliminate all the other blanks that are not literals
        // call solve
    }

}
