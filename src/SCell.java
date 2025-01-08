import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class SCell implements Cell {
    private String data;
    private int type;
    private int order;
    public static boolean isForm(String text) {
        if (text.isEmpty()) return false;
        if (!text.startsWith("=")) return false;
        text = text.substring(1);
        int balance = 0;
        boolean validCharFound = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') balance++;
            else if (c == ')') balance--;
            if (balance < 0) return false;
            if (Character.isDigit(c) || c == '.' || isOperator(c) || (c >= 'A' && c <= 'Z')) {
                validCharFound = true;
            } else {
                return false;
            }
        }
        return balance == 0 && validCharFound;
    }
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    private static boolean isValidChar(char c){
        return Character.isDigit(c) || isOperator(c) || c == '.' || c == '(' || c == ')'
                || (c >= 'A' && c <= 'Z') || c == '-';
    }

    public static boolean parseForm(String form){
        form = form.trim();
        if(isNumber(form) || isValCell(form)) return true;
        if(form.startsWith("(") && form.endsWith(")")){
            if(isMatchPar(form,0,form.length()-1)){
                return parseForm(form.substring(1,form.length()-1));
            }
        }
        int index = indOfMainOp(form);
        if (index != -1) {
            String leftSide = form.substring(0,index).trim();
            String rightSide = form.substring(index+1).trim();
            return parseForm(leftSide) && parseForm(rightSide);
        }
        return false;
    }
    public static int indOfMainOp(String text){
        int index = -1;
        int level = 0;
        int lowP = Integer.MAX_VALUE;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') {
                level++;
            } else if (c == ')') {
                level--;
            } else if (level == 0) {
                int priority = getOpPr(c);
                if (priority <= lowP) {
                    lowP = priority;
                    index = i;
                }
            }
        }
        return index;
    }
    public static int getOpPr(char c){
        switch (c){
            case '+': return 1;
            case '-': return 1;
            case '*': return 2;
            case '/': return 2;
            default: return Integer.MAX_VALUE;
        }
    }
    public static boolean isValCell(String text){
        if(text == null || text.length() < 2) return false;

        char column = Character.toUpperCase(text.charAt(0));
        if (column < 'A' || column > 'Z') return false;

        try {
            int row = Integer.parseInt(text.substring(1));
            return row >= 0 && row < 100;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isNumber(String text) {
        try {
            Double.parseDouble(text);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private static boolean isMatchPar(String text, int start, int end){
        int depthLevel = 0;

        for (int i = start; i <= end; i++) {
            if(text.charAt(i) == '('){
                depthLevel++;
            }else if(text.charAt(i) == ')'){
                depthLevel--;
                if(depthLevel == 0 && i != end) return false;
            }
        }
        return depthLevel == 0;
    }
    public static boolean isText(String text) {
        return !isNumber(text) && isForm(text);
    }
    public static double computeForm(String form, Cell[][] table) {
        form = form.trim();
        if (form.isEmpty()) {
            throw new IllegalArgumentException("Invalid formula: " + form);
        }
        return evaluate(form, table);
    }

    private static double evaluate(String expression, Cell[][] table) {
        expression = expression.replace(" ", "");
        return evalHelper(expression, table);
    }

    private static double evalHelper(String expression, Cell[][] table) {
        if (expression.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression: " + expression);
        }

        int balance = 0;
        int opIndex = -1;
        char op = ' ';
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (c == ')') {
                balance++;
            } else if (c == '(') {
                balance--;
            } else if (balance == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                opIndex = i;
                op = c;
                break;
            }
        }

        if (opIndex == -1) {
            if (expression.startsWith("(") && expression.endsWith(")")) {
                return evalHelper(expression.substring(1, expression.length() - 1), table);
            }
            try {
                return Double.parseDouble(expression);
            } catch (NumberFormatException e) {
                if (isValCell(expression)) {
                    int col = Character.toUpperCase(expression.charAt(0)) - 'A';
                    int row = Integer.parseInt(expression.substring(1));
                    return Double.parseDouble(table[col][row].getData());
                }
                throw new IllegalArgumentException("Invalid number in expression: " + expression);
            }
        }

        double left = evalHelper(expression.substring(0, opIndex), table);
        double right = evalHelper(expression.substring(opIndex + 1), table);

        switch (op) {
            case '+':
                return left + right;
            case '-':
                return left - right;
            case '*':
                return left * right;
            case '/':
                if (Math.abs(right) < Ex2Utils.EPS1) {
                    throw new ArithmeticException("Division by zero in formula");
                }
                return left / right;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }


    public static boolean containsOperator(String text){
        return text.contains("+") || text.contains("-") || text.contains("*") || text.contains("/");
    }
    private static double calculate(double leftValue, double rightValue, char operator) {
        switch (operator) {
            case '+':
                return leftValue + rightValue;
            case '-':
                return leftValue - rightValue;
            case '*':
                return leftValue * rightValue;
            case '/':
                if (Math.abs(rightValue) < Ex2Utils.EPS1) {
                    throw new ArithmeticException("Division by zero in formula");
                }
                return leftValue / rightValue;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operator);
        }
    }

public SCell(String data) {
    this.data = data.trim();
    this.type = Ex2Utils.TEXT;

    if (data.startsWith("=")) {
        if (isForm(data)) {
            this.type = Ex2Utils.FORM;
        } else {
            this.type = Ex2Utils.ERR;
        }
    } else {
        try {
            Double.parseDouble(data);
            this.type = Ex2Utils.NUMBER;
        } catch (NumberFormatException e) {
        }
    }
}


    @Override
    public int getOrder() {
       if(type == Ex2Utils.TEXT || type == Ex2Utils.NUMBER) {
           return 0;
       }

       if(type == Ex2Utils.ERR || type == Ex2Utils.ERR_CYCLE_FORM) {
           return -1;
       }

       if(type != Ex2Utils.FORM){
           return 0;
       }

       String form = data.substring(1);
        List<String> dependencies = findCellReferences(form);
        if(dependencies.isEmpty()) {
            return -1;
        }

        return order;
    }


    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String s) {
        if (s == null) {
            this.data = "";
        } else {
            this.data = s.trim();
        }
        determineType();
    }

    @Override
    public String getData() {
        return this.data;
    }


    @Override
    public int getType() {
        if(type == Ex2Utils.TEXT) {
            return 1;
        } else if(type == Ex2Utils.NUMBER){
            return 2;
        } else if (type == Ex2Utils.ERR || type == Ex2Utils.ERR_CYCLE_FORM) {
            return -1;
        } else if (type == Ex2Utils.FORM) {
            return 3;
        }else if(type == Ex2Utils.ERR_FORM_FORMAT){
            return -2;
        }
        return 0;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public boolean setOrder(int t) {
        if (t < 1) return false;
        order = t;
        return true;
    }
    private void determineType(){
        if (data == null || data.isEmpty()) {
            setType(Ex2Utils.TEXT);
            return;
        }

        if (data.startsWith("=")) {
            String form = data.substring(1).trim(); // מתעלמים מה "=" בהתחלה
            if (isForm(form)) {
                setType(Ex2Utils.FORM);
            } else {
                setType(Ex2Utils.ERR); // פורמולה לא תקינה
            }
            return;
        }

        try {
            Double.parseDouble(data);
            setType(Ex2Utils.NUMBER);
        } catch (NumberFormatException e) {
            setType(Ex2Utils.TEXT);
        }
    }

    private List<String> findCellReferences(String form) {
        List<String> references = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Za-z][0-9]+");
        Matcher matcher = pattern.matcher(form);

        while (matcher.find()) {
            references.add(matcher.group());
        }
        return references;
    }

}



