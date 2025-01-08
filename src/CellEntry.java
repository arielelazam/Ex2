public class CellEntry implements Index2D {
    private char x;
    private int y;

    public CellEntry(char x, int y) {
        this.x = Character.toUpperCase(x);
        this.y = y;
    }

    public CellEntry() {

    }

    @Override
    public String toString() {
        char column = (char) ('A' + x);
        int row = y;
        return column + String.valueOf(row);
    }

    @Override
    public boolean isValid() {
        String str = this.toString();
        if(str == null || str.length() < 2) {
            return false;
        }

        char firstChar = str.charAt(0);
        if(!((firstChar >= 'A' && firstChar <='Z') || (firstChar >= 'a' && firstChar <= 'z'))){
            return false;
        }

        try {
            int number = Integer.parseInt(str.substring(1));
            if(number < 0 || number > 99) {
                return false;
            }
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public int getX() {
        if(this.x < 0 || this.x > 25){
            throw new IllegalArgumentException("Error number");
        }
        return (char) ('A' + this.x);
    }

    @Override
    public int getY() {

        return y;
    }
}
