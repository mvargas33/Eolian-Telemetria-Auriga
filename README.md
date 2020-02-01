# Eolian Auriga Telemetry System
The telemetry system runs in a Raspberry Pi inside the car. On one hand, it uses a Java Application to receive data from the car and other logistics tasks. On the other hand, it runs a VueJS application to visualize data and interact with the users. The system uses a middleware written in JS to communicate back-end and front-end.

# Dependencies and configuration

For Back-end:

* Import the directory *Protocol* into IntelliJ
* Configure the project to work with Java 8
* To import dependencies:
  * Right click in Protocol.iml and select *import modules* OR
  * Go to *File->Project Strucure->Modules* to add all the JAR files inside the *libs* directory. The JAR files inside *JUnit* must be imported for testing, not compiling

For Middleware and Front-end:

* Run `npm install` inside the *Server API* and *VueJS front end* directories to install all dependencies

# Run the applications

Back end:

* Run the *MainReceiver* and *MainSender* classes as requested

Middleware:

* With back-end running, run `node index.js` inside *Server API* to run the middleware between back-end and front-end

Front end:

* Run `npm run dev` inside *VueJS front end* to run the front-end

# Notes

The project is under development. Basic data dynamics are already implemented. Front-end must be completed. Compatibility with Eolian Fenix (older solar car) is in progress.
