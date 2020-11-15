import java.util.BitSet;
import java.util.Random;

public class VirtualBitmap {
    BitSet bset;
    int m;
    int l;
    int flowCount;

    public VirtualBitmap(int flowCount, int m, int l) {
        this.flowCount = flowCount;
        this.m = m;
        this.l = l;
        bset = new BitSet(m);    //Initialized with all zero values
    }

    /**
     * Implement the multiple hash function
     * Return the array index to set the bit in bloom filter
     *
     * @param element    randomly generated element
     * @return the hashed value for the element
     */
    public int hash(int element) {
        int xID = element;
        //further hashing
        xID = ((xID >> 16) ^ xID) * 0x45d9f3b;
        xID = ((xID >> 16) ^ xID) * 0x45d9f3b;
        xID = (xID >> 16) ^ xID;
        return xID; //hash code for the element
    }

    /**
     *
     * @param element
     * @param arrSize
     * @return the arrayIndex to store the element into
     */
    public int getArrIndex(int element, int arrSize) {
        int arrayIndex = -1;    //Initialize to minus 1
        arrayIndex = element % arrSize;
        return arrayIndex;
    }

    /**
     * @param bitIndex the array index to record into bitset
     */
    public void record (int bitIndex) {
       this.bset.set(bitIndex);
    }

    /**
     *
     * @param bitIndex
     * @return whether that bit is set in bitset
     */
    public boolean query (int bitIndex) {
        return this.bset.get(bitIndex);
    }
}
