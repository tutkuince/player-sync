# Step 1: Compile the project
echo "Compiling the project..."
mvn clean compile

# Step 2: Run the ThreadMain class
echo "Starting the ThreadMain application..."
mvn exec:java -Dexec.mainClass="com.incetutku.ThreadMain"

# Wait a few seconds for the see the result
sleep 5
