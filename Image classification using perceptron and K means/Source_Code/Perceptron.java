import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Perceptron {

	public static void main(String[] args) {

		PercetronInputs percetronInputs = new PercetronInputs(args[0], args[1], args[2], args[3], Double.parseDouble(args[4]),
				Integer.parseInt(args[5]), args[6]);
		HashMap<String, Dictionary> words = new HashMap<String, Dictionary>();
		// build the dictionary first
		buildDictionary(percetronInputs, words);
		File spamTrainFolder = new File(percetronInputs.getSpamTrainFolder());
		File[] listOfSpamFiles = spamTrainFolder.listFiles();
		File hamTrainFolder = new File(percetronInputs.getHamTrainFolder());
		File[] listOfHamFiles = hamTrainFolder.listFiles();
		int M = listOfSpamFiles.length + listOfHamFiles.length;
		ArrayList<String> tokens = new ArrayList<String>();
		tokens.addAll(words.keySet());
		int N = tokens.size() + 2;
		int[][] vector = new int[M][N];
		// build vector
		vectorBuilder(vector, 0, listOfSpamFiles, percetronInputs.getSpamTrainFolder(), tokens, "spam", N);
		vectorBuilder(vector, listOfSpamFiles.length, listOfHamFiles, percetronInputs.getHamTrainFolder(), tokens, "ham", N);
		double pt[] = new double[M];
		Arrays.fill(pt, (1 / M));
		double wt[] = new double[N - 1];
		Arrays.fill(wt, 0.15);
		modifyWeights(vector, wt, M, N, pt, percetronInputs);
		File spamTestFolder = new File(percetronInputs.getSpamTestFolder());
		File[] listOfSpamTestFiles = spamTestFolder.listFiles();
		File hamTestFolder = new File(percetronInputs.getHamTestFolder());
		File[] listOfHamTestFiles = hamTestFolder.listFiles();
		double spamAccuracy = getPerceptronAccuracy(listOfSpamTestFiles, percetronInputs.getSpamTestFolder(), N, tokens, wt,
				"spam");
		System.out.println("spam Accuracy:       " + spamAccuracy);
		double hamAccuracy = getPerceptronAccuracy(listOfHamTestFiles, percetronInputs.getHamTestFolder(), N, tokens, wt, "ham");
		System.out.println("ham Accuracy:        " + hamAccuracy);

	}

	public static double getPerceptronAccuracy(File[] lists, String path, int COLUMN, ArrayList<String> words,
			double[] wt, String choice) {
		int count = 0;
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
				double samp[]= new double[COLUMN];
		    	samp[0]=1;
		    	for(int t = 0; t < tokens.length; t++) {
		    		  String word = tokens[t].toLowerCase();
		    		  //logic get ind word update x
		    		  //int pos = ;
		    		  samp[words.indexOf(word)+1]=samp[words.indexOf(word)+1]+1;
		    		  

		    	}
		    	double wx=wt[0];
		    	
					for(int c=1;c<COLUMN-1;c++){
					wx= wx+wt[c]*samp[c];
					}
		    	if(wx>0)
		    	count++;
		    	
			}
		}
		
		if ("spam".equals(choice)) {
			return ((double) count / lists.length) * 100;
		} else {
			return (((double) lists.length - count) / lists.length) * 100;
		}

	}

	public static void modifyWeights(int[][] vector, double[] wt, int ROW, int COLUMN, double[] pr, PercetronInputs percetronInputs) {
		for (int i = 0; i < percetronInputs.getIterations(); i++) {
			for (int j = 0; j < ROW; j++) {

				double phat = wt[0];
				for (int c = 1; c < COLUMN - 1; c++) {
					phat = phat + wt[c] * vector[j][c];
				}
				pr[j] = phat > 0 ? 1 : -1;

				double dw[] = new double[COLUMN - 1];
				for (int k = 0; k < COLUMN - 1; k++) {

					dw[k] = dw[k] + vector[j][k] * (vector[j][COLUMN - 1] - pr[j]);

				}

				for (int k = 0; k < COLUMN - 1; k++) {
					wt[k] = wt[k] + percetronInputs.getEta() * (dw[k]);
				}
			}

		}
	}

	public static void vectorBuilder(int[][] vector, int index, File[] list, String path, ArrayList<String> words,
			String choice, int n) {
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
				vector[index][0] = 1;
				if ("spam".equals(choice)) {
					vector[index][n - 1] = 1;
				} else if ("ham".equals(choice)) {
					vector[index][n - 1] = -1;
				}
				for (int t = 0; t < tokens.length; t++) {
					String word = tokens[t].toLowerCase();
					vector[index][words.indexOf(word) + 1] = vector[index][words.indexOf(word) + 1] + 1;

				}

			}
			index++;

		}
	}

	public static void buildDictionary(PercetronInputs percetronInputs, HashMap<String, Dictionary> words) {
	
		buildDictionaryUtil(percetronInputs.getSpamTrainFolder(), words, "spam", percetronInputs.getStopWords());
		buildDictionaryUtil(percetronInputs.getHamTrainFolder(), words, "ham", percetronInputs.getStopWords());

	}

	public static void buildDictionaryUtil(String path, HashMap<String, Dictionary> words, String choice,
			String isStopWords) {
		File foldertrain = new File(path);
		File[] listOfFiles = foldertrain.listFiles();
		List<String> stopWords = new ArrayList<String>();
		if ("yes".equals(isStopWords)) {
			StopWords st = new StopWords();
			stopWords = st.getStopWords();

		}
		for (File file : listOfFiles) {
			if (file.isFile()) {
				FileRead fd = new FileRead(file, path);
				try {
					fd.readFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String content = fd.getFileContent();
				String[] tokens = content.split("\\s+");
				for (int i = 0; i < tokens.length; i++) {
					String word = tokens[i].toLowerCase();
					if ("yes".equalsIgnoreCase(isStopWords) && stopWords.contains(word))
						continue;

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
				}

			}

		}
	}
}