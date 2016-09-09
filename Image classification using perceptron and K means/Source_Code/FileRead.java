import java.nio.*;
import java.nio.channels.FileChannel;
import java.io.*;


public class FileRead {

	private String file;
	private String fileContent;

	public FileRead(File file, String path) {
		this.file = path + file.getName();
	}

	public void readFile() throws IOException {
		FileInputStream fis = new FileInputStream(this.file);
		FileChannel fc = fis.getChannel();
		ByteBuffer buff = ByteBuffer.allocate((int) fc.size());
		fc.read(buff);
		fc.close();
		fis.close();
		this.fileContent = new String(buff.array());
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	
}