public class CellEntry implements Index2D {
    private char x;
    private int y;

    public CellEntry(char x, int y) {
        this.x = Character.toUpperCase(x);
        this.y = y;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid";
        }
        return String.valueOf(x) + y;
    }

    @Override
    public boolean isValid() {
        return (x >= 'A' && x <= 'Z') && (y >= 0 && y <= 99);
    }

    @Override
    public int getX() {
        if (!isValid()) {
            return Ex2Utils.ERR;
        }
        return x - 'A';
    }

    @Override
    public int getY() {
        if (!isValid()) {
            return Ex2Utils.ERR;
        }
        return y;
    }
}


/*

public class CellEntry  implements Index2D {
     private char x;
     private int y;

     public CellEntry(char x, int y){
         this.x = x;
         this.y = y;
     }

    @Override
    public String toString() {
        return this.x+String.valueOf(this.y);
    }

    @Override
    public boolean isValid() {
        for (int i = 0; i < Ex2Utils.ABC.length; i++) {
            String bigX =String.valueOf(Character.toUpperCase(this.x));
            String abcValue = Ex2Utils.ABC[i];
            if (bigX.equals(abcValue) && (this.y >= 0 && this.y <= 99)) {
                return true;
            }
        }
         return false;
    }

    @Override
    public int getX() {
        try {
            char upperC = Character.toUpperCase(this.x);
            return upperC - 'A';
        } catch (Exception e) {
            return Ex2Utils.ERR;
        }
    }


    @Override
    public int getY() {
         if(this.y >= 0 && this.y <= 99) return this.y;
            return Ex2Utils.ERR;
     }
}



 */