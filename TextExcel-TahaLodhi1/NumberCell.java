// Student Name
// Period X

/*
 * In Checkpoint #1: this class is ignored.
 * In Checkpoint #2: the NumberCell will hold a number only.
 *    Example:  a1 = 5
 * In Checkpoint #3: the NumberCell may hold an expression.
 *    Example:  a1 = ( 2 + a2 / a3 * a4 )
 * In Final Submission: the NumberCell may hold functions.
 *    Example:  a1 = ( sum b1 - b5 )
 */
public class NumberCell extends Cell {

    /*
     * This returns the string to be presented in the grid.
     */
    public String toString() {
        // TODO: You will need to CHANGE this in later checkpoints.
        // Leverage this method when fulfilling the command, "display [cell]"
        return getValue() + "";
    }

    /*
     * This will return the number for this cell.
     */
    public double getValue() {
        // TODO: You will need to CHANGE this in later checkpoints.
        // Leverage this method when fulfilling the command, "value [cell]"
        return Double.parseDouble(getExpression());
    }
}
