package org.vaadin.crudui.support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vaadin.data.BeanPropertySet;
import com.vaadin.data.PropertyDefinition;
import com.vaadin.data.PropertySet;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.shared.util.SharedUtil;

public class BeanExcelBuilder<T> {
	
	public static final String dateCellStyleFormat = "m/d/yy";
	public static final String dateTimeCellStyleFormat = "m/d/yy hh:mm:ss";

	private int startCol;
	private int startRow;
	private String sheetName;
	
	private List<String> properties;
	private List<String> columnsHeaders;
	
	private Map<String, String> formats = new HashMap<>();
	private PropertySet<T> propertySet;
	
	public BeanExcelBuilder(Class<T> clazz) {
		this.propertySet = BeanPropertySet.get(clazz);
	}
	
	protected XSSFSheet buildSheet(XSSFWorkbook wb) {
		if (sheetName!=null) {
			return wb.createSheet(sheetName);
		} else {
			return wb.createSheet();
		}
	}
	
	protected XSSFWorkbook buildWorkBook() {
		return new XSSFWorkbook();
	}
	
	public XSSFWorkbook createExcelDocument(Collection<T> beans) {
		return createExcelDocument(DataProvider.ofCollection(beans));
	}    
	
	public XSSFWorkbook createExcelDocument(DataProvider<T, ?> dataProvider) {
        XSSFWorkbook wb = buildWorkBook();
        XSSFSheet s = buildSheet(wb);
                
        XSSFRow r = s.createRow(startRow);
        
        List<String> headers = columnsHeaders;
        if (headers==null) {
        	headers = propertySet.getProperties().map(pd -> SharedUtil.propertyIdToHumanFriendly(pd.getName())).collect(Collectors.toList());
        }
        int cn=startCol;
        startHeaderRow(r);
        for (String ch : headers) {
            XSSFCell cell = r.createCell(cn++);
            cell.setCellValue(ch);
            setHeaderStyle(cell, ch);
        }
        endHeaderRow(r);

        final AtomicInteger rownum = new AtomicInteger(startRow);
        dataProvider.fetch(new Query<>()).forEach(bean -> { buildRows(bean, s, rownum.incrementAndGet()); }
  		);
       
        return wb;
    }
	
	protected void buildRows(T bean, XSSFSheet s, int rowNumber) {
		XSSFRow r = s.createRow(rowNumber++);
	                	
	    int currCol = startCol;
	    doStarRow(bean, r);
	    List<String> props = properties;
	    if (props==null) {
	    	props = propertySet.getProperties().map(pd -> pd.getName()).collect(Collectors.toList());
	    }
	    for(String propertyName : props) {
	    	
	        PropertyDefinition<T, ?> definition = propertySet
	                .getProperty(propertyName)
	                .orElseThrow(() -> new IllegalArgumentException(
	                        "Could not resolve property name " + propertyName
	                                ));
	    	
	        Object value = definition.getGetter().apply(bean);
	        
			Cell cell = buildCell(r, currCol, definition);
			setCellValue(cell, value, definition);
			setCellStyle(cell, value, definition, formats.get(propertyName));
			currCol++;
		
	    }
	    doEndRow(bean, r);
		
	}

	protected void endHeaderRow(XSSFRow r) {		
	}

	protected void startHeaderRow(XSSFRow r) {		
	}

	protected void setHeaderStyle(XSSFCell cell, String headerName) {		
	}

	protected void doEndRow(T object, Row row) {
		
	}

	protected Cell buildCell(Row row, int colNumber, PropertyDefinition<T, ?> definition) {
		Cell cell = row.createCell(colNumber);
		
		return cell;
	}
	
	protected void doStarRow(T object, Row row) {
	}
    
	protected CellStyle getCellStyle(Cell cell, String format) {
		Workbook wb = cell.getRow().getSheet().getWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		CellStyle dateCellStyle = wb.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(format));
			
