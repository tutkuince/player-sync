# Go to the directory of the script (project root)
cd "$(dirname "$0")"

# Build the project (clean & package)
mvn clean package

# Move into the target directory where the JAR is
cd target


# Start the ThreadMain application in 'thread' mode (in-process with two threads)
echo "Starting ThreadMain (in-process, thread-based test)..."

java -jar player-sync-1.0-SNAPSHOT.jar --thread

read -n 1 -s -r -p "Press any key to exit..."
echo