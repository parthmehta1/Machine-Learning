public class PercetronInputs {

	private String spamTrainFolder;
	private String hamTrainFolder;
	private String spamTestFolder;
	private String hamTestFolder;
	private double eta;
	private int iterations;
	private String stopWords;

	public PercetronInputs(String spamTrainFolder, String hamTrainFolder, String spamTestFolder, String hamTestFolder,
			double eta, int noOfIter, String stopWords) {

		this.spamTrainFolder = spamTrainFolder;
		this.hamTrainFolder = hamTrainFolder;
		this.spamTestFolder = spamTestFolder;
		this.hamTestFolder = hamTestFolder;
		this.eta = eta;
		this.iterations = noOfIter;
		this.stopWords = stopWords;

	}

	public String getSpamTrainFolder() {
		return spamTrainFolder;
	}

	public void setSpamTrainFolder(String spamTrainFolder) {
		this.spamTrainFolder = spamTrainFolder;
	}

	public String getHamTrainFolder() {
		return hamTrainFolder;
	}

	public void setHamTrainFolder(String hamTrainFolder) {
		this.hamTrainFolder = hamTrainFolder;
	}

	public String getSpamTestFolder() {
		return spamTestFolder;
	}

	public void setSpamTestFolder(String spamTestFolder) {
		this.spamTestFolder = spamTestFolder;
	}

	public String getHamTestFolder() {
		return hamTestFolder;
	}

	public void setHamTestFolder(String hamTestFolder) {
		this.hamTestFolder = hamTestFolder;
	}

	public double getEta() {
		return eta;
	}

	public void setEta(double eta) {
		this.eta = eta;
	}

	public int getIterations() {
		return iterations;
	}

	public void setNoOfIter(int noOfIter) {
		this.iterations = noOfIter;
	}

	public String getStopWords() {
		return stopWords;
	}

	public void setStopWords(String stopWords) {
		this.stopWords = stopWords;
	}

}