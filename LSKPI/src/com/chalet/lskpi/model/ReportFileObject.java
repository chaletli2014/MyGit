package com.chalet.lskpi.model;

import java.io.Serializable;

/**
 * @author Chalet
 * @version 创建时间：2014年4月3日 下午11:27:17
 * 类说明
 */

public class ReportFileObject implements Serializable{

	private String filePath;
	private String fileName;
	
	private String folderName_en;
	private String folderName_cn;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    public String getFolderName_en() {
        return folderName_en;
    }
    public void setFolderName_en(String folderName_en) {
        this.folderName_en = folderName_en;
    }
    public String getFolderName_cn() {
        return folderName_cn;
    }
    public void setFolderName_cn(String folderName_cn) {
        this.folderName_cn = folderName_cn;
    }
}
