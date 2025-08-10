Please read notes file



WhitelistService Setup Commands

Prerequisites Install
bash# MongoDB
sudo apt update
sudo apt install -y mongodb
sudo systemctl start mongod
sudo systemctl enable mongod

RabbitMQ

sudo apt install -y rabbitmq-server
sudo systemctl start rabbitmq-server
sudo systemctl enable rabbitmq-server
sudo rabbitmq-plugins enable rabbitmq_management
Verify Services
bash# Check MongoDB
sudo systemctl status mongod

Check RabbitMQ

sudo systemctl status rabbitmq-server

RabbitMQ Web UI

http://localhost:15672 (guest/guest)

Run WhitelistService
bashcd WhitelistService
mvn clean install -DskipTests
mvn spring-boot:run
Test via RabbitMQ

Go to http://localhost:15672 (guest/guest)
Queues → upload.completed → Publish message:

Form 16 Test:
json{
"fileId": "test123",
"requestId": "req456",
"fileLocation": "s3://dummy/form16_user123_20250810.pdf",
"fileName": "form16_user123_20250810.pdf",
"fileType": "application/pdf"
}
Random File Test:
json{
"fileId": "test124",
"requestId": "req456",
"fileLocation": "s3://dummy/vacation_photos.pdf",
"fileName": "vacation_photos.pdf",
"fileType": "application/pdf"
}
Check Results
bash# MongoDB
mongosh
use fainancial_whitelist
db.whitelistServiceDetails.find().pretty()
Expected

Form 16: whitelisted: true
Random: whitelisted: false
Service logs show processing details