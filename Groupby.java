
import java.util.*;
import java.text.DecimalFormat;

public class Groupby implements IGroupBy {

    List<Record> DataList = new ArrayList<Record>();
    Map<String, Float> map = new LinkedHashMap<String, Float>();
    Map<String, Float> sortmap = new LinkedHashMap<String, Float>();

    public void setData(List<Record> records) {
       // System.out.println("Data is Set (" + numberOfRecords(records) + ") records imported");
        DataList = records;

    }

    public void groupby(int byAttribute) {
        if (byAttribute == BY_SECTOR) { //Sector

            List<String> List = new ArrayList<String>();
            for (Record iter : DataList) {

                if (List.contains(iter.sector) == false) {
                    List.add(iter.sector);
                    //System.out.println(iter.sector);
                }
            }

            Object[] x = List.toArray();
            //Arrays.sort(x);

            for (int count = 0; count < x.length; count++) {
                String curr = x[count].toString();
                map.put(curr, 0.0f);

                for (Record iter : DataList) {

                    if (iter.sector.equals(curr)) {
                        float total = map.get(curr);
                        float newTotal = (total + iter.salary);
                        map.put(curr, newTotal);
                    }
                }
            }
        }

        if (byAttribute == BY_EMPLOYER) { //employer
            List<String> List = new ArrayList<String>();
            for (Record iter : DataList) {
                if (List.contains(iter.employer) == false) {
                    List.add(iter.employer);
                    //System.out.println(iter.employer);
                }
            }
            Object[] x = List.toArray();
           // Arrays.sort(x);
            for (int count = 0; count < x.length; count++) {
                map.put(x[count].toString(), 0.0f);
                for (Record iter : DataList) {
                    if (iter.employer.equals(x[count])) {
                        float total = map.get(x[count].toString());
                        float newTotal = (total + iter.salary);
                        map.put(x[count].toString(), newTotal);
                    }
                }
            }
        }

        if (byAttribute == BY_POSITION) { //position
            List<String> List = new ArrayList<String>();
            for (Record iter : DataList) {
                if (List.contains(iter.position) == false) {
                    List.add(iter.position);
                    //System.out.println(iter.position);
                }
            }
            Object[] x = List.toArray();
           // Arrays.sort(x);
            for (int count = 0; count < x.length; count++) {
                map.put(x[count].toString(), 0.0f);
                for (Record iter : DataList) {
                    if (iter.position.equals(x[count])) {
                        float total = map.get(x[count].toString());
                        float newTotal = (total + iter.salary);
                        map.put(x[count].toString(), newTotal);
                    }
                }
            }
        }

        if (byAttribute == BY_NAME) { //name
            List<String> List = new ArrayList<String>();

            for (Record iter : DataList) {
                map.put(iter.name, 0.0f);
                float total = map.get(iter.name);
                float newTotal = (total + iter.salary);
                map.put(iter.name, newTotal);
            }
        }
        sortmap = sortByValue(map);
    }

    public void printTopK(int k) {
        DecimalFormat df = new DecimalFormat("$###,###,000,000.00");

        int count = 0;
        Iterator it = sortmap.entrySet().iterator();
        while (it.hasNext()) {
            if (count < k) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.print(pair.getKey() + " = ");
                float m = (float) pair.getValue();
                System.out.println(df.format(m));
                count++;
            } else if (count == k) {
                break;
            }
        }
    }

    public static int numberOfRecords(List<Record> records) {
        int count = 0;
        for (Record iter : records) {
            count++;
        }
        return count;
    }

    public static void print(List<Record> records) {
        for (Record iter : records) {
            System.out.println("Sector: " + iter.sector);
            System.out.println("Employer: " + iter.employer);
            System.out.println("Name: " + iter.name);
            System.out.println("Position: " + iter.position);
            System.out.println("Salary: " + iter.salary);
            System.out.println("");
        }
    }

    public static void PrintMap(Map x) {
        DecimalFormat df = new DecimalFormat("$###,###,000,000.00");

        Iterator it = x.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getKey() + " = ");
            float m = (float) pair.getValue();
            System.out.println(df.format(m));
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public Map<String, Float> getSortedMap() {
        return sortmap;
    }
     public Map<String, Float> getUnSortedMap() {
        return map;
    }

    public static List<Integer> mapToValueList(Map x) {
        List<Integer> newList = new ArrayList<>();

        Iterator iter = x.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            newList.add(Math.round((float) pair.getValue()));
        }
        return newList;
    }

    
}
