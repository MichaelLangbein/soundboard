package org.langbein.michael.soundboard.utils;


public class FFT {

    public static Complex[] paddedFft(Complex[] signal) {
        int N = signal.length;
        int M = nextPowerOf2(N);
        if(N == M) {
            return fft(signal);
        } else {
            Complex[] paddedSignal = new Complex[M];
            for(int m = 0; m<M; m++) {
                paddedSignal[m] = signal[m%N];
            }
            Complex[] result = fft(paddedSignal);
            Complex[] cutResult = new Complex[N];
            for(int n = 0; n<N; n++){
                cutResult[n] = result[n];
            }
            return cutResult;
        }
    }

    private static int nextPowerOf2(int n)  {
        int count = 0;

        /* First n in the below condition
        is for the case where n is 0*/
        if (n > 0 && (n & (n - 1)) == 0){
            return n;
        }

        while(n != 0) {
            n >>= 1;
            count += 1;
        }

        return 1 << count;
    }

    public static Complex[] fft(Complex[] signal) {
        int N = signal.length;

        if(N == 1){
           return signal;
        }

        Complex[] signalEven = new Complex[N/2];
        Complex[] signalOdd  = new Complex[N/2];

        int evenIndex = 0;
        int oddIndex = 0;
        for(int k = 0; k < N; k++) {
            if(k%2 == 0){
                signalEven[evenIndex] = signal[k];
                evenIndex += 1;
            } else {
                signalOdd[oddIndex] = signal[k];
                oddIndex += 1;
            }
        }

        Complex[] ampsEven = fft(signalEven);
        Complex[] ampsOdd  = fft(signalOdd);

        Complex[] amplitudes = new Complex[N];
        for(int n = 0; n<(N/2); n++){
            Complex exponent  = new Complex(0, -2.0 * Math.PI * ((double)n/(double)N));
            Complex wn        = Complex.exp(exponent);
            Complex fac1   = ampsEven[n];
            Complex fac2   = Complex.times(wn, ampsOdd[n]);
            Complex amp_n  = Complex.plus(fac1, fac2);
            Complex amp_2n = Complex.minus(fac1, fac2);
            amplitudes[n]        = amp_n;
            amplitudes[n + N/2]  = amp_2n;
        }

        return amplitudes;
    }

    public static double[] getFrequencies(int N, double deltaTimestep) {
        // f = [0, 1, ...,   n/2-1,     -n/2, ..., -1] / (d*n)   if n is even
        // f = [0, 1, ..., (n-1)/2, -(n-1)/2, ..., -1] / (d*n)   if n is odd
        boolean even = (N%2 == 0);
        double[] frequencies;

        if(even) {
            frequencies = new double[N/2];
            for(int n = 0; n<(N/2 -1); n++) {
                double f = n / (deltaTimestep * N);
                frequencies[n] = f;
            }
        } else {
            frequencies = new double[(N-1)/2];
            for(int n = 0; n<(N/2); n++){
                double f = n / (deltaTimestep * N);
                frequencies[n] = f;
            }
        }

        return frequencies;
    }
}