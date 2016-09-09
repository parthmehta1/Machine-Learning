
public class Dictionary 
{
	private String token;
	private int spamCount; 
	private int hamCount; 
	private double spamProbability;
	private double hamProbability;

	public Dictionary(String token) {
		this.token = token;
		this.hamCount = 0;
		this.spamCount = 0;
		this.hamProbability = 0.0;
		this.spamProbability = 0.0;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getspamCount() {
		return spamCount;
	}

	public void setspamCount(int spamCount) {
		this.spamCount = spamCount;
	}

	public int getHamCount() {
		return hamCount;
	}

	public void setHamCount(int hamCount) {
		this.hamCount = hamCount;
	}

	public void incrementHamCount() {
		this.hamCount++;
	}

	public void incrementSpamCount() {
		this.spamCount++;
	}

	public double getSpamProbability() {
		return spamProbability;
	}

	public void setSpamProbability(double spamProbability) {
		this.spamProbability = spamProbability;
	}

	public double getHamProbability() {
		return hamProbability;
	}

	public void setHamProbability(double hamProbability) {
		this.hamProbability = hamProbability;
	}

	public void calcProbabilty(int totalSpam, int totalHam , int dictSize) {
		this.spamProbability = Math.log((double)(this.spamCount +1)/(totalSpam + dictSize));
		this.hamProbability = Math.log((double)(this.hamCount +1)/(totalHam + dictSize));
	}

}
