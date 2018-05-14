package org.langbein.michael.soundboard.utils;

public class ArrayUtils {


    public static int[] highestN(double[] data, int N) {
        int[] maxIndices = new int[N];

        // Going through all datapoints
        for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {
            double dataPoint = data[dataIndex];

            // going through current maxima
            for (int maxIndicesIndex = 0; maxIndicesIndex < N; maxIndicesIndex++) {

                int currentIndex = maxIndices[maxIndicesIndex];
                double nthHighest = data[currentIndex];

                if(dataPoint > nthHighest) {

                    // shift maxima down
                    for (int rightPointer = N-1; rightPointer > maxIndicesIndex; rightPointer--) {
                        maxIndices[rightPointer] = maxIndices[rightPointer-1];
                    }

                    // put new maximum in place
                    maxIndices[maxIndicesIndex] = dataIndex;

                    // break out of inner loop
                    break;
                }
            }
        }

        return maxIndices;
    }



    public static short[] addArrays(short[] arr1, short[] arr2) {
        int len = Math.max(arr1.length, arr2.length);
        short[] out = new short[len];

        for(int k = 0; k<len; k++) {
            short a = k < arr1.length ? arr1[k] : 0;
            short b = k < arr2.length ? arr2[k] : 0;
            out[k] = (short) (a + b);
        }

        return out;
    }

    public static short[] concatArrays(short[] arr1, short[] arr2) {
        int len = arr1.length + arr2.length;
        short[] out = new short[len];

        int indxOut = 0;

        for(int indx1 = 0; indx1 < arr1.length; indx1++) {
            out[indxOut] = arr1[indx1];
            indxOut += 1;
        }

        for(int indx2 = 0; indx2 < arr2.length; indx2++) {
            out[indxOut] = arr2[indx2];
            indxOut += 1;
        }

        return out;
    }

    public static int getIndexMaximum(double[] array) {
        int indexMaximum = 0;
        double currentMaximum = 0;
        for(int i = 0; i<array.length; i++) {
            if(array[i] > currentMaximum) {
                indexMaximum = i;
                currentMaximum = array[i];
            }
        }
        return indexMaximum;
    }

}
