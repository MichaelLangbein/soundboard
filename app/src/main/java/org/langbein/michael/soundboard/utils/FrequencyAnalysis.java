package org.langbein.michael.soundboard.utils;


public class FrequencyAnalysis {


    /**
     * The normal fourier analysis is oftentainted by weaker, irrelevant signals.
     * For that reason, we filter out all but  the strongest two amplitudes.
     * Alternatively, we could have chosen a high-pass amplitude.
     * @param data
     * @return
     */
    public static double[] postprocess(double[] data) {
        int maxIndx = 0;
        int secIndx = 0;

        for(int k = 0; k < data.length; k++)  {
            if(data[k] > data[maxIndx]){
                secIndx = maxIndx;
                maxIndx = k;
            } else if (data[k] > data[secIndx]) {
                secIndx = k;
            }
        }

        for(int k = 0; k<data.length; k++){
            if(k != maxIndx &&  k != secIndx){
                data[k] = 0;
            }
        }

        return data;
    }

    /**
     * Analyse the relative brightness of every key by calculating the inner product with the keys frequency
     * @param currentBatch
     * @param keyFrequencies
     * @return
     */
    public static double[] analyseInputOnKeys(short[] currentBatch, float[] keyFrequencies, int sampleRate) {

        int batchSize = currentBatch.length;

        double[] rawAmps = new double[keyFrequencies.length];
        for(int k = 0; k < keyFrequencies.length; k++) {
            float frq = keyFrequencies[k];
            double amp = innerProduct(currentBatch, frq, sampleRate);
            rawAmps[k] = amp;
        }

        double[] normedAmps = new double[keyFrequencies.length];
        double normalizer = batchSize;
        for(int k = 0; k < keyFrequencies.length; k++) {
            double ampNormed = rawAmps[k] / normalizer;
            normedAmps[k] = ampNormed;
        }

        return normedAmps;
    }

    /**
     * Inner product with frequency.
     * Not really inner product, though.
     * @param currentBatch
     * @param frq
     * @return
     */
    private static double innerProduct(short[] currentBatch, float frq, int sampleRate) {
        double ampSinPart = 0;
        double ampCosPart = 0;
        double deltaT = 1.0 / (double) sampleRate;
        double angularVelocity = 2.0 * Math.PI * frq * deltaT;
        for(int t = 0; t < currentBatch.length; t++) {
            double btch = (double)currentBatch[t];
            double pt = angularVelocity * t;
            double sinComp = btch * Math.sin(pt);
            double cosComp = btch * Math.cos(pt);
            ampSinPart += sinComp;
            ampCosPart += cosComp;
        }
        double ampSinPartNormed = ampSinPart / (double) currentBatch.length;
        double ampCosPartNormed = ampCosPart / (double) currentBatch.length;
        return (ampSinPartNormed*ampSinPartNormed + ampCosPartNormed*ampCosPartNormed);
    }

    /**
     * Analyse the keys relative brightness using an fft.
     * @param currentBatch
     * @param keyFrequencies
     * @param delta
     * @return
     */
    public static double[] analyseInputWithFFT(short[] currentBatch, float[] keyFrequencies, long delta) {

        int batchSize = currentBatch.length;
        double[] rawAmps = new double[keyFrequencies.length];

        Complex[] currentBatchComplex = Complex.transformToComplex(currentBatch);
        Complex[] currentBatchAmplitudes = FFT.paddedFft(currentBatchComplex); // This is what takes too long. Needs 500MB memory, but only has 60 initially. Eventually causes IllegalStateException: Queue full
        double[] currentBatchFrequencies = FFT.getFrequencies(batchSize, delta/batchSize);
        double[] currentBatchIntensity = associateAmplitudeWithKeys(currentBatchAmplitudes, currentBatchFrequencies, keyFrequencies);

        return currentBatchIntensity;
    }


    public static short[] downsample(short[] currentBatch, int every) {
        int N = currentBatch.length;
        short[] out = new short[N / every];
        int outindex = 0;
        for(int i = 0; i<(N-every); i+=every){
            out[outindex] = currentBatch[i];
            outindex += 1;
        }
        return out;
    }


    /*
     * @TODO: Bisher ist Zuweisung f < key.getF . Besser: f < key.getF + delta
     */
    private static double[] associateAmplitudeWithKeys(Complex[] currentBatchAmplitudes, double[] currentBatchFrequencies, float[] keyFrequencies) {
        double[] keyAmplitudes = new double[keyFrequencies.length];
        double totalSum = 0;
        int F = currentBatchFrequencies.length;

        // Filling key's amplitudes by all proximate fourier-slots.
        int currentKeyIndex = 0;
        float currentKeyFreq = keyFrequencies[currentKeyIndex];
        for(int f = 0; f<F; f++){
            double currentFrequency = currentBatchFrequencies[f];
            while(currentFrequency < currentKeyFreq && currentKeyIndex < keyFrequencies.length - 1){
                currentKeyIndex += 1;
                currentKeyFreq = keyFrequencies[currentKeyIndex];
            }
            double power = currentBatchAmplitudes[f].getPower();
            keyAmplitudes[currentKeyIndex] += power;
            totalSum += power;
        }

        // Normalizing
        for(int k = 0; k<keyAmplitudes.length; k++) {
            keyAmplitudes[k] = keyAmplitudes[k]/totalSum;
        }

        return keyAmplitudes;
    }

}
