package org.vaadin.crudui.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;

public abstract class ExcelOnDemandStreamResource implements ConnectorResource {

	protected abstract XSSFWorkbook getWorkbook();
	
	@Override
	public DownloadStream getStream() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			getWorkbook().write(baos);
			baos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new DownloadStream(new ByteArrayInputStream(baos.toByteArray()), getMIMEType(), getFilename());
	}
	
	@Override
	public String getFilename() {
		return "export.xlsx";
	}
	
	@Override
	public String getMIMEType() {
		return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	}
	
	
}
