package objetos;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class EncabezadosExcel {
    public EncabezadosExcel() {
    }

    public void createHeaderRow(Sheet sheet, String type) {

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);

        Row row = sheet.createRow(0);
        int col = 0;

        switch (type) {
            case "ALUMNOS":
                Cell id = row.createCell(++col);
                id.setCellStyle(cellStyle);
                id.setCellValue("ID ALUMNO");

                Cell na = row.createCell(++col);
                na.setCellStyle(cellStyle);
                na.setCellValue("NOMBRE ALUMNO");

                Cell ap = row.createCell(++col);
                ap.setCellStyle(cellStyle);
                ap.setCellValue("APELLIDO PATERNO");

                Cell am = row.createCell(++col);
                am.setCellStyle(cellStyle);
                am.setCellValue("APELLIDO MATERNO");

                Cell mail = row.createCell(++col);
                mail.setCellStyle(cellStyle);
                mail.setCellValue("CORREO");

                Cell rfc = row.createCell(++col);
                rfc.setCellStyle(cellStyle);
                rfc.setCellValue("RFC");

                Cell grupo = row.createCell(++col);
                grupo.setCellStyle(cellStyle);
                grupo.setCellValue("GRUPO");
                break;
            case "USUARIOS":
                Cell un = row.createCell(++col);
                un.setCellStyle(cellStyle);
                un.setCellValue("NOMBRE");

                Cell uap = row.createCell(++col);
                uap.setCellStyle(cellStyle);
                uap.setCellValue("APELLIDO PATERNO");

                Cell uam = row.createCell(++col);
                uam.setCellStyle(cellStyle);
                uam.setCellValue("APELLIDO MATERNO");

                Cell usn = row.createCell(++col);
                usn.setCellStyle(cellStyle);
                usn.setCellValue("NOMBRE DE USUARIO");

                Cell rl = row.createCell(++col);
                rl.setCellStyle(cellStyle);
                rl.setCellValue("ROL");
                break;
            case "GRUPOS":
                Cell idg = row.createCell(++col);
                idg.setCellStyle(cellStyle);
                idg.setCellValue("IDGRUPO");

                Cell cdg = row.createCell(++col);
                cdg.setCellStyle(cellStyle);
                cdg.setCellValue("CODIGO_GRUPO");

                Cell ng = row.createCell(++col);
                ng.setCellStyle(cellStyle);
                ng.setCellValue("NOMBRE_GRUPO");

                Cell lvl = row.createCell(++col);
                lvl.setCellStyle(cellStyle);
                lvl.setCellValue("NIVEL");
                break;
            case "PAGOS":
                Cell idp = row.createCell(++col);
                idp.setCellStyle(cellStyle);
                idp.setCellValue("IDPAGOS");

                Cell mnt = row.createCell(++col);
                mnt.setCellStyle(cellStyle);
                mnt.setCellValue("MONTO");

                Cell fch = row.createCell(++col);
                fch.setCellStyle(cellStyle);
                fch.setCellValue("FECHA");

                Cell cnpt = row.createCell(++col);
                cnpt.setCellStyle(cellStyle);
                cnpt.setCellValue("CONCEPTO");

                Cell ida = row.createCell(++col);
                ida.setCellStyle(cellStyle);
                ida.setCellValue("IDALUMNO");
                break;
            case "ADEUDO":
                Cell idadd = row.createCell(++col);
                idadd.setCellStyle(cellStyle);
                idadd.setCellValue("ID ADEUDO");

                Cell ctdd = row.createCell(++col);
                ctdd.setCellStyle(cellStyle);
                ctdd.setCellValue("CANTIDAD");

                Cell fchad = row.createCell(++col);
                fchad.setCellStyle(cellStyle);
                fchad.setCellValue("FECHA");

                Cell ctp = row.createCell(++col);
                ctp.setCellStyle(cellStyle);
                ctp.setCellValue("PAGADO");

                Cell idda = row.createCell(++col);
                idda.setCellStyle(cellStyle);
                idda.setCellValue("ID ALUMNO");
                break;
    }
}
}
