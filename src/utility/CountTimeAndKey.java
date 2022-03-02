package utility;

import java.io.Serializable;

public class CountTimeAndKey implements java.io.Serializable, Comparable<CountTimeAndKey> {
    public  int count = 1;
    public  Long time = Long.valueOf(0);
    public  String key = null;

    public CountTimeAndKey(String keychar) {
        this.key = keychar;
    }

    public CountTimeAndKey(int count, long time, String keychar) {
        this.count = count;
        this.time = Long.valueOf(time);
        this.key = keychar;
    }

    @Override
    public int compareTo(CountTimeAndKey o) {
        return o.count - this.count;
    }
}