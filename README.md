Authors
David Macy & Ryan O'Valley

We developed the Italian Football (Soccer) Academy web application using Java, SpringBoot, and JPA to
persist data in a locally hosted MySQL database and keep a record of all user profiles so that users can
log in and access their training drills based on their age. We also created comprehensive unit tests for the user
service class using JUnit and an H2 database created in memory to help minimize any potential bugs in the business logic
of the web application. We also used SLF4J to record events to a log file while the web application is running to
provide valuable information and help pinpoint the cause for any performance issues. JavaDoc and descriptive in-line
comments for classes and methods can be found to help create more understandable code.

In order to run the project the viewer will need to have a few things already installed. MySQL must be installed since the
database is running [MySQL]
(https://dev.mysql.com/downloads/installer/).

The viewer will also need to have [Java]
(https://www.oracle.com/java/technologies/downloads/) installed. I would recommend Java 17 and you can check
to see what version is installed if you run:
java -version

Finally, make sure you have [GIT]
(https://git-scm.com/downloads) installed and setup so that you can clone the project. Once all of these have been
installed the user will need to clone the project.

Once all of these things are installed I recommend going to your command line and cloning the project in a directory of
your choice using the command:
'''sh
git clone https://github.com/djmacy/ifa-web-app
'''
After you have the project cloned you can run the command in the terminal:
'''
gradlew bootRun
'''
After starting you should eventually see a line that looks similar to the following.
'''sh
2023-10-18 11:35:02,786 - [INFO] - from edu.carroll.ifa.IfaApplication in restartedMain
Started IfaApplication in 5.857 seconds (process running for 6.433)
'''
Open the following URL in your browser to check out the web application:
<http://localhost:8080>

To stop running the application you can click the ctrl c command.
