package com.micezipper.jwp.printmethods.impl;

import com.micezipper.jwp.Printer;
import com.micezipper.jwp.printmethods.PrintMethod;
import jakarta.annotation.PostConstruct;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;

/**
 *
 * @author miceZipper
 */

public class CUPSPrintMethod extends PrintMethod<CupsPrinter> {

    CupsClient cups;

    @PostConstruct
    public void init(){
        try (java.net.Socket connection = new Socket("127.0.0.1", 631)) {
            cups = new CupsClient();
            printer = cups.getDefaultPrinter();
            available = true;
        } catch (Exception ex) {
            //Logger.getLogger(CUPSPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<String> getPrintersNames() {
        ArrayList<String> result = new ArrayList<>();
        try {
            cups.getPrinters().forEach((t) -> {
                result.add(t.getName());
            });
        } catch (Exception ex) {
            Logger.getLogger(Printer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public void print(ByteBuffer outputByteBuffer) {
        try {
            printer.print(new PrintJob.Builder(outputByteBuffer.array()).color(true).build());
        } catch (Exception ex) {
            Logger.getLogger(CUPSPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setPrinter(String printerName) {
        try {
            printer = cups.getPrinter(printerName);
        } catch (Exception ex) {
            Logger.getLogger(CUPSPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return printer.getName();
    }

}
