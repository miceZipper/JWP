package com.micezipper.jwp.printmethods;

import com.micezipper.jwp.printmethods.impl.*;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author miceZipper
 */

public class PrintMethodFactory implements Serializable {
    
    HashMap<String, PrintMethod> printMethodsList = new HashMap<>();
    
    @Inject PDFBoxPrintMethod pdfBoxPrintMethod;
    @Inject CUPSPrintMethod cupsPrintMethod;
    @Inject FSPrintMethod fsPrintMethod;

    @PostConstruct
    public void init() {
        //TODO: replace by reflection???
        printMethodsList.put("PDFBox", pdfBoxPrintMethod);
        printMethodsList.put("CUPS", cupsPrintMethod);
        printMethodsList.put("Filesystem", fsPrintMethod);

        printMethodsList.entrySet().removeIf((t) -> !t.getValue().isAvailable());
    }

    
    public PrintMethod getPrintMethod(String name) {
        return printMethodsList.get(name);
    }

    public HashMap<String, PrintMethod> getPrintMethodsList() {
        return printMethodsList;
    }

    public Collection<String> getAvailalePrintMethodNames() {
        return printMethodsList.keySet();
    }

}
