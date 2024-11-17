// Student Name
// Period X
// Text Excel Project

import java.io.*;
import java.util.*;

/*
 * The Grid class will hold all the cells. It allows access to the cells via the
 * public methods. It will create a display String for the whole grid and process
 * many commands that update the cells. These command will include
 * sorting a range of cells and saving the grid contents to a file.
 *
 */
public class Grid extends GridBase {

  // These are called instance fields.
  // They are scoped to the instance of this Grid object.
  // Use them to keep track of the count of columns, rows and cell width.
  // They are initialized to the prescribed default values.
  // Notice that there is no "static" here.
  private int colCount = 4; // 7 is default this is just for ease must revert
  private int rowCount = 3; // 10 is default this is just for ease must revert
  private int cellWidth = 4; // 9 is default this is just for ease must revert
  String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private Cell[][] matrix;

  public static PrintStream outputStream = System.out;

  public Grid() {
    matrix = new Cell[rowCount][colCount];
  }

  // This function prints dash lines while accounting for over lap with +
  // It is called both for the top row and latter for each individual row
  private String printDashes() {
    String localResult = "----+"; // hard coded for first col width
    for (int col = 0; col < colCount; col++) {
      for (int w = 0; w < cellWidth; w++) {
        // outputStream.print("-");
        localResult += "-"; // for each cell width
      }
      localResult += "+";
    }
    return localResult;
  }

  // This function aligns the Column title [A, B, C..] towards the center of
  // column
  // It has the logic to accomodate even width cells where there is no exact
  // center
  // Test cases align col to right side i.e. A, B and hence there would be one
  // less
  // char on the right
  private String centeralignCol(int i, String format) {
    String text = alphabets.substring(i, i + 1);

    int padding;

    // Add +1 in below division which gives us right middle index for even sized
    // cell
    padding = (cellWidth - text.length() + 1) / 2;
    String centered = "";

    // if cell width was even, then we need to pad one less element on right.
    if (cellWidth % 2 < padding) {
      int suffix = padding - 1;
      centered = String.format("%" + padding + "s%s%" + suffix + "s", "", text, "");
    }

    else // this is the normal case with odd widht column with a perfect center
      centered = String.format("%" + padding + "s%s%" + padding + "s", "", text, "");

    return String.format(format, centered);
  }

  // Actual grid printing function which uses several other helper functions to
  // print
  // various secions of the excel
  public String print() {
    String result = "";
    String format = "%-" + cellWidth + "s";
    String formatcell = "%" + cellWidth + "s";
    result += "    "; // 4 empty slots for top left
    for (int i = 0; i < colCount; i++) {
      result += "|";

      result += centeralignCol(i, format);
    }
    result += "|\n";
    result += printDashes(); // this is for the column header row "A B C.."

    result += "\n";
    for (int row = 0; row < rowCount; row++) {

      String frmt = "%3s"; // width is 4 but we need to leave a space
      result += String.format(frmt, Integer.toString(row + 1));
      result += " "; // the 4th space inserted here
      String truncell = "";
      for (int col = 0; col < colCount; col++) {

        result += "|";
        if (matrix[row][col] == null) {
          result += String.format(format, "".toString());
        } else {
          if (matrix[row][col].toString().length() > cellWidth) {
            truncell = matrix[row][col].toString().substring(0, cellWidth);

          } else {
            truncell = matrix[row][col].toString();
          }

          result += String.format(formatcell, truncell);
        }
      } // end each row
      result += "|";

      // print hyphens after each row
      result += "\n";
      result += printDashes(); // this is after each row

      result += "\n";
    } // end all rows
    return result;
  }

  public int rows() {
    // outputStream.println(rowCount);
    return rowCount;
  }

  public int cols() {
    // outputStream.println(colCount);
    return colCount;
  }

  public int width() {
    // outputStream.println(cellWidth);
    return cellWidth;
  }

  public int resizerows(int input) {
    rowCount = input;
    matrix = new Cell[rowCount][colCount];
    return rowCount;
  }

  public int resizecols(int input) {
    colCount = input;
    matrix = new Cell[rowCount][colCount];
    return colCount;
  }

  public int resizewidth(int input) {
    cellWidth = input;
    return cellWidth;
  }

  public void inputcell() {

  }

  public void operation() {

  }

  private String processReadOneField(String command) {
    if (command.startsWith("value")){

      command = command.substring(6);
    }
    if (command.startsWith("display")){

      command = command.substring(8);
    }
      char location[] = command.toCharArray();
      String outer = "";
      String localResult = "";
      String alpha = String.valueOf(location[0]);
      int colindex = Integer.parseInt(String.valueOf(location[0] - 'a'));
      int rowIndex = Integer.parseInt(String.valueOf(location[1])) - 1; 
      int temper = 0;
      double temp1 = 0;
      String accessalpha = "";

      if (matrix[rowIndex][colindex] != null) {

        outer = matrix[rowIndex][colindex].getExpression();
        // if there is a string in the matrix
        if (outer.matches("[a-zA-Z\\s].*")){
            localResult  += "\"" + outer + "\""; 

        } else{   // if its a number in the matrix  
          //outputStream.println("DEBUG is:" + outer);
          temp1 =  Double.valueOf(outer);
          temper = (int) temp1;
          localResult += String.valueOf(temper);
        }
      }
      return localResult;
  }

