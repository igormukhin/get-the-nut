package de.igormukhin.getthenut.solve;

import com.google.common.util.concurrent.RateLimiter;
import de.igormukhin.getthenut.Game;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Throwables.propagate;
import static java.util.Objects.requireNonNull;

public class SolverProgressPrinter implements SolverProgress {

    private final Writer writer;
    private final RateLimiter rateLimiter = RateLimiter.create(1, 10, TimeUnit.SECONDS);
    private boolean headerPrinted;

    public SolverProgressPrinter() {
        this(new OutputStreamWriter(System.out));
    }

    public SolverProgressPrinter(Writer writer) {
        this.writer = requireNonNull(writer);
        printHeader();
    }

    private void printHeader() {
        println("The number is the length of the optimized path. " +
                "The smaller the number to close we are to the completion. " +
                "Don't worry if the number gets bigger, you will just have to wait more. )))");
        headerPrinted = true;
    }

    @Override
    public void onBetterPathFound(Game betterPath) {
        if (rateLimiter.tryAcquire(1)) {
            if (headerPrinted) {
                println(Integer.toString(betterPath.rolls()));
            } else {
                printHeader();
            }
        }
    }

    private void println(String str) {
        try {
            writer.write(str);
            writer.write(System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw propagate(e);
        }
    }
}
