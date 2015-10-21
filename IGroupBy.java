
import java.util.*;
interface IGroupBy {
    static int BY_SECTOR   = 0;
    static int BY_EMPLOYER = 1;
    static int BY_POSITION = 2;
    static int BY_NAME     = 3;

    public void setData(List<Record> records);
    public void groupby(int byAttribute);
    public void printTopK(int k);
}