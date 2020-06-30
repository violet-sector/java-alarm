# To build from source the following commands should be run from the java-alarm directory

# Compile the java files
javac -d ./ ./*.java

# Create an executable jar file
jar cmvf META-INF/MANIFEST.MF alarm.jar com resources

# Run the programme
java -jar alarm.jar
