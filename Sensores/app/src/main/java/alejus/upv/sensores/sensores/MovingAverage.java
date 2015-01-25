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
        // k = {4,20,100}
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
        if(count++ == 0){//the first time a value will be added we will go inside here
            primeBuffer(x); //All the circular buffer will be added with the first value introduced
        }
        float lastValue = circularBuffer[circularIndex];//get the value which will be replaced
        avg = avg +(x-lastValue)/circularBuffer.length;//calculate the average with the new value
        circularBuffer[circularIndex] = x;//insert the new value on the actual position
        circularIndex = nextIndex(circularIndex);//obtain next position where the new value will be added
    }

    public long getCount(){
        return count;
    }

    private void primeBuffer(float val){
        for(int i=0; i < circularBuffer.length;++i){
            circularBuffer[i] = val;
        }
        avg = val;
    }

    private int nextIndex(int curIndex){
        if (curIndex +1 >= circularBuffer.length){
            return 0; //we must return to the first position if the actual one is the last one
        }
        return curIndex + 1;//if not, then move to the next buffer position
    }

}
