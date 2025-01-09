public class SCell implements Cell {
    private String line;
    private int type;

    public SCell(String s) {
        setData(s);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String s) {
        this.line = s;
    }

    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int t) {
        this.type = t;
    }

    @Override
    public void setOrder(int t) {

    }

}