  /*
   * This method processes a user command.
   * 
   * Checkpoint #1 commands are: print : render a text based version of the matrix
   * width = [value] : set the cell width width : get the cell width rows =
   * [value] : set the row count cols = [value] : set the column count rows : get
   * the row count cols : get the column count
   *
   * Checkpoint #2 commands are: [cell] = [expression] : set the cell's
   * expression, for checkpoint # expressions may be... - a value such as 5.
   * Example: a2 = 5 - a string such as "hello". Example: a3 = "hello" [cell] :
   * get the cell's expression, NOT the cell's value value [cell] : get the cell
   * value expr [cell] : get the cell's expression, NOT the cell's value display
   * [cell] : get the string for how the cell wants to display itself clear :
   * empty out the entire matrix save [file] : saves to a file all the commands
   * necessary to regenerate the grid's contents
   *
   * Checkpoint #3 commands are: [cell] = [expression] : where the expression is a
   * complicated formula. Example: a1 = ( 3.141 * b3 + b1 - c2 / 4 )
   *
   * Final commands are: [cell] = [expression] : where the expression may contain
   * a single function, sum or avg: Example: a1 = ( sum a1 - a3 ) Example: b1 = (
   * avg a1 - d1 ) clear [cell] : empty out a single cell. Example: clear a1 sorta
   * [range] : sort the range in ascending order. Example: sorta a1 - a5 sortd
   * [range] : sort the range in descending order. Example: sortd b1 - e1
   * 
   *
   * 
   * Parameters: command : The command to be processed. Returns : The results of
   * the command as a string to be printed by the infrastructure.
   */
  public String processCommand(String command) {
    String result = ""; // this was originally null and hence += would fail
    if (command.toLowerCase().matches("[a-z][0-9]+ = \\( .* \\)")){
      String oper = "";
      String op1 = "";
      int tempprevnum = 0;
      double prevnum = 0.0;
      double nextnum = 0;
      double total = 0.0;
      Scanner splitter = new Scanner(command);
      while(splitter.hasNext()){
        oper = splitter.next();
        //outputStream.println(oper);
        if (oper.matches("/^[0-9]*[.]?[0-9]+$/")){
          tempprevnum = Integer.valueOf(oper);
          prevnum = (double) tempprevnum;

          outputStream.println(prevnum);
        }
       
        if (!oper.matches("[A-Z][a-z]") && !oper.matches("[0-9]") && !oper.matches("[a-z][0-9]") && !oper.matches("[(]") && !oper.matches("[)]") && !oper.matches("[=]")){
          op1 = oper;
         // int tempnext = splitter.nextInt;
        //  nextnum = Integer.valueOf(tempnext);
          while (splitter.hasNextDouble()){
            nextnum = splitter.nextDouble();
          }
          
          
          total = operator(prevnum, nextnum, op1);
          
         // outputStream.println(total);

          
          //break;
          
        } 
        
       
        
      
     // while(splitter.hasNext()){
        //oper = splitter.next();
      /*  if (splitter.next().equals("+") ||  splitter.next().equals("-") ||  splitter.next().equals("*") ||  splitter.next().equals("/")){
          oper = splitter.next();
          outputStream.println(oper);
        } else{
          splitter.next();
        } */
        
        
      }
     
     // Scanner operations = new Scanner(command);
      char location[] = command.toCharArray();

      String alpha = String.valueOf(location[0]);
      int colindex = Integer.parseInt(String.valueOf(location[0] - 'a'));
      // outputStream.print(colindex);
      int rowIndex = Integer.parseInt(String.valueOf(location[1])) - 1;
      // outputStream.println("" + alpha + " " + rowIndex);

      // if next token is an int
        double numin = total;
        // outputStream.println("I AM assigned " + numin);
        NumberCell newnumcell = new NumberCell();
        newnumcell.setExpression(Double.toString(numin));
        matrix[rowIndex][colindex] = newnumcell;
        result += Double.toString(numin);
    
    } // added check for brackets right here so it works this was changed
    else if (command.equalsIgnoreCase("print")) {  // added else to the if so it works this was changed
      result = this.print();
    } else if (command.equalsIgnoreCase("rows")) {
      result = Integer.toString(this.rows());
    } else if (command.equalsIgnoreCase("cols")) {
      result = Integer.toString(this.cols());

    } else if (command.equalsIgnoreCase("width")) {
      result = Integer.toString(this.width());
    } else if (command.toLowerCase().startsWith("rows =")) {
      Scanner rower = new Scanner(command);
      int input = 0;
      while (rower.hasNext()) {

        if (rower.hasNextInt()) {
          input = rower.nextInt();
        } else {
          rower.next();
        }
      }
      result = Integer.toString(this.resizerows(input));

    } else if (command.toLowerCase().startsWith("cols =")) {
      Scanner coller = new Scanner(command);
      int input = 0;
      while (coller.hasNext()) {
        if (coller.hasNextInt()) {
          input = coller.nextInt();
        } else {
          coller.next();
        }
      }

      result = Integer.toString(this.resizecols(input));

    } else if (command.toLowerCase().startsWith("width =")) {
      Scanner widther = new Scanner(command);
      int input = 0;
      while (widther.hasNext()) {
        if (widther.hasNextInt()) {
          input = widther.nextInt();
        } else {
          widther.next();
        }
      }

      result = Integer.toString(this.resizewidth(input));
    } else if (command.toLowerCase().matches("[a-z][0-9] = .*") /* && !command.toLowerCase().contains("\\(") */) {
      //outputStream.println("you did it"); to test
      char location[] = command.toCharArray();

      String alpha = String.valueOf(location[0]);
      int colindex = Integer.parseInt(String.valueOf(location[0] - 'a'));
      // outputStream.print(colindex);
      int rowIndex = Integer.parseInt(String.valueOf(location[1])) - 1;
      // outputStream.println("" + alpha + " " + rowIndex);

      Scanner cellinput = new Scanner(command);
      cellinput.next(); // skip first token
      cellinput.next(); // skip second token (=)
      if (cellinput.hasNextInt()) { // if next token is an int
        double numin = cellinput.nextInt();
        // outputStream.println("I AM assigned " + numin);
        NumberCell newnumcell = new NumberCell();
        newnumcell.setExpression(Double.toString(numin));
        matrix[rowIndex][colindex] = newnumcell;
        result += Double.toString(numin);

      } else {
        String whole = command.substring(5);

        if (whole.startsWith("\"")) {
          whole = whole.substring(1, whole.length() - 1);
        }

        TextCell newCell = new TextCell();
        newCell.setExpression(whole);
        matrix[rowIndex][colindex] = newCell;
        result += whole;
      } // end else when input is not a number but string

    } else if (command.toLowerCase().matches("[a-z][0-9]")){

      result += processReadOneField(command);
    }
    // if asked for value at
      else if (command.toLowerCase().matches("value [a-z][0-9]")){
        String takein = processReadOneField(command);
        int fornow = Integer.parseInt(takein);
        double last = (double) fornow;
        result += last;
    }
    else if (command.toLowerCase().matches("display [a-z][0-9]")){
        String takein = processReadOneField(command);
        takein = takein.substring(1, takein.length() - 1);

        result += takein;
    } /* else if (command.toLowerCase().matches("[a-z][0-9]+ = \\( .* \\)")){
     // System.out.println("it works");
    } */

    

    return result;
  }
    public static double operator(double num1, double num2, String op){
      double result = 0.0;
      if (op.equals("+")){
        result = num1 + num2;
      } else if (op.equals("-")){
        result = num1 - num2;
      } else if (op.equals("*")){
        result = num1 * num2;
      } else if (op.equals("/")){
        result = num1 / num2;
      }
      return result;
      
    }

