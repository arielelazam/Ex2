import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ex2Sheet implements Sheet {
    private Cell[][] table;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                table[i][j] = new SCell("");
            }
        }
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        if (!isIn(x, y)) {
            return Ex2Utils.EMPTY_CELL;
        }
        Cell c = get(x, y);
        if (c == null) {
            return Ex2Utils.EMPTY_CELL;
        }
        return eval(x, y);
    }

    @Override
    public Cell get(int x, int y) {
        if (isIn(x, y)) {
            return table[x][y];
        } else {
            return null;
        }
    }

    @Override
    public Cell get(String cords) {
        char colChar = cords.charAt(0);
        String rowPart = cords.substring(1);
        int x = Character.toUpperCase(colChar) - 'A';
        int y = Integer.parseInt(rowPart);
        return table[x][y];
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
        if (!isIn(x, y)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        if (s == null) {
            s = Ex2Utils.EMPTY_CELL;
        }
        table[x][y] = new SCell(s);
    }

    @Override
    public void eval() {
        int[][] dd = depth();
    }

    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && yy >= 0 && xx < width() && yy < height();
    }

    @Override
    public int[][] depth() {
        int[][] depths = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                depths[i][j] = calculateCellDepth(i, j, new HashSet<>());
            }
        }
        return depths;
    }

    private int calculateCellDepth(int x, int y, Set<String> visited) {
        if (!isIn(x, y)) {
            return 0;
        }

        Cell cell = get(x, y);
        if (cell == null || cell.getData() == null || cell.getData().isEmpty()) {
            return 0;
        }

        String data = cell.getData();

        if (!data.startsWith("=")) {
            return 0;
        }

        String cellId = x + "," + y;

        if (visited.contains(cellId)) {
            return -1;
        }
        visited.add(cellId);

        Pattern pattern = Pattern.compile("[A-Za-z][0-9]+");
        Matcher matcher = pattern.matcher(data.substring(1));

        int maxDepth = 0;
        boolean hasCircular = false;

        while (matcher.find()) {
            String cellRef = matcher.group();

            int col = Character.toUpperCase(cellRef.charAt(0)) - 'A';
            int row = Integer.parseInt(cellRef.substring(1));
            int refDepth = calculateCellDepth(col, row, visited);
            if (refDepth == -1) {
                hasCircular = true;
            } else {
                maxDepth = Math.max(maxDepth, refDepth);
            }
        }
        visited.remove(cellId);
        if (hasCircular) {
            return -1;
        }
        return maxDepth + 1;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here
    }

    @Override
    public String eval(int x, int y) {
        Cell cell = get(x, y);
        if (cell == null) {
            return Ex2Utils.EMPTY_CELL;
        }

        String data = cell.getData();
        if (data == null || data.isEmpty()) {
            return Ex2Utils.EMPTY_CELL;
        }

        if (data.startsWith("=")) {
            try {
                String form = data.substring(1);

                Set<String> visited = new HashSet<>();
                int depth = calculateCellDepth(x, y, visited);
                if (depth == -1) {
                    return Ex2Utils.ERR_CYCLE;
                }

                double result = SCell.computeForm(form, table);
                return String.format("%.1f", result);
            } catch (Exception e) {
                return Ex2Utils.ERR_FORM;
            }
        }

        try {
            double number = Double.parseDouble(data);
            return String.format("%.1f", number);
        } catch (NumberFormatException e) {
            return data;
        }
    }

}
