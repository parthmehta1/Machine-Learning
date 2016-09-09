/*** Author :Vibhav Gogate
The University of Texas at Dallas
*****/


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
 

public class KMeans {
	
	public static class AssignColor {
		double dist;
		int cluster;
		public AssignColor(double dist , int cluster) {
			this.dist = dist;
			this.cluster = cluster;
		}
	}
	public static final int maxIteration=100;
    public static void main(String [] args){
	if (args.length < 3){
	    System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
	    return;
	}
	try{
	    BufferedImage originalImage = ImageIO.read(new File(args[0]));
	    int k=Integer.parseInt(args[1]);
	    BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
	    ImageIO.write(kmeansJpg, "jpg", new File(args[2])); 
	    
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}	
    }
    
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
	int w=originalImage.getWidth();
	int h=originalImage.getHeight();
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	// Read rgb values from the image
	int[] rgb=new int[w*h];
	int count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		rgb[count++]=kmeansImage.getRGB(i,j);
	    }
	}
	// Call kmeans algorithm: update the rgb values
	kmeans(rgb,k);

	// Write the new rgb values to the image
	count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count++]);
	    }
	}
	return kmeansImage;
    }

    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k){
    	int []centroids = initCentroids(rgb, k);
    	HashMap<Integer, Integer> mapRGB = new LinkedHashMap<Integer, Integer>();
    	for(int i=0;i<rgb.length ;i++) { 
    		if(mapRGB.containsKey(rgb[i])) {
    			int count = mapRGB.get(rgb[i]);
    			mapRGB.put(rgb[i], count+1);
    		}else{
    			mapRGB.put(rgb[i], 1);
    		}
    	}
    	int [] previousCentroids = new int[centroids.length];
    	System.arraycopy(centroids, 0, previousCentroids, 0, centroids.length);
    	
    	Map<Integer,AssignColor>  assignColor = new HashMap<Integer, AssignColor>();
    	
    	boolean flag = false;
    	int it =1;
    	while(!flag) {
    		for(Entry<Integer, Integer> entry : mapRGB.entrySet()){
    			double dist = computeDistances(entry.getKey(), centroids[0]);
    			assignColor.put(entry.getKey(), new AssignColor(dist, 0));
    			
    			for(int j=0;j<centroids.length;j++) {
    				double newDist = computeDistances(entry.getKey(), centroids[j]);
    				if(newDist < assignColor.get(entry.getKey()).dist ) {
    					assignColor.get(entry.getKey()).dist = newDist;
    					assignColor.get(entry.getKey()).cluster = j;
    				}
    			}
    		}
    		
    		for(int j=0;j<k;j++) {
        		
	    		int[] s= new int[3];
	    		int c=0;
	    		int m=0;
	    		for(Entry<Integer, Integer> entry : mapRGB.entrySet()){
	    			if(assignColor.get(entry.getKey()).cluster==j)
	    			{
	    				Color temp = new Color(entry.getKey());
	    				s[0]=s[0]+(temp.getRed() * entry.getValue());
	    				s[1]=s[1]+(temp.getBlue() * entry.getValue());
	    				s[2]=s[2]+(temp.getGreen() * entry.getValue());
	    				c+=entry.getValue();
	    			}
	    			m++;
	    		}
	    		
	    		if(c!=0){
	    			Color temp = new Color(s[0]/c, s[1]/c, s[2]/c);
	    			centroids[j]= temp.getRGB();
	    		}

	    	}
    		
    		if(Arrays.equals(previousCentroids, centroids) || it==maxIteration) {
    			System.out.println("Image is Compressed and Converted.");
    			flag = true;
    			break;
    		}
    		
    		System.arraycopy(centroids, 0, previousCentroids, 0, centroids.length);
    		it++;
    	}
    	int p=0;
    	for(int i=0;i<rgb.length ;i++) {
			for(int j=0;j<k;j++) {
				if(assignColor.get(rgb[i]).cluster==j){
					rgb[i]=centroids[j];
					break;
				}
			}
			p++;
		}
    }
    private static double computeDistances(int i , int j) {
    	
    	Color point = new Color(i);
    	Color centroid = new Color(j);
    	
    	double blue = Math.abs(Math.pow((point.getBlue() - centroid.getBlue()),2));
    	double red = Math.abs(Math.pow((point.getRed() - centroid.getRed()),2));
    	double green = Math.abs(Math.pow((point.getGreen() - centroid.getGreen()),2));
    	
    	return Math.sqrt((blue+red+green));
    }
    
    private static int[] initCentroids(int [] rgb , int k ) {
    	int centroids[] = new int[k];
    	int length =rgb.length;
    	for(int i=0 ;i< k ; i++) {
    		int centroid = rgb[(int) (Math.floor(length*Math.random()))];
    		boolean found = false;
    		while(!found) {
    			for(int j=0; j< i-1;j++) {
    				if(centroids[j] == centroid) {
    					centroid = rgb[(int) (Math.floor(length*Math.random()))];
    					break;
    				}
    				
    			}
    			
    			found = true;
    		}
    		centroids[i] = centroid;
    	}
    	
    	return centroids;
    }

}