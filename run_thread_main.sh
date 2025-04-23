mvn clean package

cd target

java -jar player-sync-1.0-SNAPSHOT.jar --thread

read -n 1 -s -r -p "Press any key to exit..."
echo