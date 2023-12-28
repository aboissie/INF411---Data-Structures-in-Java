public class UnionFind {
    int parent;
    int rank;
    

}

public class Network {
    PRNG prng;
    int success;
    int callee; 
    int caller;
    

    public Network(int m){
        this.prng = new PRNG(m);
        this.success = 0;
        this.callee = -1;
        this.caller = -1;
    }  

    public int getCallCount(){
        return this.success;
    }

    public int getLastCaller(){
        return this.caller;
    }

    public int getLastCallee(){
        return this.callee;
    }

    public void oneCall(){
        int caller = this.prng.getNext();
        int callee = this.prng.getNext();
        
        if(caller != callee){
            this.success += 1;
            this.caller = caller;
            this.callee = callee;
        }
    }

    public void runUntilCall(int u){
        int origCaller = caller;
        while(caller == origCaller) this.oneCall();
        while(this.callee != u & this.caller != u) this.oneCall();
    }

    public void runUntilConnected(int u, int v){
        
    }
}
