
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplementMain1 {
	public static int noOfIter;
	public static double eta;
	public static double lambda;
	public static void main(String[] args) {
		
		String spamTrainFolder = args[0];
		String hamTrainFolder = args[1];
		String spamTestFolder = args[2];
		String hamTestFolder = args[3];
		ImplementMain1.noOfIter = Integer.parseInt(args[4]);
		ImplementMain1.eta = Double.parseDouble(args[5]);
		ImplementMain1.lambda = Double.parseDouble(args[6]);
		
		HashMap<String, Dictionary> words = new HashMap<String, Dictionary>();
		
		File folderSpam = new File(spamTrainFolder);
		File folderHam = new File(hamTrainFolder);
		File[] listSpamFiles = folderSpam.listFiles();
		File[] listHamFiles = folderHam.listFiles();

		File folderTestSpam = new File(spamTestFolder);
		File folderTestHam = new File(hamTestFolder);
		File[] listSpamTestFiles = folderTestSpam.listFiles();
		File[] listHamTestFiles = folderTestHam.listFiles();
		
		System.out.println("1. WITH STOP WORDS\n\n");
		
		System.out.println("Naive Bayes\n");
		runNaiveBayes(words, listSpamFiles, listHamFiles, listSpamTestFiles, listHamTestFiles, spamTrainFolder,
				hamTrainFolder, spamTestFolder, hamTestFolder, "no", "no");
		
		System.out.println("\nLogistic Regression\n");
		runLogisticRegression(words, listSpamFiles, listHamFiles, listSpamTestFiles, listHamTestFiles, spamTrainFolder,
				hamTrainFolder, spamTestFolder, hamTestFolder, "no");
		System.out.println("*************************************************************");
		
		
		System.out.println("2. WITHOUT STOP WORDS\n\n");
		words = new HashMap<String, Dictionary>();
		System.out.println("Naive Bayes\n");
		runNaiveBayes(words, listSpamFiles, listHamFiles, listSpamTestFiles, listHamTestFiles, spamTrainFolder,
				hamTrainFolder, spamTestFolder, hamTestFolder, "yes", "no");
		//System.out.println(words.size());
		System.out.println("\nLogistic Regression\n");
		runLogisticRegression(words, listSpamFiles, listHamFiles, listSpamTestFiles, listHamTestFiles, spamTrainFolder,
				hamTrainFolder, spamTestFolder, hamTestFolder, "no");
		System.out.println("***************************************************************");

		words = new HashMap<String, Dictionary>();
	}

	public static void removeStopWords(HashMap<String, Dictionary> words) {
		StopWords st = new StopWords();
		for (String token : st.getStopWords()) {
			words.remove(token);
		}
	}

	public static void printTokens(HashMap<String, Dictionary> words) {
		ArrayList<String> tokens = new ArrayList<String>();
		tokens.addAll(words.keySet());
		for (String string : tokens) {
			System.out.println(string);
		}
	}

	public static void printWords(HashMap<String, Dictionary> words) {
		for (Map.Entry<String, Dictionary> entry : words.entrySet()) {
			System.out.println("word is:" + entry.getKey() + "in spam : " + entry.getValue().getspamCount()
					+ " in Ham : " + entry.getValue().getHamCount());
		}
	}

	public static void runNaiveBayes(HashMap<String, Dictionary> words, File[] listSpamFiles, File[] listHamFiles,
			File[] listSpamTestFiles, File[] listHamTestFiles, String spamTrainFolder, String hamTrainFolder,
			String spamTestFolder, String hamTestFolder, String stopWords, String fs) {

		PreCalculate preCalculate = new PreCalculate(listSpamFiles.length, listHamFiles.length);
		int totalSpamWordCount = buildDictionary(listSpamFiles, spamTrainFolder, words, "spam", stopWords, fs);
		int totalHamWordCount = buildDictionary(listHamFiles, hamTrainFolder, words, "ham", stopWords, fs);

		for (Map.Entry<String, Dictionary> entry : words.entrySet()) {
			entry.getValue().calcProbabilty(totalSpamWordCount, totalHamWordCount, words.size());
		}

		double priorSpam = Math.log((double) preCalculate.getSpamSum() / (preCalculate.getSpamSum() + preCalculate.getHamCount()));
		double priorHam = Math.log((double) preCalculate.getHamCount() / (preCalculate.getSpamSum() + preCalculate.getHamCount()));
		double spamAccuracy = getNaiveAccuracy(listSpamTestFiles, priorSpam, priorHam, words, spamTestFolder, "spam");
		System.out.println("Spam Accuracy is :    " + spamAccuracy);
		double hamAccuracy = getNaiveAccuracy(listHamTestFiles, priorSpam, priorHam, words, hamTestFolder, "ham");
		System.out.println("Ham Accuracy is:      " + hamAccuracy);

	}

	public static void runLogisticRegression(HashMap<String, Dictionary> words, File[] listSpamFiles,
			File[] listHamFiles, File[] listSpamTestFiles, File[] listHamTestFiles, String spamTrainFolder,
			String hamTrainFolder, String spamTestFolder, String hamTestFolder, String fs) {

		ArrayList<String> tokens = new ArrayList<String>();
		tokens.addAll(words.keySet());
		int M = listSpamFiles.length + listHamTestFiles.length;
		int N = tokens.size();
	
		int[][] vector = new int[M][N + 2];
	
		buildVector(vector, M, N + 2, 0, listSpamFiles, "spam", tokens, spamTrainFolder);
		buildVector(vector, M, N + 2, listSpamFiles.length, listHamFiles, "ham", tokens, hamTrainFolder);
		int count = 0;
		
		double[] wt = new double[N + 1];
		Arrays.fill(wt, 0.15);
		double[] pr = new double[M];
		int noOfIter = ImplementMain1.noOfIter;
		double eta = ImplementMain1.eta;
		double lambda = ImplementMain1.lambda;
		modifyWeights(vector, wt, noOfIter, M, N + 1, pr, eta, lambda);
		double spamAccuracy = getLogisticAccuracy(listSpamTestFiles, spamTestFolder, N + 2, tokens, wt, "spam", fs);
		System.out.println("spam Accuracy:       " + spamAccuracy);
		double hamAccuracy = getLogisticAccuracy(listHamTestFiles, hamTestFolder, N + 2, tokens, wt, "ham", fs);
		System.out.println("ham Accuracy:        " + hamAccuracy);

	}

	public static double getLogisticAccuracy(File[] lists, String path, int COLUMN, ArrayList<String> words,
			double[] wt, String choice, String fs) {
		int count = 0;
		Utilities porterObj = new Utilities();
		for (File file : lists) {

			if (file.isFile()) {
				FileRead fd = new FileRead(file, path);
				try {
					fd.readFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String content = fd.getFileContent();
				String[] tokens = content.split("\\s+");
				double sampleData[] = new double[COLUMN];
				sampleData[0] = 1;
				for (int t = 0; t < tokens.length; t++) {
					String word = tokens[t].toLowerCase();
					if ("yes".equals(fs)) {
						word = porterObj.stripAffixes(word);
					}
					sampleData[words.indexOf(word) + 1] = sampleData[words.indexOf(word) + 1] + 1;

				}
				double wx = wt[0];

				for (int a = 1; a < COLUMN - 1; a++) {
					wx = wx + wt[a] * sampleData[a];
					if (sampleData[a] != 0.0) {
					}
				}
				// System.out.println("wx is: "+ wx);
				if (wx > 0.0)
					count++;

			}

		}
		if ("spam".equals(choice)) {
			return ((double) count / lists.length) * 100;
		} else {
			return (((double) lists.length - count) / lists.length) * 100;
		}

	}

	public static void modifyWeights(int[][] vector, double[] wt, int noOfIter, int ROW, int COLUMN, double[] pr,
			double eta, double lambda) {
		for (int iter = 0; iter < noOfIter; iter++) {

			for (int row = 0; row < ROW; row++) {
				double p = wt[0];
				// System.out.println("for row number" + row);
				for (int column = 1; column < COLUMN; column++) {
					p = p + wt[column] * vector[row][column];
				}
				if (p <= 0) {
					
				}


				pr[row] = customSigmoid(p);
				if (row == 149 && iter == 0) {
					
				}
				
				if (iter == 0) {
					// System.out.println("pr row" + pr[row]);
				}
			}
			double dw[] = new double[COLUMN];
			for (int k = 1; k < COLUMN; k++) {
				for (int j = 0; j < ROW; j++) {
					dw[k] = dw[k] + vector[j][k] * (vector[j][COLUMN] - pr[j]);
					if (iter == 0) {
						
					}
					

				}

			}
			for (int k = 1; k < COLUMN; k++) {

				wt[k] = wt[k] + eta * (dw[k]) - (eta * lambda * wt[k]);
				

			}

			
		}

	}

	public static double customSigmoid(double p) {
		if (p > 100)
			return 1.0;
		else if (p < -100)
			return 0.0;
		else
			return 1 / (1 + Math.exp(-p));
		// return 1 / (1 + Math.exp(-p));
	}

	public static void buildVector(int[][] vector, int m, int n, int index, File[] list, String choice,
			ArrayList<String> words, String path) {
		for (File file : list) {

			FileRead fd = new FileRead(file, path);
			try {
				fd.readFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String content = fd.getFileContent();
			String[] tokens = content.split("\\s+");
			vector[index][0] = 1;
			if ("spam".equalsIgnoreCase(choice)) {
				vector[index][n - 1] = 1;
			} else {
				vector[index][n - 1] = 0;
			}
			for (int i = 0; i < tokens.length; i++) {
				String word = tokens[i].toLowerCase();
				vector[index][words.indexOf(word) + 1] = vector[index][words.indexOf(word) + 1] + 1;

			}

			index++;

		}

	}

	public static int buildDictionary(File[] list, String path, HashMap<String, Dictionary> words, String choice,
			String stopWordChoice, String fs) {
		int count = 0;
		StopWords st = new StopWords();
		Utilities porterObj = new Utilities();
		// String regexNumber = "\\d+";
		List<String> stopWords = st.getStopWords();
		for (File file : list) {
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
					if ("yes".equalsIgnoreCase(stopWordChoice) && stopWords.contains(word))
						continue;
					// if(word.matches(regexNumber)) continue;
					if (!word.matches("[a-zA-Z.? ]*"))
						continue;

					if ("yes".equals(fs)) {
						word = porterObj.stripAffixes(word);
					}
					if (words.containsKey(word)) {
						Dictionary token = (Dictionary) words.get(word);
						if ("spam".equalsIgnoreCase(choice)) {
							token.incrementSpamCount();
						} else {
							token.incrementHamCount();
						}
					} else {
						Dictionary token = new Dictionary(word);
						if ("spam".equalsIgnoreCase(choice)) {
							token.incrementSpamCount();
						} else {
							token.incrementHamCount();
						}
						words.put(word, token);
					}
					count++;
				}

			}

		}
		
		
		if ("yes".equalsIgnoreCase(fs)) {
			int c=0;
			HashMap<String, Dictionary> tempWords = new HashMap<String, Dictionary>();
			for (Map.Entry<String, Dictionary> entry : words.entrySet()) {
				tempWords.put(entry.getKey(), entry.getValue());
			}
			
			for (Map.Entry<String, Dictionary> entry : tempWords.entrySet()) {
				if ("spam".equalsIgnoreCase(choice) && entry.getValue().getspamCount() == 1)
					words.remove(entry.getKey());
				else if ("ham".equalsIgnoreCase(choice) && entry.getValue().getHamCount() == 1) {
					words.remove(entry.getKey());
				}else if("spam".equalsIgnoreCase(choice)){
					c += entry.getValue().getspamCount();
				}else if("ham".equalsIgnoreCase(choice)) {
					c+= entry.getValue().getHamCount();
				}

			}
			return c;
		}

		return count;
	}

	
	public static double getNaiveAccuracy(File[] list, double priorSpam, double priorHam,
			HashMap<String, Dictionary> words, String path, String choice) {
		double isSpam = 0.0;
		double isHam = 0.0;
		int count = 0;
		for (File file : list) {
			if (file.isFile()) {
				isSpam = priorSpam;
				isHam = priorHam;
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
					String token = tokens[i].toLowerCase();
					if (words.containsKey(token)) {
						Dictionary dic = (Dictionary) words.get(token);
						isSpam += dic.getSpamProbability();
						isHam += dic.getHamProbability();
					}
				}

				if ("spam".equals(choice) && isSpam > isHam)
					count++;

				if ("ham".equals(choice) && isHam > isSpam)
					count++;

			}
		}
	
		return (100 * (double) (count) / list.length);
	}

}