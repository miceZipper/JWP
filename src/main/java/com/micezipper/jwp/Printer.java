package com.micezipper.jwp;

import com.micezipper.jwp.printmethods.PrintMethodFactory;
import java.io.Serializable;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miceZipper
 */
@Named(value = "printer")
@SessionScoped
public class Printer implements Serializable {

    //with getters and setters (for JSF render)
    private String name;
    private Part inputFile;
    private String contentType;
    private String printMethodName;
    private Collection<String> availalePrintMethodNames;

    //internal usage only
    private ByteBuffer outputByteBuffer;
    @Inject private PrintMethodFactory printMethodFactory;

    @PostConstruct
    public void init() {
        System.setProperty("java.awt.headless", "true");
        availalePrintMethodNames = printMethodFactory.getAvailalePrintMethodNames();
        printMethodName = (String) availalePrintMethodNames.toArray()[0];
        name = printMethodFactory.getPrintMethod(printMethodName).getName();
    }

    public Collection<String> getAvailalePrintMethodNames() {
        return availalePrintMethodNames;
    }

    public void setAvailalePrintMethodNames(Collection<String> availalePrintMethodNames) {
        this.availalePrintMethodNames = availalePrintMethodNames;
    }

    public String getPrintMethodName() {
        return printMethodName;
    }

    public void setPrintMethodName(String printMethod) {
        this.printMethodName = printMethod;
    }

    public void print() {
        try {
            printMethodFactory.getPrintMethod(printMethodName).setPrinter(name);
            printMethodFactory.getPrintMethod(printMethodName).print(outputByteBuffer);
            outputByteBuffer.clear();
            inputFile.delete();
            contentType = "";
        } catch (IOException ex) {
            Logger.getLogger(Printer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Part getInputFile() {
        return inputFile;
    }

    public void setInputFile(Part inputFile) {
        try {
            this.inputFile = inputFile;
            outputByteBuffer = ByteBuffer.wrap(this.inputFile.getInputStream().readAllBytes());
            contentType = this.inputFile.getContentType();
        } catch (IOException ex) {
            Logger.getLogger(Printer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getPrinterNames() {
        return printMethodFactory.getPrintMethod(printMethodName).getPrintersNames();
    }

}
