package com.micezipper.jwp.printmethods.impl;

import com.micezipper.jwp.printmethods.PrintMethod;
import jakarta.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miceZipper
 */
public class FSPrintMethod extends PrintMethod<String> {

    File tempFile;
    String system;
    Runtime runtime = Runtime.getRuntime();

    @PostConstruct
    public void init() {
        try {
            system = System.getProperty("os.name").toLowerCase();
            tempFile = Files.createTempFile("jwp_", ".pdf").toFile();
            tempFile.deleteOnExit();
            printer = getDefaultPrinter();
            available = true;
        } catch (Exception ex) {
            Logger.getLogger(FSPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getDefaultPrinter() {
        if (system.contains("win")) {
            StringBuilder buf = new StringBuilder();
            String[] split = exec("wmic printer get name,default | find \"TRUE\"").strip().split("[ ]{1,}");
            Arrays.stream(Arrays.copyOfRange(split, 1, split.length - 1)).forEach((t) -> {
                buf.append(t).append(" ");
            });
            return buf.toString().strip();
        } else if (system.contains("mac")) {
            return exec("lpstat -d").strip().substring(28);
        } else if (system.contains("nix") | system.contains("nux") | system.contains("aix")) {
            return exec("lpstat -d").strip().substring(28);
        } else if (system.contains("sunos")) {
            return exec("lpstat -d").strip().substring(28);
        }
        return null;
    }

    @Override
    public List<String> getPrintersNames() {
        if (system.contains("win")) {
            return exec("wmic printer get Name | find /v \"Name\"").strip().lines().filter((t) -> !t.isBlank()).toList();
        } else if (system.contains("mac")) {
            return exec("lpstat -a | cut -f1 -d ' '").strip().lines().filter((t) -> !t.isBlank()).toList();
        } else if (system.contains("nix") | system.contains("nux") | system.contains("aix")) {
            return exec("lpstat -a | cut -f1 -d ' '").strip().lines().filter((t) -> !t.isBlank()).toList();
        } else if (system.contains("sunos")) {
            return exec("lpstat -a | cut -f1 -d ' '").strip().lines().filter((t) -> !t.isBlank()).toList();
        }
        return null;
    }

    @Override
    public void print(ByteBuffer outputByteBuffer) {
        try (var out = new BufferedOutputStream(new FileOutputStream(tempFile))) {
            out.write(outputByteBuffer.array());
            out.flush();
            if (system.contains("win")) {
                //get printer port
                String test = exec("wmic printer get Name,portname | find \"".concat(printer).concat("\"")).strip();
                StringBuilder output = new StringBuilder();

                if (test.lines().count() > 1) {
                    test.lines().filter((t) -> {
                        if (t.isEmpty()) {
                            return false;
                        } else {
                            String[] result = t.split("[ ]{1,}");
                            StringBuilder localOutput = new StringBuilder();
                            for (int i = 0; i < result.length - 1; i++) {
                                localOutput.append(result[i]).append(" ");
                            }
                            return localOutput.toString().strip().equals(printer);
                        }
                    }).forEach((t) -> {
                        output.append(t);
                    });
                } else {
                    output.append(test);
                }
                String[] split = output.toString().strip().split("[ ]{1,}");
                exec("print /D:".concat(split[split.length - 1]).concat(" ").concat(tempFile.getAbsolutePath()));
            } else if (system.contains("mac")) {
                exec("lp -d ".concat(printer).concat(" ").concat(tempFile.getAbsolutePath()));
            } else if (system.contains("nix") | system.contains("nux") | system.contains("aix")) {
                exec("lp -d ".concat(printer).concat(" ").concat(tempFile.getAbsolutePath()));
            } else if (system.contains("sunos")) {
                exec("lp -d ".concat(printer).concat(" ").concat(tempFile.getAbsolutePath()));
            }
        } catch (IOException ex) {
            Logger.getLogger(FSPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        tempFile.delete();
        try {
            tempFile = Files.createTempFile("jwp_", ".pdf").toFile();
            tempFile.deleteOnExit();
        } catch (IOException ex) {
            Logger.getLogger(FSPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void setPrinter(String printerName) {
        printer = printerName;
    }

    @Override
    public String getName() {
        return printer;
    }

    private String exec(String cmd) {
        StringBuilder result = new StringBuilder();
        try {
            Process exec;
            if (system.contains("win")) {
                exec = runtime.exec(new String[]{"cmd", "/c", cmd});
            } else if (system.contains("mac")) {
                exec = runtime.exec(new String[]{"sh", "-c", cmd});
            } else if (system.contains("nix") | system.contains("nux") | system.contains("aix")) {
                exec = runtime.exec(new String[]{"sh", "-c", cmd});
            } else if (system.contains("sunos")) {
                exec = runtime.exec(new String[]{"sh", "-c", cmd});
            } else {
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    result.append("\n");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FSPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(FSPrintMethod.class.getName()).log(Level.INFO, result.toString());
        return result.toString();
    }

}
