package com.micezipper.jwp.printmethods.impl;

import com.micezipper.jwp.printmethods.PrintMethod;
import jakarta.annotation.PostConstruct;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

/**
 *
 * @author miceZipper
 */

public class PDFBoxPrintMethod extends PrintMethod<PrintService> {

    @Override
    public List<String> getPrintersNames() {
        ArrayList<String> result = new ArrayList<>();
        Arrays.stream(PrintServiceLookup.lookupPrintServices(null, null)).forEach(printer -> {
            result.add(printer.getName());
        });
        return result;
    }

    @Override
    public void print(ByteBuffer outputByteBuffer) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(printer);
            try (var pdDoc = PDDocument.load(outputByteBuffer.array())) {
                job.setPageable(new PDFPageable(pdDoc));
                job.print();
            } catch (IOException ex) {
                Logger.getLogger(PDFBoxPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (PrinterException ex) {
            Logger.getLogger(PDFBoxPrintMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostConstruct
    public void init(){
        printer = PrintServiceLookup.lookupDefaultPrintService();
        available = printer != null;
    }

    @Override
    public void setPrinter(String printerName) {
        printer = (PrintService) Arrays.stream(PrintServiceLookup.lookupPrintServices(null, null)).filter((t) -> t.getName().equals(printerName)).toArray()[0];
    }

    @Override
    public String getName() {
        return printer.getName();
    }

}
