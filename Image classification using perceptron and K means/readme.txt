A. Perceptron Implementation


1. Unzip the folder containing code 

2. Set the path in the command prompt, where all the files are located. Use command 'cd' and then desired path.
One Example is:

cd C:\Users\Parth\Desktop\ML@
 

2. Compile the java file which is main class file by running following command:

javac Perceptron.java

3. Run the main class with the help of following command.

java Perceptron p1 p2 p3 p4 p5 p6 p7 

Where,

p1: Absolute path of folder containing spam train folder 
p2: Absolute path of folder containing ham train folder
p3: Absolute path of folder containing spam test folder
p4: Aboslute path of folder containing ham test folder 
p5: Learning Rate value
p6: Number of iterations
p7: {yes,no}. you have to select 1 value from 'yes' and 'no. For including or excluding stop words. yes-exclude stop words.

One example is 


C:\Users\Parth\Desktop\MLAssignment3\Parth_Mehta_pjm150030\Source_Code>java Perceptron C:\Users\Parth\Desktop\MLAssignment3\Parth_Mehta_pjm150030\train\spam\ C:\Users\Parth\Desktop\MLAssignment3\
\Users\Parth\Desktop\MLAssignment3\Parth_Mehta_pjm150030\test\spam\ C:\Users\Parth\Desktop\MLAssignment3\Parth_Mehta_pjm150030\test\ham\ 2.43 100 no

Also, I have attached the screenshot of the run in cmd window.




B. K means 


1. It is in the same folder where Perceptron is.

2. Compile the java file which is main class file by running following command:

javac KMeans.java

3. Run the main class with the help of following command.

java Kmeans p1 p2 p3

where 
p1 : Absolute path for input image with name of input image
p2 : K value(2,5,10,15,20 etc)
p3 : Absolute path for output image with name of output image.

One example is 
C:\Users\Parth\Desktop\MLAssignment3\Parth_Mehta_pjm150030\Source_Code>java KMeans C:\Users\Parth\Desktop\MLAssignment3\Parth_Mehta_pjm150030\Koala.jpg 5 C:\Users\Parth\Desktop\MLAssignment3\
Parth_Mehta_pjm150030\KoalaOut.jpg


C. Converting Ham and Spam into arff

This program is only for folder. Not for Zip file. We have to provide a root folder. the folder should have subfolder named ham and spam

1. Compile the java file which is main class file by running following command:

javac Texttoarff .java

2. Run following command:

java Texttoarff p1 p2

p1: absolute path of train folder
p2: absolute path of test folder

One example is 

java Texttoarff C:\Users\Parth\Desktop\MLAssignment3\Parth_Mehta_pjm150030\test\ C:\Users\Parth\Desktop\MLAssignment3\Parth_Mehta_pjm150030\train\

The arff files are generated in the path where the main class is. 


D. Running Weka code for Multilayered perceptron

I have created separate folder for this named 'Weka_MultiPerc' in which I have kept test.arff, train.arff and weka.jar files. 
For running this, we have to make sure that everything is in same folder.

1. give the path of the folder in which above 3 things are there.

2. run the following code:

java -Xmx2g -cp weka.jar weka.classifiers.functions.MultilayerPerceptron arg

where arg = parameters that we have to give:

I have listed the sample parameters that I have used to calculate.


"-L 0.03  -M 0.1 -N 2 -E 20 -H 2 -B -C -I -t train.arff -T test.arff -o"  
"-L 0.03  -M 0.2 -N 20 -E 20 -H 4,4,4 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.1  -M 0.4 -N 2 -E 20 -H 5 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.1  -M 0.1 -N 20 -E 20 -H 10 -B -C -I -t train.arff -T test.arff -o"
"-L 0.1  -M 0.2 -N 50 -E 20 -H 10,10,10 -B -C -I -t train.arff -T test.arff -o"  
"-L 0.1  -M 0.4 -N 100 -E 20 -H 10,5,2 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.3  -M 0.1 -N 2 -E 20 -H 4 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.3  -M 0.1 -N 20 -E 20 -H 10 -B -C -I -t train.arff -T test.arff -o"  
"-L 0.3  -M 0.2 -N 50 -E 20 -H 10,10,10 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.3  -M 0.4 -N 100 -E 20 -H 10,10 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.9  -M 0.1 -N 2 -E 20 -H 4 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.9  -M 0.2 -N 10 -E 20 -H 10 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.9  -M 0.2 -N 20 -E 20 -H 10,10 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.9  -M 0.4 -N 50 -E 20 -H 10,10,10 -B -C -I -t train.arff -T test.arff -o" 
"-L 0.9  -M 0.6 -N 50 -E 20 -H 10,5,2 -B -C -I -t train.arff -T test.arff -o"


U have to take one of this parameter without quote.

Sample code is:

java -Xmx2g -cp weka.jar weka.classifiers.functions.MultilayerPerceptron -L 0.03  -M 0.1 -N 2 -E 20 -H 2 -B -C -I -t train.arff -T test.arff -o







