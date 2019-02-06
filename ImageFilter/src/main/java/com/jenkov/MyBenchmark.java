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

import org.openjdk.jmh.annotations.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class MyBenchmark {

    @State(Scope.Thread)
    public static class MyState {

        @Setup(Level.Trial)
        public void doSetup() {
            File bmpFileBig = new File("1.bmp");
            File bmpFileSmall = new File("3.bmp");
            try {
                BufferedImage bigImage = ImageIO.read(bmpFileBig);
                ImageIO.write(bigImage, "bmp", new File("2.bmp"));
                newBigImage = ImageIO.read(new File("2.bmp"));
                BufferedImage smallImage = ImageIO.read(bmpFileSmall);
                ImageIO.write(smallImage, "bmp", new File("4.bmp"));
                newSmallImage = ImageIO.read(new File("4.bmp"));
                filterBig = new Filter(bigImage, newBigImage);
                filterSmall = new Filter(smallImage, newSmallImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            try {
                ImageIO.write(newBigImage, "bmp", new File("2.bmp"));
                ImageIO.write(newSmallImage, "bmp", new File("4.bmp"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Do TearDown");
        }

        public Filter filterBig;
        public Filter filterSmall;
        BufferedImage newBigImage;
        BufferedImage newSmallImage;
    }



    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_16Horizontal(MyState state) {
            state.filterBig.run(true, 16);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_8Horizontal(MyState state) {
        state.filterBig.run(true, 8);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_4Horizontal(MyState state) {
        state.filterBig.run(true, 4);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_2Horizontal(MyState state) {
        state.filterBig.run(true, 2);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_1Horizontal(MyState state) {
        state.filterBig.run(true, 1);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_16Vertical(MyState state) {
        state.filterBig.run(false, 16);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_8Vertical(MyState state) {
        state.filterBig.run(false, 8);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_4Vertical(MyState state) {
        state.filterBig.run(false, 4);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_2Vertical(MyState state) {
        state.filterBig.run(false, 2);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testBigImage_1Vertical(MyState state) {
        state.filterBig.run(false, 1);
    }


    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_16Horizontal(MyState state) {
        state.filterSmall.run(true, 16);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_8Horizontal(MyState state) {
        state.filterSmall.run(true, 8);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_4Horizontal(MyState state) {
        state.filterSmall.run(true, 4);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_2Horizontal(MyState state) {
        state.filterSmall.run(true, 2);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_1Horizontal(MyState state) {
        state.filterSmall.run(true, 1);
    }


    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_16Vertical(MyState state) {
        state.filterSmall.run(false, 16);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_8Vertical(MyState state) {
        state.filterSmall.run(false, 8);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_4Vertical(MyState state) {
        state.filterSmall.run(false, 4);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_2Vertical(MyState state) {
        state.filterSmall.run(false, 2);
    }

    @Benchmark @Fork(1) @Warmup(iterations = 1) @Measurement(iterations = 3) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSmallImage_1Vertical(MyState state) {
        state.filterSmall.run(false, 1);
    }


}
