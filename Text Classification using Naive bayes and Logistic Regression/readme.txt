1. Unzip the folder containing code 

2. Set the path in the command prompt, where all the files are located. Use command 'cd' and then desired path.
One Example is:

cd C:\Users\Parth\Desktop\ML@
 

2. Compile the java file which is main class file by running following command:

javac ImplementMain1.java

3. Run the main class with the help of following command.

java ImplementMain1 p1 p2 p3 p4 p5 p6 p7 

Where,

p1: Absolute path of folder containing spam train folder 
p2: Absolute path of folder containing ham train folder
p3: Absolute path of folder containing spam test folder
p4: Aboslute path of folder containing ham test folder 
p5: Number of iterations
p5: Learning Rate value
p6: lambda value

One example is 

java ImplementMain1 C:\Users\Parth\Desktop\ML@\train\spam\ C:\Users\Parth\Desktop\ML@\train\ham\ C:\Users\Parth\Desktop\ML@\test\spam\ C:\Users\Parth\Desktop\ML@\test\ham\ 100 0.01 2


Also, I have attached the screenshot of the run in cmd window.