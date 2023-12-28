import java.util.LinkedList;

public class PRNG {
    int m;
    long idx;
    LinkedList<Long> q;

    public PRNG(int m){
        this.m = m;
        this.idx = -1;
        this.q = new LinkedList<Long>();
    }

    private static long sumModulo(long idx, int m){
        return ((300007 * idx * idx * idx) + (900021 * idx * idx) + (700018 * idx + 200007))  % m ;
    }

    public int getNext(){
        this.idx += 1;

        if(this.idx < 55){
            this.q.add(sumModulo(this.idx, this.m));
            return (int) sumModulo(this.idx, this.m);
        }
        
        long next = (this.q.get(31) + this.q.pop()) % m;
        this.q.add(next);

        return (int) next;
    }
}
