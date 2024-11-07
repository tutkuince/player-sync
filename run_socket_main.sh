# Step 1: Compile and package the project with Maven
echo "Packaging project with Maven..."
mvn clean package

# Step 2: Start the PlayerServer in the background
echo "Starting PlayerServer..."
java -cp target/PlayerCommunicationProject.jar com.incetutku.socket.server.PlayerServer &

# Step 3: Wait for 3 seconds to allow the server to initialize
sleep 3

# Step 4: Start the PlayerClient
echo "Starting PlayerClient..."
java -cp target/PlayerCommunicationProject.jar com.incetutku.socket.client.PlayerClient

# Optional: Wait for the server process to finish
echo "Press any key to end the program..."
read -n 1 -s  # Waits for a single key press