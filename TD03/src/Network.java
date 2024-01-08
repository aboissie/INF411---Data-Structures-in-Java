import java.util.Vector;

public class Network {
    PRNG prng;
    int success = 0;
    int callee = -1; 
    int caller = -1;
    private UnionFind relations;

    public Network(int m){
        prng = new PRNG(m);
        relations = new UnionFind(m);
    }  

    public int getCallCount(){
        return success;
    }

    public int getLastCaller(){
        return caller;
    }

    public int getLastCallee(){
        return callee;
    }

    public void oneCall(){
        int caller = this.prng.getNext();
        int callee = this.prng.getNext();
        
        if(caller != callee){
            this.success += 1;
            this.caller = caller;
            this.callee = callee;
            
            this.relations.union(caller, callee);
        }
    }

    public void runUntilCall(int u){
        int origCaller = caller;
        while(caller == origCaller) this.oneCall();
        while(this.callee != u & this.caller != u) this.oneCall();
    }

    public void runUntilConnected(int u, int v){
        while(relations.find(u) != relations.find(v)) oneCall();
    }

    public int getContactCount(int u){
        return relations.classCardinality(u);
    }

    public static void main(String[] args){
        int president = 524287;
        int m = 1000000;
        Network n = new Network(m);
        while(n.getContactCount(president) <= (float) m * 0.01) n.oneCall();
        System.out.println("Pour relier le président à 1 % de la population, il faut passer n = " + n.success + " appels réussis");
        
        n = new Network(m);
        while(n.getContactCount(president) <= (float) m * 0.5) n.oneCall();
        System.out.println("Pour relier le président à 50 % de la population, il faut passer n = " + n.success + " appels réussis");

        n = new Network(m);
        while(n.getContactCount(president) <= (float) m * 0.99) n.oneCall();
        System.out.println("Pour relier le président à 99 % de la population, il faut passer n = " + n.success + " appels réussis");
    }
}
