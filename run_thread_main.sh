# Package the project as a JAR (only needs to be done once or after code changes)
mvn clean package

# Run the JAR file
java -cp target/PlayerCommunicationProject.jar com.incetutku.ThreadMain

# Optional: Wait for the server process to finish
echo "Press any key to end the program..."
read -n 1 -s  # Waits for a single key press