		return dateCellStyle;
	}

	protected void setCellValue(Cell cell, Object value, PropertyDefinition<T, ?> definition) {
		
		if (String.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.STRING);
		} else if (Date.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.NUMERIC);
		} else if (Double.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.NUMERIC);
		} else if (Number.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.NUMERIC);
		} else if (Boolean.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.BOOLEAN);		
		} else if (LocalDate.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.NUMERIC);
		} else if (LocalDateTime.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.NUMERIC);
		} else if (definition.getType().isEnum()) {
			cell.setCellType(CellType.STRING);			
		} else if (Calendar.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.NUMERIC);
		} else if (RichTextString.class.isAssignableFrom(definition.getType())) {
			cell.setCellType(CellType.STRING);
		}


		if (value==null)
			return;

		if (String.class.isInstance(value)) {
			cell.setCellValue((String) value);
		} else if (Date.class.isInstance(value)) {
			cell.setCellValue((Date) value);
		} else if (Double.class.isInstance(value)) {
			cell.setCellValue((Double) value);
		} else if (Number.class.isInstance(value)) {
			cell.setCellValue(((Number) value).doubleValue());
		} else if (Boolean.class.isInstance(value)) {
			cell.setCellValue((Boolean) value);
		
		} else if (LocalDate.class.isInstance(value)) {
			cell.setCellValue(DateUtil.getExcelDate(java.sql.Date.valueOf((LocalDate)value)));
		} else if (LocalDateTime.class.isInstance(value)) {
			cell.setCellValue(DateUtil.getExcelDate(java.sql.Timestamp.valueOf((LocalDateTime)value)));

		} else if (definition.getType().isEnum()) {
			cell.setCellValue(((Enum<?>)value).name());
			
		} else if (Calendar.class.isInstance(value)) {
			cell.setCellValue((Calendar) value);
		} else if (RichTextString.class.isInstance(value)) {
			cell.setCellValue((RichTextString) value);
		}
		
	}

	protected void setCellStyle(Cell cell, Object value, PropertyDefinition<T, ?> definition, String format) {
		
		CellStyle cs = null;
		
		if (format==null) {
			
			if (String.class.isAssignableFrom(definition.getType())) {
			} else if (Date.class.isAssignableFrom(definition.getType())) {
				cs = getCellStyle(cell, dateTimeCellStyleFormat);
			} else if (Double.class.isAssignableFrom(definition.getType())) {
			} else if (Number.class.isAssignableFrom(definition.getType())) {
			} else if (Boolean.class.isAssignableFrom(definition.getType())) {		
			} else if (LocalDate.class.isAssignableFrom(definition.getType())) {
				cs = getCellStyle(cell, dateCellStyleFormat);
			} else if (LocalDateTime.class.isAssignableFrom(definition.getType())) {
				cs = getCellStyle(cell, dateTimeCellStyleFormat);
			} else if (definition.getType().isEnum()) {			
			} else if (Calendar.class.isAssignableFrom(definition.getType())) {
				cs = getCellStyle(cell, dateTimeCellStyleFormat);
			} else if (RichTextString.class.isAssignableFrom(definition.getType())) {
			}
		} else {
			cs = getCellStyle(cell, format);
		}
		
		if (cs!=null)
			cell.setCellStyle(cs);	
	}

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public Collection<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	public void setProperties(String... properties) {
		this.properties = new ArrayList<>();
		Collections.addAll(this.properties, properties);
	}

	public Collection<String> getColumnsHeaders() {
		return columnsHeaders;
	}

	public void setColumnsHeaders(List<String> columnsHeaders) {
		this.columnsHeaders = columnsHeaders;
	}

	public void setColumnsHeaders(String... columnsHeaders) {
		this.columnsHeaders = new ArrayList<>();
		Collections.addAll(this.columnsHeaders, columnsHeaders);
	}

	public Map<String, String> getFormats() {
		return formats;
	}

	public void setFormats(Map<String, String> formats) {
		this.formats = formats;
	}
    
}
