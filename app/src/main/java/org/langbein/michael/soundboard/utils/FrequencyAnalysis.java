package org.langbein.michael.soundboard.utils;


public class FrequencyAnalysis {

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
        double normalizer = batchSize * 20;
        for(int k = 0; k < keyFrequencies.length; k++) {
            double ampNormed = rawAmps[k] / normalizer;
            normedAmps[k] = ampNormed;
        }

        return normedAmps;
    }

    /**
     * Inner product with frequency.
     * @param currentBatch
     * @param frq
     * @return
     */
    private static double innerProduct(short[] currentBatch, float frq, int sampleRate) {
        double amp = 0;
        double deltaT = 1.0 / (double) sampleRate;
        for(int t = 0; t < currentBatch.length; t++) {
            amp += (double)currentBatch[t] * Math.cos(2.0 * Math.PI * frq * deltaT * t);
        }
        return amp;
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
