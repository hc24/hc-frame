package com.hc24.frame.util.verify;

import java.io.File;
import java.io.FileFilter;

/**
 * 字模文件过滤
 * @author 6tail
 *
 */
public class ZmFileFilter implements FileFilter {

	
	public boolean accept(File f) {
		return !f.isDirectory()&&f.getName().endsWith(".gif");
	}

}
