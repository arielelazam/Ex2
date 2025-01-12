import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Ex2Sheet implements Sheet {
    private Cell[][] table;

    // Inner class PositionFinder
    private static class PositionFinder {
        public int x;
        public int y;

        public PositionFinder(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i = i + 1) {
            for (int j = 0; j < y; j = j + 1) {
                table[i][j] = new SCell("");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        return eval(x, y);
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String posses) {
        PositionFinder pos = interpretPos(posses);
        return table[pos.x][pos.y];
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        table[x][y] = c;
    }

    @Override
    public void eval() {
    }

    public boolean isIn(int x, int y) {
        if (x < 0) {
            return false;
        }
        if (x >= width()) {
            return false;
        }
        if (y < 0) {
            return false;
        }
        if (y >= height()) {
            return false;
        }
        return true;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        List<String> loadStr = readFile(fileName);
        clearTableData();
        loadTableData(loadStr);
    }

    @Override
    public void save(String fileName) throws IOException {
        StringBuilder content = generateContent();
        writeToFile(fileName, content);
    }

    private StringBuilder generateContent() {
        StringBuilder content = new StringBuilder();

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                SCell cell = (SCell)table[x][y];
                String line = cell.getData();
                if (!Objects.equals(line, "")) {
                    content.append(x).append(",").append(y).append(",").append(line).append("\n");
                }
            }
        }

        return content;
    }

    private void writeToFile(String fileName, StringBuilder content) throws IOException {
        FileWriter myWriter = new FileWriter(fileName);
        myWriter.write(content.toString());
        myWriter.close();
    }

    @Override
    public String eval(int x, int y) {
        Cell cell = table[x][y];
        String line = cell.getData();

        String computable = computeForm(line, List.of(new PositionFinder(x, y)));

        setTypeBasedOnComputable(cell, computable, line);

        return computable;
    }

    private void setTypeBasedOnComputable(Cell cell, String computable, String line) {
        if (Objects.equals(computable, Ex2Utils.ERR_FORM)) {
            cell.setType(Ex2Utils.ERR_FORM_FORMAT);
        } else if (Objects.equals(computable, Ex2Utils.ERR_CYCLE)) {
            cell.setType(Ex2Utils.ERR_CYCLE_FORM);
        } else if (parseDouble(computable) != -1 && !line.startsWith("=")) {
            cell.setType(Ex2Utils.NUMBER);
        } else if (parseDouble(computable) != -1) {
            cell.setType(Ex2Utils.FORM);
        } else {
            cell.setType(Ex2Utils.TEXT);
        }
    }


    String computeForm(String form, List<PositionFinder> positions) {
        if (form.startsWith("=")) {
            String cleanedForm = removeEqualsAndWhitespace(form);
            return evaluateExpression(cleanedForm, positions);
        }

        double num = tryParseDouble(form);
        if (num != -1) {
            return String.valueOf(num);
        }

        return form;
    }

    private String removeEqualsAndWhitespace(String form) {
        return form.substring(1).replaceAll(" ", "");
    }

    private String evaluateExpression(String form, List<PositionFinder> positions) {
        form = stripOuterParentheses(form);

        int mainOpIndex = findMainOperatorIndex(form);

        if (mainOpIndex == -1) {
            return evaluateSimpleExpression(form, positions);
        }

        return evaluateComplexExpression(form, positions, mainOpIndex);
    }

    private String stripOuterParentheses(String form) {
        while (form.startsWith("(") && form.endsWith(")")) {
            form = form.substring(1, form.length() - 1);
        }
        return form;
    }

    private int findMainOperatorIndex(String form) {
        return indOfMainOp(form);
    }

    private String evaluateSimpleExpression(String form, List<PositionFinder> positions) {
        double result = tryParseDouble(form);
        if (result != -1) {
            return String.valueOf(result);
        }

        PositionFinder pos = interpretPosition(form);
        if (pos.x != -1) {
            return evaluateCellExpression(form, positions, pos);
        }

        return Ex2Utils.ERR_FORM;
    }

    private String evaluateCellExpression(String form, List<PositionFinder> positions, PositionFinder pos) {
        if (containsCoordinate(positions, pos)) {
            return Ex2Utils.ERR_CYCLE;
        }

        if (!isIn(pos.x, pos.y)) {
            return Ex2Utils.ERR_FORM;
        }

        Cell cell = table[pos.x][pos.y];
        String cellExpr = cell.getData();

        List<PositionFinder> positionsCopy = new ArrayList<>(positions);
        positionsCopy.add(pos);

        return computeForm(cellExpr, positionsCopy);
    }

    private String evaluateComplexExpression(String form, List<PositionFinder> positions, int mainOpIndex) {
        String leftPart = form.substring(0, mainOpIndex);
        String rightPart = form.substring(mainOpIndex + 1);
        char operator = form.charAt(mainOpIndex);

        if (!leftPart.isEmpty()) {
            return evaluateBinaryExpression(leftPart, rightPart, operator, positions);
        }

        if (operator == '+' || operator == '-') {
            return evaluateUnaryExpression(rightPart, operator, positions);
        }

        return Ex2Utils.ERR_FORM;
    }

    private String evaluateBinaryExpression(String leftPart, String rightPart, char operator, List<PositionFinder> positions) {
        String leftResult = evaluateExpression(leftPart, positions);
        String rightResult = evaluateExpression(rightPart, positions);

        if (Objects.equals(leftResult, Ex2Utils.ERR_FORM) || Objects.equals(rightResult, Ex2Utils.ERR_FORM)) {
            return Ex2Utils.ERR_FORM;
        }

        if (Objects.equals(leftResult, Ex2Utils.ERR_CYCLE) || Objects.equals(rightResult, Ex2Utils.ERR_CYCLE)) {
            return Ex2Utils.ERR_CYCLE;
        }

        double leftNum = Double.parseDouble(leftResult);
        double rightNum = Double.parseDouble(rightResult);

        double result = performOperation(leftNum, rightNum, operator);
        return String.valueOf(result);
    }

    private String evaluateUnaryExpression(String rightPart, char operator, List<PositionFinder> positions) {
        String resultStr = evaluateExpression(rightPart, positions);

        if (tryParseDouble(resultStr) == -1) {
            return resultStr;
        }

        double num = tryParseDouble(resultStr);
        double result = applyUnaryOperator(num, operator);
        return String.valueOf(result);
    }

    private double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private PositionFinder interpretPosition(String form) {
        return interpretPos(form);
    }

    private double applyUnaryOperator(double num, char operator) {
        return applyUnOp(num, operator);
    }

    public int indOfMainOp(String str) {
        boolean found = false;

        int barDepth = Integer.MAX_VALUE;
        int opVal = Integer.MAX_VALUE;
        int opIndex = Integer.MAX_VALUE;

        int corDepth = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch == '(') {
                corDepth++;
                continue;
            }
            if (ch == ')') {
                corDepth--;
                continue;
            }

            int oc = signType(ch);
            if (oc == -1) continue;

            if (isOp(ch) && isPreferredCandidate(barDepth, opVal, opIndex, corDepth, oc, i)) {
                found = true;
                barDepth = corDepth;
                opVal = oc;
                opIndex = i;
            }
        }

        if (!found) return -1;

        return opIndex;
    }

    boolean isOp(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    boolean isPreferredCandidate(int domA, int compA, int bonusA, int domB, int compB, int bonusB) {
        return domA > domB ||
                (domA == domB &&
                        (compA > compB ||
                                (compA == compB &&
                                        bonusA > bonusB)));
    }



    public int signType(char op) {
        switch (op) {
            case '+':
                return 0;
            case '-':
                return 0;
            case '*':
                return 1;
            case '/':
                return 1;
            default:
                return -1;
        }
    }

    public PositionFinder interpretPos(String str) {
        if (str.isEmpty()) {
            return new PositionFinder(-1, -1);
        }

        char columnChar = str.charAt(0);
        int column = -1;
        if (Character.isUpperCase(columnChar)) {
            column = columnChar - 'A';
        } else if (Character.isLowerCase(columnChar)) {
            column = columnChar - 'a';
        }

        if (column == -1) {
            return new PositionFinder(-1, -1);
        }

        int row = parseInt(str.substring(1));
        if (row == -1) {
            return new PositionFinder(-1, -1);
        }

        return new PositionFinder(column, row);
    }

    double performOperation(double num1, double num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            default:
                return Double.NEGATIVE_INFINITY;
        }
    }

    double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return -1;
        }
    }

    public int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    boolean containsCoordinate(List<PositionFinder> coordinates, PositionFinder coordinate) {
        for (PositionFinder currentCoord : coordinates) {
            if (coordinate.x == currentCoord.x && coordinate.y == currentCoord.y) {
                return true;
            }
        }
        return false;
    }

    double applyUnOp(double val, char op) {
        switch (op) {
            case '+':
                return val;
            case '-':
                return -val;
            default:
                return Double.NEGATIVE_INFINITY;
        }
    }

    private List<String> readFile(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName));
    }

    private void clearTableData() {
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                table[x][y].setData("");
            }
        }
    }

    private void loadTableData(List<String> loadStr) {
        for (int index = 1; index < loadStr.size(); index++) {
            String[] parsedData = loadStr.get(index).split(",");
            int xCoord = Integer.parseInt(parsedData[0]);
            int yCoord = Integer.parseInt(parsedData[1]);

            table[xCoord][yCoord].setData(parsedData[2]);
        }
    }
}