# JWP 0.1
Java Web Printer interface
The initial very basic release. Just to make it work.

Requirements:
* Jakarta EE capable Web Server. I used TomEE
* root rights (temprorary requirement. It has issues when using non-root account ro print)

Installation:
* Just deploy it and run

Usage:
1. Choose one of the available print methods.
2. Choose the required printer or keep it default.
3.1. Choose PDFbox if you got Windows. At lt least my tests worked. It uses the PDFbox library to print.
3.2. Choose CUPS if PDFbox does not work. Its uses CUPS library,
3.3. Choose Filesystem if CUPS does not work. It uses the available CLI/terminal environment to run the printing utility.
4. Upload the file (PDF ONLY). If you see the correct application type under the uploading button - it means the file has been uploaded successfully.
5. Click Print.
