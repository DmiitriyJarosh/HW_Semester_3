/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jenkov;

import com.company.Main;
import org.openjdk.jmh.annotations.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Fork(1)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 6, time = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MyBenchmark {

    @State(Scope.Thread)
    public static class MyState {
        @Setup(Level.Trial)
        public void doSetup() {
            NUM_OF_NUMBERS = 1000000;
            Main.setNumOfNumbers(NUM_OF_NUMBERS);
            bigIntA = new int[NUM_OF_NUMBERS];
            bigIntB = new int[NUM_OF_NUMBERS];
            for (int i = 0; i < NUM_OF_NUMBERS; i++) {
                bigIntA[i] = 7;
                bigIntB[i] = 6;
            }
            bigIntA = Main.reverse(bigIntA);
            bigIntB = Main.reverse(bigIntB);
        }

        @TearDown(Level.Trial)
        public void doTearDown() {}
        int[] bigIntA;
        int[] bigIntB;
        int[] bigIntParallelRes;
        int NUM_OF_NUMBERS;
    }


    @Benchmark
    public void testLongSum_Single(MyState state) {
        for (int i = 0; i < 10; i++) {
            state.bigIntParallelRes = Main.sum(state.bigIntA, state.bigIntB);
        }
    }

    @Benchmark
    public void testLongSum_1(MyState state) {
        int NUM_OF_THREADS = 1;
        Main.setSTEP(state.NUM_OF_NUMBERS / NUM_OF_THREADS);
        Main.setNumOfThreads(NUM_OF_THREADS);
        for (int i = 0; i < 10; i++) {
            try {
                state.bigIntParallelRes = Main.multiSum(state.bigIntA, state.bigIntB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Benchmark
    public void testLongSum_2(MyState state) {
        int NUM_OF_THREADS = 2;
        Main.setSTEP(state.NUM_OF_NUMBERS / NUM_OF_THREADS);
        Main.setNumOfThreads(NUM_OF_THREADS);
        for (int i = 0; i < 10; i++) {
            try {
                state.bigIntParallelRes = Main.multiSum(state.bigIntA, state.bigIntB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Benchmark
    public void testLongSum_4(MyState state) {
        int NUM_OF_THREADS = 4;
        Main.setSTEP(state.NUM_OF_NUMBERS / NUM_OF_THREADS);
        Main.setNumOfThreads(NUM_OF_THREADS);
        for (int i = 0; i < 10; i++) {
            try {
                state.bigIntParallelRes = Main.multiSum(state.bigIntA, state.bigIntB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Benchmark
    public void testLongSum_8(MyState state) {
        int NUM_OF_THREADS = 8;
        Main.setSTEP(state.NUM_OF_NUMBERS / NUM_OF_THREADS);
        Main.setNumOfThreads(NUM_OF_THREADS);
        for (int i = 0; i < 10; i++) {
            try {
                state.bigIntParallelRes = Main.multiSum(state.bigIntA, state.bigIntB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
