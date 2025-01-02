// Add your documentation below:

public class SCell implements Cell {
    private String line;
    private int type;
    // Add your code here
    public  boolean isForm(String text) {
        if (text == null || text.isEmpty() || !text.startsWith("=")) return false;
        String form = text.substring(1).trim();

        if(isNumber(form) || isValCell(form)) return true;
        //if (isNumber(form) ) return true;
        //if(isValCell(form)) return true;

        if (!parBalance(form)) return false;

        try {
            return parseForm(form);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    public boolean parseForm(String form){
        form = form.trim();

        //*
        if(isNumber(form) || isValCell(form)) return true;
        // if(isNumber(form)) return true;
        //if(isValCell(form)) return true;

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
    public int indOfMainOp(String text){
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
    public int getOpPr(char c){
        switch (c){
            case '+': return 1;
            case '-': return 1;
            case '*': return 2;
            case '/': return 2;
            default: return Integer.MAX_VALUE;
        }
    }
    public boolean isValCell(String text){
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
    private boolean parBalance(String text){
        int balance = 0;
        for(char c: text.toCharArray()){
            if(c == '('){
                balance++;
            } else if(c == ')'){
                balance--;
                if(balance < 0) return false;
            }
        }

        return balance == 0;
    }
    public boolean isNumber(String text) {
        try {
            Double.parseDouble(text);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private boolean isMatchPar(String text, int start, int end){
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
    public boolean isText(String text) {
        if(text == null || text.isEmpty()) return false;
        if(text.startsWith("=")) return false;
        if(isNumber(text)) return false;
        return true;
    }
    public double computeForm(String form) {
        if (!isForm(form)) {
            throw new IllegalArgumentException("INVALID FORM");
        }

        String finalForm = form.substring(1).trim();

        if (isNumber(finalForm)) return Double.parseDouble(finalForm);
        if (isValCell(finalForm)) {
            throw new IllegalArgumentException("CELL REFERENCE ERR");
        }
        if (finalForm.startsWith("(") && finalForm.endsWith(")")) {
            if (isMatchPar(finalForm, 0, finalForm.length() - 1)) {
                return computeForm("=" + finalForm.substring(1, finalForm.length() - 1));
            }
        }

        int index = indOfMainOp(finalForm);
        if (index != -1) {
            String leftSide = finalForm.substring(0, index).trim();
            String rightSide = finalForm.substring(index + 1).trim();
            char mainOp = finalForm.charAt(index);
            return calculator(computeForm("=" + leftSide), computeForm("=" + rightSide), mainOp);
        }
        throw new IllegalArgumentException("INVALID FORM");
    }
    public double calculator(double leftSide, double rightSide, char mainOp){
        switch (mainOp){
            case '+': return leftSide + rightSide;
            case '-': return leftSide - rightSide;
            case '*': return leftSide * rightSide;
            case '/':
                if(rightSide == 0) throw new ArithmeticException("INVALID DIVIDING");
                return leftSide/rightSide;
            default: throw new IllegalArgumentException("INVALID OPERATOR");
        }
    }
    private boolean isFullyParenthesized(String form) {
        if (form.charAt(0) != '(' || form.charAt(form.length() - 1) != ')') {
            return false;
        }

        int balance = 0;
        for (int i = 0; i < form.length(); i++) {
            if (form.charAt(i) == '(') balance++;
            if (form.charAt(i) == ')') balance--;
            if (balance <= 0 && i != form.length() - 1) return false;
        }
        return balance == 0;
    }

    public SCell(String s) {
        // Add your code here
        setData(s);
    }

    @Override
    public int getOrder() {
        // Add your code here

        return 0;
        // ///////////////////
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
        // Add your code here
        line = s;
        /////////////////////
    }
    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here

    }
}
