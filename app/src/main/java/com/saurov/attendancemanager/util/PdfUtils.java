package com.saurov.attendancemanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.saurov.attendancemanager.BuildConfig;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.database.CourseStudent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfUtils {

    private static final String APP_EXTERNAL_DIRECTORY = "/AttendanceManager";

    public static void createAttendanceReport(Activity activity, Course course) {

        String pdfName = course.getNumber() + "_"
                + course.getDepartment() + course.getSeries() + "_" + course.getSection();

        File pdfFile = getPdfFile(activity, APP_EXTERNAL_DIRECTORY, pdfName);

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(pdfFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PdfWriter writer = new PdfWriter(fOut, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0));
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        pdfDocument.setTagged();

        createPDFDocument(pdfDocument, course);
        triggerOpenWithPdfReader(activity, pdfFile);

    }

    private static void triggerOpenWithPdfReader(Context context, File file) {
        Uri uri = FileProvider
                .getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    private static File getPdfFile(Activity activity, String directory, String pdfName) {

        Utils.isStoragePermissionGranted(activity);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + directory;

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, pdfName + ".pdf");

        return file;
    }


    private static void createPDFDocument(PdfDocument pdfDocument, Course course) {
        Document document = new Document(pdfDocument);

        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PdfFont bold = null;
        try {
            bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.setFont(font);

        Text heaven = new Text("Heaven’s light is our guide")
                .setItalic()
                .setFontSize(9);
        Paragraph header = new Paragraph();
        header.add(heaven).setTextAlignment(TextAlignment.CENTER).add("\n");

        Text ruet = new Text("Rajshahi University of Engineering & Technology")
                .setFont(bold)
                .setFontSize(13);
        header.add(ruet).setTextAlignment(TextAlignment.CENTER).add("\n");

        String deptName = "Computer Science and Engineering";

        Text department = new Text("Department of ".concat(deptName))
                .setFont(bold)
                .setFontSize(13);

        header.add(department).setTextAlignment(TextAlignment.CENTER).add("\n");

        Text courseNo = new Text("Course No.: ".concat(course.getNumber()))
                .setFontSize(11);

        header.add(courseNo).setTextAlignment(TextAlignment.CENTER).add("\n");

        Text series = new Text("Series: "
                .concat(course.getDepartment())
                .concat("'" + course.getSeries())
                .concat("(" + course.getSection() + " Section" + ")")
        ).setFontSize(11);

        header.add(series).setTextAlignment(TextAlignment.CENTER).add("\n");

        document.add(header);

        Cell totalClassText = new
                Cell(1, 1)
                .setFont(bold)
                .setFontSize(13)
                .add("Total No. of Classes");

        String totalClasses = String.valueOf(course.getTotalClassTaken());

        Cell totalClassVal = new Cell(1, 1)
                .setFont(bold)
                .setFontSize(13)
                .add(totalClasses);


        Table totalClassTable = new Table(2).setTextAlignment(TextAlignment.CENTER);
        totalClassTable.addCell(totalClassText).addCell(totalClassVal);

        document.add(totalClassTable);

        Table attendanceMarkTable = new Table(5).setFontSize(12).setTextAlignment(TextAlignment.CENTER);

        attendanceMarkTable
                .addHeaderCell("Sl. No.").setFont(bold)
                .addHeaderCell("Roll No.").setFont(bold)
                .addHeaderCell("Attendance").setFont(bold)
                .addHeaderCell("% of Attendance").setFont(bold)
                .addHeaderCell("Obtained Marks ( Out of 8 )").setFont(bold);

        List<CourseStudent> studentList = course.getStudents();

        for (int i = 1; i <= studentList.size(); i++) {

            CourseStudent student = studentList.get(i - 1);

            attendanceMarkTable.addCell(new Cell().add(String.valueOf(i)).setFont(font))
                    .addCell(new Cell().add(String.valueOf(student.getRoll())).setFont(font))
                    .addCell(new Cell().add(String.valueOf(student.getTotalClassAttended())).setFont(font))
                    .addCell(new Cell().add(String.valueOf(Utils.round(student.getAttendancePercentage(), 2))).setFont(font))
                    .addCell(new Cell().add(String.valueOf(student.getAttendanceMark())).setFont(font));
        }


        document.add(attendanceMarkTable);

        Paragraph teacherInfo = new Paragraph().setTextAlignment(TextAlignment.RIGHT);

        String steacherName = "Barshon Sen";
        String steacherDesignation = "Assistant Professor";
        String spost = "Department of CSE, RUET";

        Text teacherName = new Text(steacherName).setFontSize(13).setItalic().setFont(bold);
        Text teacherDesignation = new Text(steacherDesignation).setFontSize(13).setFont(font);
        Text teacherPost = new Text(spost).setFontSize(13).setFont(font);
        Text invisibleText = new Text(spost + "AAAAA").setFontSize(13).setFontColor(Color.WHITE).setFont(font);


        teacherInfo.add("\n").add("\n");
        teacherInfo.add(invisibleText.setBorderBottom(new SolidBorder(Color.BLACK, 1f))).add("\n");
        teacherInfo.add(teacherName).add("\n");
        teacherInfo.add(teacherDesignation).add("\n");
        teacherInfo.add(teacherPost).add("\n");

        document.add(teacherInfo);

        document.close();

    }

}