  /**
   * saveToFile
   *
   * This method will process the command: "save {filename}"
   * <p>
   * Ask the matrix for all formulas for all non-empty cells. Empty cells should
   * not be saved.
   *
   * Save all properties such as grid size and cell width (if available)
   * 
   * @param filename is the name of the file to save
   * @return A message to user about the success/failure of saving to a file.
   */
  private String saveToFile(String filename) {
    File file = new File(filename);
    String result = "Saved to file: " + file.getAbsolutePath();

    try {
      // Get the writer ready
      PrintStream writer = new PrintStream(file);
      saveGrid(writer);
    } catch (FileNotFoundException e) {
      result = "Cannot write to the file: " + file.getAbsolutePath();
    }

    return result;
  }

  /**
   * saveGrid will save the gride to a file.
   *
   * Ask the matrix for all formulas for all non-empty cells. Empty cells should
   * not be saved.
   *
   * Save all properties such as grid size and cell width (if available)
   * 
   * @param writer is the PrintStream to print to
   */
  public void saveGrid(PrintStream writer) {
    // save the rows, cols and width
    writer.println("rows = " + rowCount);
    writer.println("cols = " + colCount);
    writer.println("width = " + cellWidth);

    // save the grid formulas, for every cell that is not empty
    for (int row = 0; row < rowCount; row++) {
      for (int col = 0; col < colCount; col++) {
        String formula = matrix[row][col].getExpression();
        if (formula != null && formula.length() > 0) {
          writer.println("" + (char) ('A' + col) + (row + 1) + " = " + formula);
        }
      }
    }
  }

}
