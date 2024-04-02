package cn.itscloudy.infocommit.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CmdUtil {
    private CmdUtil() {
    }

    public static CmdUtil.Result runGitCommand(String basePath, String... params) {
        try {
            List<String> parts = new ArrayList<>();
            parts.add("git");
            parts.add("-C");
            parts.add(basePath);
            parts.addAll(Arrays.asList(params));
            return CmdUtil.run(parts);
        } catch (IOException e) {
            return new CmdUtil.Result(null, e.getMessage(), 1);
        }
    }

    public static Result run(List<String> command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        String input = readInputStream(inputStream);

        InputStream errorStream = process.getErrorStream();
        String error = readInputStream(errorStream);
        process.destroy();
        try {
            process.waitFor(1000, MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Result(input, error, process.exitValue());
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        String input = null;
        if (inputStream != null) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                input = new String(bytes, 0, len);
            }
        }
        return input;
    }

    public record Result(String output, String error, int exitValue) {
        public Result(String output, String error, int exitValue) {
            this.output = output == null ? "" : output.trim();
            this.error = error == null ? "" : error.trim();
            this.exitValue = exitValue;
        }
    }
}
