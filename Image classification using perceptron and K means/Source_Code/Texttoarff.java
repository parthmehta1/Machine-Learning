import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class Texttoarff {

	public static void main(String[] args) throws IOException {
		TreeSet<String> attr = new TreeSet<String>();
		String trainpath = args[0];
		String testPath = args[1];
		buildAttributes(trainpath, attr);
		buildAttributes(testPath, attr);
		buildArffFile("train.arff", attr, trainpath);
		buildArffFile("test.arff", attr, testPath);

	}

	public static void buildArffFile(String fileName, TreeSet<String> attr, String path) throws IOException {
		String spamPath = path + "\\spam//";
		String hamPath = path + "\\ham//";
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.append("@relation  'check1'");
		bw.newLine();
		for (Iterator iterator = attr.iterator(); iterator.hasNext();) {
			bw.append("@ATTRIBUTE " + (String) iterator.next() + " NUMERIC");
			bw.newLine();
		}
		bw.append("@ATTRIBUTE @@class@@ {-1,1}");
		bw.newLine();
		bw.append("@DATA");
		bw.newLine();
		buildData(spamPath, 1, bw, attr);
		buildData(hamPath, -1, bw, attr);
		bw.close();

	}

	public static void buildAttributes(String path, TreeSet<String> attr) {
		String spamPath = path + "\\spam//";
		String hamPath = path + "\\ham//";
		buildWordAttributes(attr, spamPath);
		buildWordAttributes(attr, hamPath);
	}

	public static void buildData(String path, int outputClass, BufferedWriter bw, TreeSet<String> attr)
			throws IOException {
		System.out.println("--> path : " + path);
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		StopWords st = new StopWords();
		List<String> stopWords = st.getStopWords();
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				FileRead fd = new FileRead(file, path);
				try {
					fd.readFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String content = fd.getFileContent();
				String[] tokens = content.split("\\s+");
				TreeMap<String, Integer> countMap = new TreeMap<String, Integer>();
				for (int i = 0; i < tokens.length; i++) {
					String word = tokens[i].toLowerCase();
					if (stopWords.contains(word)) {
						continue;
					}
					if (!word.matches("[a-zA-Z.? ]*"))
						continue;
					if (countMap.containsKey(word)) {
						int count = countMap.get(word);
						countMap.put(word, count + 1);
					} else {
						countMap.put(word, 1);
					}

				}
				StringBuilder row = new StringBuilder();
				row.append("{");
				for (Entry<String, Integer> entry : countMap.entrySet()) {
					row.append(getIndex(entry.getKey(), attr) + " " + entry.getValue() + ",");
				}
				row.append(attr.size() + " " + outputClass);
				row.append("}");
				bw.append(row.toString());
				bw.newLine();
			}
		}
	}

	public static int getIndex(String word, TreeSet<String> attr) {
		int index = 0;
		for (Iterator iterator = attr.iterator(); iterator.hasNext();) {
			if (word.equals((String) iterator.next()))
				return index;
			index++;
		}
		return index;
	}

	public static void buildWordAttributes(TreeSet<String> attr, String path) {
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		StopWords st = new StopWords();
		List<String> stopWords = st.getStopWords();
		

		for (File file : listOfFiles) {
			if (file.isFile()) {
				FileRead fd = new FileRead(file, path);
				try {
					fd.readFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String content = fd.getFileContent();
				String[] tokens = content.split("\\s+");
				for (int i = 0; i < tokens.length; i++) {
					String word = tokens[i].toLowerCase();
					if (stopWords.contains(word)) {
						continue;
					}
					if (!word.matches("[a-zA-Z.? ]*"))
						continue;
					if (!attr.contains(word)) {
						attr.add(word);
					}
				}
			}
		}
	}
}