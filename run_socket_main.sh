# Step 1: Compile the project
echo "Compiling the project..."
mvn clean compile

# Step 2: Run the SocketMain class
echo "Starting the SocketMain application..."
mvn exec:java -Dexec.mainClass="com.incetutku.SocketMain"

# Wait a few seconds for the see the result
sleep 5
