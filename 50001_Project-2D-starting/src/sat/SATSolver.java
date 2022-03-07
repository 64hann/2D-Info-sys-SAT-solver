package sat;

import immutable.*;
import sat.env.*;
import sat.formula.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
//Done by Krit
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
        Environment newEnv = new Environment(); 

        return solve(formula.getClauses(), newEnv); 
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

        Bool b;
        if (smallest.isUnit())
        {
            if (l instanceof PosLiteral)
            {
                b = Bool.TRUE;
            }
            else
            {
                b = Bool.FALSE;
            }
            env = env.put(v, b);
            return solve(substitute(clauses, l), env);
        }
        else
        {
            if (l instanceof NegLiteral)
            {
                l = l.getNegation();
            }
            b = Bool.TRUE;
            env = env.put(v, b);
            Environment newenv = solve(substitute(clauses, l), env);
            if (newenv == null)
            {
                Bool newb = Bool.FALSE;
                env = env.put(v, newb);
                return solve(substitute(clauses, l.getNegation()), env);
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
        ImList<Clause> sub = new EmptyImList<Clause>();
        for (Clause c : clauses)
        {
            Clause newc = c.reduce(l);
            if (newc != null)
            {
                sub = sub.add(newc);
            }
        }
        return sub;
    }

}
