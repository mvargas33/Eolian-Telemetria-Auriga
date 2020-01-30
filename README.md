# Telemetria_Auriga
Telemtry system of the Eolian Áuriga project from the University of Chile. Contains the back-end and front-end used to visualize data in real time inside and outside the solar car. More info at

# Dependencies and configuration

For Back-end:

* Import the directory *Protocol* into IntelliJ
* Configure the project to work with Java 8
* Right click in Protocol.iml and select *import modules* OR
* Go to *File->Project Strucure->Modules* to add all the JAR files inside the *libs* directory. The JAR files inside *JUnit* must be imported for testing, not compiling
* Run the *MainReceiver* and *MainSender* clases as requested

For Middleware and Front-end

* Run `npm install` inside the *Server API* and *VueJS front end* directories to install all dependencies

# Run the applications

Back end:

* Run the *MainReceiver* and *MainSender* clases as requested

Middleware:

* With the back-end working, run `node index.js` inside *Server API* to run the middleware between back-end and front-end

Front end:

* Run `npm run dev` inside *VueJS front end* to run the front-end
