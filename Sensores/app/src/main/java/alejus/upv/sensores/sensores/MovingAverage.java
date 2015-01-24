package alejus.upv.sensores.sensores;

/**
 * This code is available under the Apache 2.0 license from
 * https://code.google.com/p/bigwords/
 */
public class MovingAverage {

    private float circularBuffer[];
    private float avg;
    private int circularIndex;
    private int count;

    /*
    This is the class constructor
     */
    public MovingAverage(int k){
        circularBuffer = new float[k];
        count = 0;
        circularIndex = 0;
        avg = 0;
    }
    /*
    Get the current moving average method
     */
    public float getValue(){
        return avg;
    }

    /*
    Push the newly collected sensor value
     */
    public void pushValue(float x){
        if(count++ == 0){
            primeBuffer(x);
        }
        float lastValue = circularBuffer[circularIndex];
        avg = avg +(x-lastValue)/circularBuffer.length;
        circularBuffer[circularIndex] = x;
        circularIndex = nextIndex(circularIndex);
    }

    public long getCount(){
        return count;
    }

    public void primeBuffer(float val){
        for(int i=0; i < circularBuffer.length;++i){
            circularBuffer[i] = val;
        }
        avg = val;
    }

    private int nextIndex(int curIndex){
        if (curIndex +1 >= circularBuffer.length){
            return 0;
        }
        return curIndex + 1;
    }

}
