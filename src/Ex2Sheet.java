import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Ex2Sheet implements Sheet {
    private Cell[][] table;

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

    @Override
    public boolean isIn(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];

        return ans;
    }
    @Override
    public void load(String fileName) throws IOException {
        List<String> loadStr = Files.readAllLines(Paths.get(fileName));

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                table[x][y].setData("");
            }
        }

        for (int index = 1; index < loadStr.size(); index++) {
            String[] parsedData = loadStr.get(index).split(",");
            int xCoord = Integer.parseInt(parsedData[0]);
            int yCoord = Integer.parseInt(parsedData[1]);

            table[xCoord][yCoord].setData(parsedData[2]);
        }

    }

    @Override
    public void save(String fileName) throws IOException {
        StringBuilder first_line = new StringBuilder("I2CS ArielU: SpreadSheet (Ex2) assignment");

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                SCell cell = (SCell)table[x][y];
                String line = cell.getData();
                if (!Objects.equals(line, "")) {
                    first_line.append(x).append(",").append(y).append(",").append(line).append("\n");
                }
            }
        }

        FileWriter myWriter = new FileWriter(fileName);
        myWriter.write(first_line.toString());
        myWriter.close();
    }

    @Override
    public String eval(int x, int y) {
        Cell cell = table[x][y];
        String line = cell.getData();

        String computable =computeForm(line, List.of(new PositionFinder(x, y)));

        if (Objects.equals(computable, Ex2Utils.ERR_FORM)) cell.setType(Ex2Utils.ERR_FORM_FORMAT);
        else if (Objects.equals(computable, Ex2Utils.ERR_CYCLE)) cell.setType(Ex2Utils.ERR_CYCLE_FORM);
        else if (parseDouble(computable) != -1 && !line.startsWith("=")) cell.setType(Ex2Utils.NUMBER);
        else if (parseDouble(computable) != -1) cell.setType(Ex2Utils.FORM);
        else cell.setType(Ex2Utils.TEXT);

        return computable;
    }

    String computeForm(String line, List<PositionFinder> positions) {
        if (line.startsWith("=")) {
            String str = line.substring(1).replaceAll(" ", "");

            return computeFormHelper  (str, positions);
        }

        double num = parseDouble(line);
        if (num != -1) {
            return String.valueOf(num);
        }

        return line;
    }

    String computeFormHelper(String form, List<PositionFinder> positions) {
        while (form.startsWith("(") && form.endsWith(")"))
            form = form.substring(1, form.length() - 1);

        int signIndex = indOfMainOp(form);

        if (signIndex == -1) {
            double result = parseDouble(form);
            if (result != -1) {
                return String.valueOf(result);
            }

            PositionFinder position = interpretPos(form);
            if (position.x != -1) {
                if (containsCoordinate(positions, position))
                    return Ex2Utils.ERR_CYCLE;

                if (!isIn(position.x, position.y))
                    return Ex2Utils.ERR_FORM;

                Cell cell = table[position.x][position.y];
                String cellExpr = cell.getData();

                List<PositionFinder> positionsCopy = new ArrayList<PositionFinder>(positions);
                positionsCopy.add(position);

                return computeForm(cellExpr, positionsCopy);
            }

            return Ex2Utils.ERR_FORM;
        }

        String leftPart = form.substring(0, signIndex);
        String rightPart = form.substring(signIndex + 1);
        char op = form.charAt(signIndex);

        if (leftPart != "") {
            String leftStr = computeFormHelper(leftPart, positions);
            String rightStr = computeFormHelper(rightPart, positions);

            if (Objects.equals(leftStr, Ex2Utils.ERR_FORM) || Objects.equals(rightStr, Ex2Utils.ERR_FORM))
                return Ex2Utils.ERR_FORM;

            if (Objects.equals(leftStr, Ex2Utils.ERR_CYCLE) || Objects.equals(rightStr, Ex2Utils.ERR_CYCLE))
                return Ex2Utils.ERR_CYCLE;

            double leftNum = Double.parseDouble(leftStr);
            double rightNum = Double.parseDouble(rightStr);

            double result = performOperation(leftNum, rightNum, op);
            return String.valueOf(result);
        }

        if (op == '+' || op == '-') {
            String num1Str = computeFormHelper(rightPart, positions);

            if (parseDouble(num1Str) == -1)
                return num1Str;

            double num1 = parseDouble(num1Str);

            double res = applyUnOp(num1, op);
            return String.valueOf(res);
        }

        return Ex2Utils.ERR_FORM;
    }

   public int indOfMainOp(String str) {
        boolean found = false;

        int barDepth = Integer.MAX_VALUE;
        int opVal = Integer.MAX_VALUE;
        int opIndex = Integer.MAX_VALUE;

        int corDepth = 0;
        for (int ic = 0; ic < str.length(); ic++) {
            char ch = str.charAt(ic);

            if (ch == '(') {
                corDepth++;
                continue;
            }
            if (ch == ')') {
                corDepth--;
                continue;
            }

            int oc = signType(ch);
            if (oc == -1)
                continue;

            if (isOp(ch) && isOpBetter(barDepth, opVal, opIndex, corDepth, oc, ic)) {
                found = true;
                barDepth = corDepth;
                opVal = oc;
                opIndex = ic;
            }
        }

        if (!found)
            return -1;

        return opIndex;

    }

    boolean isOp(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    boolean isOpBetter(int DD, int OD, int OB, int DC, int CC, int BD) {
        return DD > DC || DD == DC && (OD > CC || OD == CC && OB > BD);
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

    public PositionFinder interpretPos(String s) {
        if (s.isEmpty()) {
            return new PositionFinder(-1, -1);
        }

        char columnChar = s.charAt(0);
        int column = -1;
        if (Character.isUpperCase(columnChar)) {
            column = columnChar - 'A';
        } else if (Character.isLowerCase(columnChar)) {
            column = columnChar - 'a';
        }

        if (column == -1) {
            return new PositionFinder(-1, -1);
        }

        int row = parseInt(s.substring(1));
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

    double applyUnOp(double value, char operator) {
        switch (operator) {
            case '+':
                return value;
            case '-':
                return -value;
            default:
                return Double.NEGATIVE_INFINITY;
        }
    }
}
