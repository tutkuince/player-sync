# Go to the directory of the script (project root)
cd "$(dirname "$0")"

# Build the project (clean & package)
mvn clean package

# Move into the target directory where the JAR is
cd target

echo "Starting SocketServer..."
java -jar player-sync-1.0-SNAPSHOT.jar --server & SERVER_PID=$!
sleep 1

echo "Starting ClientApp 1..."
java -jar player-sync-1.0-SNAPSHOT.jar --client & CLIENT1_PID=$!
sleep 1

echo "Starting ClientApp 2..."
java -jar player-sync-1.0-SNAPSHOT.jar --client & CLIENT2_PID=$!

echo "All processes started. Waiting for them to finish."
wait $SERVER_PID
wait $CLIENT1_PID
wait $CLIENT2_PID

read -n 1 -s -r -p "Press any key to exit..."
echo